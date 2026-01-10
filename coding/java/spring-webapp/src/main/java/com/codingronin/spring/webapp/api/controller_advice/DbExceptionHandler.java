package com.codingronin.spring.webapp.api.controller_advice;

import org.jboss.logging.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.codingronin.spring.webapp.api.model.http.v1.BadInputResponse;

@ControllerAdvice
public class DbExceptionHandler {

  Logger log = Logger.getLogger(DbExceptionHandler.class);

  @ExceptionHandler(DataAccessException.class)
  public ResponseEntity<BadInputResponse> handleNullPointerExceptions(DataAccessException ex) {
    log.error("Encountered exception: ", ex);
    BadInputResponse resp = new BadInputResponse();
    resp.setStatusMessage("Encountered database error.");
    return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
  }
}
