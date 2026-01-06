package com.mockinterview.mockinterview.services;

import static java.nio.file.Files.write;

import com.mockinterview.mockinterview.CoreConstant;
import com.mockinterview.mockinterview.entities.ApiResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import javax.crypto.Cipher;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.bouncycastle.asn1.x500.X500Name;

@Service
public class KeyService {

  public KeyPair generateKeys() throws Exception {

    KeyPair keyPair = RSAKeyGenerator.generateRSAKeyPair();

    String pemPublic = "-----BEGIN PUBLIC KEY-----\n"
        + Base64.getMimeEncoder(64, "\n".getBytes())
        .encodeToString(keyPair.getPublic().getEncoded())
        + "\n-----END PUBLIC KEY-----\n";

    String pemPrivate = "-----BEGIN PRIVATE KEY-----\n"
        + Base64.getMimeEncoder(64, "\n".getBytes())
        .encodeToString(keyPair.getPrivate().getEncoded())
        + "\n-----END PRIVATE KEY-----\n";

    Files.write(Paths.get("public_key.pem"), pemPublic.getBytes(StandardCharsets.UTF_8));
    Files.write(Paths.get("private_key.pem"), pemPrivate.getBytes(StandardCharsets.UTF_8));

    System.out.println("Public & Private PEM created.");
    System.out.println("Generating certificate...");

    X500Name issuer = new X500Name("CN=MyApp");
    BigInteger serial = BigInteger.valueOf(System.currentTimeMillis());
    Date start = new Date();
    Date end = new Date(start.getTime() + 365L * 24 * 60 * 60 * 1000);

    ContentSigner signer = new JcaContentSignerBuilder("SHA256withRSA")
        .setProvider("BC")
        .build(keyPair.getPrivate());

    X509v3CertificateBuilder certBuilder = new JcaX509v3CertificateBuilder(
        issuer, serial, start, end, issuer, keyPair.getPublic());

    X509Certificate cert = new JcaX509CertificateConverter()
        .setProvider("BC")
        .getCertificate(certBuilder.build(signer));
    if (cert == null) {
      throw new RuntimeException("Certificate generation failed!");
    }
    // 3. PKCS12 keystore
    KeyStore ks = KeyStore.getInstance("PKCS12");
    ks.load(null, null);

    String password = "changeit";

    ks.setKeyEntry("mykey", keyPair.getPrivate(),
        password.toCharArray(), new Certificate[]{cert});

    try (FileOutputStream fos = new FileOutputStream("keystore.p12")) {
      ks.store(fos, password.toCharArray());
    }

    System.out.println("Keystore generated: keystore.p12");

    return keyPair;
  }

  public String encodePublic(KeyPair keyPair) {
    return Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
  }

  public String encodePrivate(KeyPair keyPair) {
    return Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
  }

  public PublicKey getPublicKey() throws Exception{
    String b64 = System.getenv("KEYSTORE_B64");
    byte[] decoded = Base64.getDecoder().decode(b64);

    KeyStore ks = KeyStore.getInstance("PKCS12");
    ks.load(new ByteArrayInputStream(decoded), System.getenv("KEYSTORE_PSW").toCharArray());
    String alias = ks.aliases().nextElement();
    java.security.cert.Certificate cert = ks.getCertificate(alias);
    return cert.getPublicKey();
  }
  public PrivateKey getPrivateKey() throws Exception{
    String b64 = System.getenv("KEYSTORE_B64");
    byte[] decoded = Base64.getDecoder().decode(b64);

    KeyStore ks = KeyStore.getInstance("PKCS12");
    ks.load(new ByteArrayInputStream(decoded), System.getenv("KEYSTORE_PSW").toCharArray());
    String alias = ks.aliases().nextElement();

// Use this to get the private key
    Key key = ks.getKey(alias, System.getenv("KEYSTORE_PSW").toCharArray());

    if (key instanceof PrivateKey) {
      return (PrivateKey) key;
    }
    throw new RuntimeException("No private key found in keystore");
  }
  public static String decrypt(String encryptedPayload, PrivateKey privateKey) throws Exception {
    System.out.println("private key "+Base64.getMimeEncoder(64, "\n".getBytes())
        .encodeToString(privateKey.getEncoded()));
    byte[] encryptedBytes = Base64.getDecoder().decode(encryptedPayload);

    Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding"); // MUST MATCH JSEncrypt
    cipher.init(Cipher.DECRYPT_MODE, privateKey);

    byte[] decrypted = cipher.doFinal(encryptedBytes);
    return new String(decrypted, StandardCharsets.UTF_8);
  }
  @Autowired
  JwtUtil jwtUtil;
  public String getUserId(HttpServletRequest request){
    String authHeader = request.getHeader("Authorization");
    if (authHeader == null || !authHeader.startsWith("Bearer")) {
      return null;
    }

    Claims claims = Jwts.parser()
        .setSigningKey(jwtUtil.getSecretKey())
        .build()
        .parseClaimsJws(authHeader.substring(7))
        .getBody();
    return claims.get(CoreConstant.ID_L_CASE).toString();
  }
}
