package server.util;

import commands.ExecutionPayload;

public class RequestValidator {
  public static boolean isLong(Object payload) {
    return payload instanceof Long;
  }

  public static boolean isExecutable(Object payload) {
    return payload instanceof ExecutionPayload;
  }
}
