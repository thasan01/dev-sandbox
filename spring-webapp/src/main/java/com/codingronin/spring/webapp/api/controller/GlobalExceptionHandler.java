package com.codingronin.spring.webapp.api.controller;

import static com.codingronin.spring.webapp.filter.TransactionFilter.CLIENT_RESPONSE_ID_KEY;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import com.codingronin.spring.webapp.api.model.http.v1.BadInputResponse;
import com.google.gson.Gson;

@ControllerAdvice

public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
  @Autowired
  Gson gson;

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      final MethodArgumentNotValidException ex, final HttpHeaders headers, final HttpStatus status,
      final WebRequest request) {

    BadInputResponse resp = new BadInputResponse();
    resp.setResponseId(headers.getFirst(CLIENT_RESPONSE_ID_KEY));
    resp.setStatusMessage("Request failed input validation.");
    resp.setErrors(ex.getBindingResult().getFieldErrors());
    return ResponseEntity.badRequest().body(resp);
  }

  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
      HttpHeaders headers, HttpStatus status, WebRequest request) {

    BadInputResponse resp = new BadInputResponse();
    resp.setResponseId(headers.getFirst(CLIENT_RESPONSE_ID_KEY));
    resp.setStatusMessage("Request payload is invalid.");
    return ResponseEntity.badRequest().body(resp);
  }


  @Override
  protected ResponseEntity<Object> handleMissingServletRequestParameter(
      MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status,
      WebRequest request) {
    BadInputResponse resp = new BadInputResponse();
    resp.setResponseId(headers.getFirst(CLIENT_RESPONSE_ID_KEY));
    resp.setStatusMessage("Request is missing required parameter " + ex.getParameterName());
    return ResponseEntity.badRequest().body(resp);
  }

}
