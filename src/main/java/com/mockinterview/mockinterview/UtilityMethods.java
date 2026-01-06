package com.mockinterview.mockinterview;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class UtilityMethods {
    public static boolean isNotBlank(Object obj) {
        return obj != null && obj != "" && obj != " " && obj != "null";
    }

    public static String stringOf(Object obj) {
        return isNotBlank(obj) ? String.valueOf(obj) : "";
    }

    public static LocalDateTime getLocalDateTime(long millis){
      return Instant.ofEpochMilli(millis)
          .atZone(ZoneId.systemDefault())
          .toLocalDateTime();
    }
}
