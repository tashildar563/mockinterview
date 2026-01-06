package com.mockinterview.mockinterview.services;

import com.mockinterview.mockinterview.CoreConstant;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

  public Key getSecretKey() {
    return KEY;
  }

  private final String SECRET_KEY = "123456789qwertyuiiopasdfghjklxcvbnm123456789"; // 32+ chars
  private final Key KEY = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());


  public String generateToken(String userName,String id, String roleCode) {
    long currentMillis = System.currentTimeMillis();
    Date expiresAt = new Date(currentMillis + 360000);
    Map<String, Object> claims = new HashMap<>();
    claims.put("id", id);
    claims.put("role_code",roleCode);
    claims.put(CoreConstant.ISSUED_AT_L_CASE, currentMillis);
    claims.put(CoreConstant.EXPIRE_AT_L_CASE,expiresAt.getTime());
    return Jwts.builder().setClaims(claims).setSubject(userName).setIssuedAt(new Date())
        .setExpiration(expiresAt)
        .signWith(KEY, SignatureAlgorithm.HS256).compact();
  }

  public String extractUsername(String token) {
    return Jwts.parser().setSigningKey(KEY).build().parseClaimsJws(token).getBody().getSubject();
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parser().setSigningKey(KEY).build().parseClaimsJws(token);
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}
