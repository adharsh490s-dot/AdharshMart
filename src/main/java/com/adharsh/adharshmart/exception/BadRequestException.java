// src/main/java/com/adharsh/adharshmart/exception/BadRequestException.java
package com.adharsh.adharshmart.exception;

public class BadRequestException extends AppException {
    public BadRequestException(String message) {
        super(400, "BAD_REQUEST", message);
    }
}