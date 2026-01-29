package com.projects.employee_management.controllers.exceptions;

import com.projects.employee_management.services.exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class ControllerExceptionHandler {


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> entityNotFound (ResourceNotFoundException e, HttpServletRequest request){

       StandardError err = new StandardError();

        err.setTimestamp(Instant.now());
        err.setStatus(HttpStatus.NOT_FOUND.value());
        err.setPath(request.getRequestURI());
        err.setError("Recurso não encontrado");
        err.setMessage(e.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);

    }

}
