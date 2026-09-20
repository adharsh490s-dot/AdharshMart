package com.adharsh.adharshmart.dto;

public record ApiResponse<T>(boolean success, T data, String error) {
  public static <T> ApiResponse<T> ok(T data) { return new ApiResponse<>(true, data, null); }
  public static <T> ApiResponse<T> error(String message) { return new ApiResponse<>(false, null, message); }
  public static <T> ApiResponse<T> fail(String code, String message) { return error(code + ": " + message); }
}
