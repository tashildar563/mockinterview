package com.mockinterview.mockinterview.controller;

import com.mockinterview.mockinterview.entities.ApiResponse;
import com.mockinterview.mockinterview.services.KeyService;
import java.security.Security;
import java.util.Map;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KeyController {

  private final KeyService keyService;

  public KeyController(KeyService keyService) {
    this.keyService = keyService;
  }

  @PostMapping("/generate-keys")
  public ResponseEntity<Map<String,Object>> generateKeys() throws Exception {
    String alias = System.getenv("ENVIRONMENT");
    if(!alias.equals("DEVELOPMENT")){
      return ResponseEntity.ok(Map.of("status","FAIL","message","Key Generated In Development Environment Only.","data",Map.of()));
    }
    Security.addProvider(new BouncyCastleProvider());
    var keyPair = keyService.generateKeys();
    // Save public key

    return ResponseEntity.ok(Map.of("status","SUCCESS","message","Key generation completed successfully.","data",Map.of()));
  }

  @GetMapping("/getPublicKey")
  public ResponseEntity<Map<String,Object>> getPublicKey()
      throws Exception {
    return ResponseEntity.ok(Map.of("status","SUCCESS","message","Public Key Fetched successfully.","data",Map.of("public_key",keyService.getPublicKey().getEncoded())));
  }
}
