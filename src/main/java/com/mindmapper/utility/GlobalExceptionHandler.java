package com.mindmapper.utility;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // Handle IllegalArgumentException specifically
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public ResponseEntity<Response> handleIllegalArgumentException(IllegalArgumentException ex) {
        Response response = new Response(ex.getMessage(), "400");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Handle any other unhandled exceptions globally
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<Response> handleGeneralException(Exception ex) {
        Response response = new Response("Something went wrong: " + ex.getMessage(), "500");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
