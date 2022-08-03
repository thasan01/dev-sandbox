package com.codingronin.spring.webapp.api.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import com.codingronin.spring.webapp.api.model.http.v1.ApiBaseResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ApiAccessDeniedHandler implements AccessDeniedHandler {

  Gson gson = new GsonBuilder().create();

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response,
      AccessDeniedException exception) throws IOException, ServletException {

    ApiBaseResponse resp = new ApiBaseResponse();
    resp.setStatusMessage(exception.getMessage());
    String respJson = gson.toJson(resp);

    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setStatus(HttpStatus.FORBIDDEN.value());
    response.getOutputStream().write(respJson.getBytes());
  }
}
