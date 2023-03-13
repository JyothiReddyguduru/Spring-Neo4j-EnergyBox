package com.energybox.backendcodingchallenge.custom.exception;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(Long id) {
        super("Sensor #" + id + " was not found");
    }
}