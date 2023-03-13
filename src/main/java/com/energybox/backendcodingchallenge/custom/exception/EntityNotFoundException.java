package com.energybox.backendcodingchallenge.custom.exception;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String entity, Long id) {
        super(entity + "#" + id + " was not found");
    }
}