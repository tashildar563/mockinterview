package com.mockinterview.mockinterview.services;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.mockinterview.mockinterview.entities.ApiResponse;
import java.io.IOException;
import java.security.PublicKey;
import org.springframework.beans.factory.annotation.Autowired;

public class ApiResponseEncryptingSerializer extends JsonSerializer<ApiResponse<?>> {

  private final ObjectMapper mapper = new ObjectMapper();
 @Autowired
  private KeyService keyService;

  @Override
  public void serialize(ApiResponse<?> value, JsonGenerator gen, SerializerProvider serializers)
      throws IOException {

    try {
      // 1. Convert the ApiResponse to JSON string
      String json = mapper.writeValueAsString(value);

      PublicKey publicKey = keyService.getPublicKey(); // your way of loading the key
      String encrypted = EncryptionUtil.encryptWithPublicKey(json, publicKey);

      // 3. Write encrypted wrapper
      gen.writeStartObject();
      gen.writeStringField("encrypted", encrypted);
      gen.writeEndObject();

    } catch (Exception e) {
      throw new RuntimeException("Failed to encrypt ApiResponse", e);
    }
  }
}
