// src/main/java/com/adharsh/adharshmart/exception/UnauthorizedException.java
package com.adharsh.adharshmart.exception;

public class UnauthorizedException extends AppException {
    public UnauthorizedException(String message) {
        super(401, "UNAUTHORIZED", message);
    }
}