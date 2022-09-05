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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.NestedServletException;
import com.codingronin.spring.webapp.ui.handler.AppAccessDeniedHandler;


@Component
public class TransactionFilter extends OncePerRequestFilter {

  static Logger log = LoggerFactory.getLogger(TransactionFilter.class);
  public static final String CLIENT_REQUEST_ID_KEY = "x-request-id";
  public static final String CLIENT_RESPONSE_ID_KEY = "x-response-id";

  @Autowired
  AppAccessDeniedHandler accessDeniedHandler;

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
    } catch (NestedServletException ex) {
      Throwable cause = ex.getCause();
      if (cause instanceof AccessDeniedException)
        throw new AccessDeniedException(cause.getMessage(), cause);
      else
        throw ex;
    } catch (AccessDeniedException ex) {
      accessDeniedHandler.handle(request, response, ex);
    } catch (Exception ex) {
      log.error("Encountered error for responseId {}", responseId, ex);
    }

    log.info("Exiting filter for request. responseId: {}", responseId);
  }


}
