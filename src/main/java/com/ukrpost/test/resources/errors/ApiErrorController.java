package com.ukrpost.test.resources.errors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Controller
public class ApiErrorController extends ResponseEntityExceptionHandler {
 
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ResponseError> handleError(ResponseStatusException ex, WebRequest request) {
    	ResponseError err = new ResponseError();
    	err.setCode(ex.getStatus().value());
    	err.setMessage(ex.getReason());
    	
    	return new ResponseEntity<>(err, ex.getStatus());
    }
 
}
