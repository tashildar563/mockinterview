package com.mockinterview.mockinterview.services;

import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.util.Base64;
import javax.crypto.Cipher;

public class EncryptionUtil {
  public static String encryptWithPublicKey(String plainText, PublicKey publicKey) throws Exception {
    Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
    cipher.init(Cipher.ENCRYPT_MODE, publicKey);
    byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
    return Base64.getEncoder().encodeToString(encrypted);
  }
}
