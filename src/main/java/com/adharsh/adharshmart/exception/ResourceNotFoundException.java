// src/main/java/com/adharsh/adharshmart/exception/ResourceNotFoundException.java
package com.adharsh.adharshmart.exception;

public class ResourceNotFoundException extends AppException {
    public ResourceNotFoundException(String message) {
        super(404, "NOT_FOUND", message);
    }
}