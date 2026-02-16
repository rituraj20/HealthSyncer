package com.healthsync.Patient.Exception;

public class ApiException extends RuntimeException {
    public ApiException(String message) {
        super(message);
    }
}
