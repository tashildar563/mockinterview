package com.mockinterview.mockinterview.controller.advice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mockinterview.mockinterview.entities.ApiResponse;
import com.mockinterview.mockinterview.services.EncryptionUtil;
import com.mockinterview.mockinterview.services.KeyService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

public class EncryptionAdvice implements ResponseBodyAdvice<Object> {

  @Autowired
  private KeyService keyService;

  private final ObjectMapper mapper = new ObjectMapper();

  @Override
  public boolean supports(
      MethodParameter returnType,
      Class<? extends HttpMessageConverter<?>> converterType) {

    // Apply only when controller returns ApiResponse
    return ApiResponse.class.isAssignableFrom(returnType.getParameterType());
  }

  @Override
  public Object beforeBodyWrite(
      Object body,
      MethodParameter returnType,
      MediaType selectedContentType,
      Class<? extends HttpMessageConverter<?>> selectedConverterType,
      ServerHttpRequest request,
      ServerHttpResponse response) {

    try {
      String json = mapper.writeValueAsString(body);
      String encrypted = EncryptionUtil.encryptWithPublicKey(
          json,
          keyService.getPublicKey()
      );

      return Map.of("encrypted", encrypted);

    } catch (Exception e) {
      throw new RuntimeException("Response encryption failed", e);
    }}
}
