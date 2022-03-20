package com.example.chartographer.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.awt.image.RasterFormatException;

@ControllerAdvice
public class ChartographerControllerExceptionHandler
        extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { MultipartException.class })
    protected ResponseEntity<Object> handleFileException(RuntimeException ex, WebRequest request) {
        String bodyOfResponse = "Not found file for upload";
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }
    @ExceptionHandler(value = { ConstraintViolationException.class })
    public ResponseEntity<String> handleMaxSizeException(ConstraintViolationException exc) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Requested fragment too large!");
    }

    @ExceptionHandler(value = {ArrayIndexOutOfBoundsException.class})
    public ResponseEntity<String> handleIndexOutOfBoundsException(ArrayIndexOutOfBoundsException e) {
        return new ResponseEntity<>("Coordinate out of bounds!", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {RasterFormatException.class})
    public ResponseEntity<String> handleInternalServerException(RasterFormatException e) {
        return new ResponseEntity<>("Coordinate out of bounds!", HttpStatus.BAD_REQUEST);
    }
}
