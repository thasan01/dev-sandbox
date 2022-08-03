package com.codingronin.spring.webapp.ui.handler;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import com.codingronin.spring.webapp.api.model.http.v1.ApiBaseResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Component
public class AppAccessDeniedHandler implements AccessDeniedHandler {

  static Logger log = LoggerFactory.getLogger(AppAccessDeniedHandler.class);

  Gson gson = new GsonBuilder().create();

  @Value("${server.servlet.context-path}/api")
  String contextPath;

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response,
      AccessDeniedException exception) throws IOException, ServletException {

    log.debug("requestURI: {}", request.getRequestURI());

    if (request.getRequestURI().startsWith(contextPath))
      handleApi(response, exception);
    else
      response.sendRedirect(request.getContextPath() + "/access-denied");
  }


  void handleApi(HttpServletResponse response, AccessDeniedException exception) throws IOException {
    ApiBaseResponse resp = new ApiBaseResponse();
    resp.setStatusMessage(exception.getMessage());
    String respJson = gson.toJson(resp);

    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setStatus(HttpStatus.FORBIDDEN.value());
    response.getOutputStream().write(respJson.getBytes());
  }



}
