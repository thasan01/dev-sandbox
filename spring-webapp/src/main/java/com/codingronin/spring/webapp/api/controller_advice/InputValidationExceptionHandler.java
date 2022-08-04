package com.codingronin.spring.webapp.api.controller_advice;

import org.jboss.logging.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.codingronin.spring.webapp.api.errors.BadInputException;
import com.codingronin.spring.webapp.api.errors.ResourceNotFoundException;
import com.codingronin.spring.webapp.api.model.http.v1.BadInputResponse;

@ControllerAdvice
public class InputValidationExceptionHandler {

  Logger log = Logger.getLogger(InputValidationExceptionHandler.class);

  @ExceptionHandler(BadInputException.class)
  public ResponseEntity<BadInputResponse> handleBadInputExceptions(BadInputException ex) {
    log.error("Encountered exception: ", ex);
    BadInputResponse resp = new BadInputResponse();
    resp.setResponseId(ex.getResponseId());
    resp.setErrors(ex.getFieldErrors());
    resp.setStatusMessage("Encountered invalid input.");
    return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<BadInputResponse> handleBadInputExceptions(ResourceNotFoundException ex) {
    log.error("Encountered exception: ", ex);
    BadInputResponse resp = new BadInputResponse();
    resp.setResponseId(ex.getResponseId());
    resp.setStatusMessage(ex.getMessage());
    return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
  }



}
