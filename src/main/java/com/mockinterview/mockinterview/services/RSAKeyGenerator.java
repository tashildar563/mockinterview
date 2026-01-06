package com.mockinterview.mockinterview.services;

import java.security.KeyPair;
import java.security.KeyPairGenerator;

public class RSAKeyGenerator {
  public static KeyPair generateRSAKeyPair() throws Exception {
    KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
    generator.initialize(2048);
    return generator.generateKeyPair();
  }

}
