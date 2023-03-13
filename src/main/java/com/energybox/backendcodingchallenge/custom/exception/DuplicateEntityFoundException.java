package com.energybox.backendcodingchallenge.custom.exception;

public class DuplicateEntityFoundException extends RuntimeException {

    public DuplicateEntityFoundException(String message) {
        super(message);
    }
}