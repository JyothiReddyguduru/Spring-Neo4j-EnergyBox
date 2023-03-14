package com.energybox.backendcodingchallenge.custom.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

import com.energybox.backendcodingchallenge.custom.models.ErrorMessage;

@RestControllerAdvice
public class BackendExceptionHandler {

    Logger logger = LoggerFactory.getLogger(BackendExceptionHandler.class);

    @ExceptionHandler(EntityNotFoundException.class)
    ResponseEntity<ErrorMessage> entityNotFound(EntityNotFoundException ex, WebRequest request) {
        logger.debug("handling exception::" + ex);
        ErrorMessage message = new ErrorMessage(
        HttpStatus.NOT_FOUND.value(),
        new Date(),
        ex.getMessage(),
        request.getDescription(false));
        return new ResponseEntity<ErrorMessage>(message, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateEntityFoundException.class)
    ResponseEntity<ErrorMessage> duplicateEntityFound(DuplicateEntityFoundException ex, WebRequest request) {
        logger.debug("handling exception::" + ex);
        ErrorMessage message = new ErrorMessage(
        HttpStatus.NOT_ACCEPTABLE.value(),
        new Date(),
        ex.getMessage(),
        request.getDescription(false));
        return new ResponseEntity<ErrorMessage>(message, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage globalExceptionHandler(Exception ex, WebRequest request) {
        ErrorMessage message = new ErrorMessage(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            new Date(),
            ex.getMessage(),
            request.getDescription(false));
        
        return message;
  }

}
