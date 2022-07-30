package com.codingronin.spring.webapp.filter;

import java.io.IOException;
import java.util.UUID;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;
import com.google.gson.Gson;

public class TransactionFilter extends OncePerRequestFilter {

  static Logger log = LoggerFactory.getLogger(TransactionFilter.class);
  public static final String CLIENT_REQUEST_ID_KEY = "x-request-id";
  public static final String CLIENT_RESPONSE_ID_KEY = "x-response-id";

  @Autowired
  Gson gson;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    String requestId = request.getHeader(CLIENT_REQUEST_ID_KEY);
    String responseId = UUID.randomUUID().toString();

    request.setAttribute(CLIENT_REQUEST_ID_KEY, requestId);
    request.setAttribute(CLIENT_RESPONSE_ID_KEY, responseId);

    response.addHeader(CLIENT_REQUEST_ID_KEY, requestId);
    response.addHeader(CLIENT_RESPONSE_ID_KEY, responseId);
    String requestUri = request.getRequestURI();

    log.info("Entering filter for request. responseId: {}, requestUrl: {}", responseId, requestUri);
    try {
      filterChain.doFilter(request, response);
    } catch (Exception ex) {
      log.error("Encountered error for responseId {}", responseId, ex);
    }

    log.info("Exiting filter for request. responseId: {}", responseId);
  }


}
