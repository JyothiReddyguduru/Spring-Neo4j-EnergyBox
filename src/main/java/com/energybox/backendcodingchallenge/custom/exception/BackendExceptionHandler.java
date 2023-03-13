package com.energybox.backendcodingchallenge.custom.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BackendExceptionHandler {

    Logger logger = LoggerFactory.getLogger(BackendExceptionHandler.class);

    @ExceptionHandler(EntityNotFoundException.class)
    ResponseEntity entityNotFound(EntityNotFoundException ex) {
        logger.debug("handling exception::" + ex);
        return ResponseEntity.notFound().build();
    }

}
