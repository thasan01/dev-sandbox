package com.codingronin.spring.webapp.api.controller;

import static com.codingronin.spring.webapp.filter.TransactionFilter.CLIENT_RESPONSE_ID_KEY;
import java.lang.reflect.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import com.codingronin.spring.webapp.api.model.http.v1.ApiBaseResponse;

/**
 * This Class intercepts responses for all Controller classes that implement `RestApiController`
 * interface and modifies the payload before returning it to the client.
 *
 */
@RestControllerAdvice
public class ResponseControllerAdvice implements ResponseBodyAdvice<ApiBaseResponse> {

  static Logger log = LoggerFactory.getLogger(ResponseControllerAdvice.class);

  @Override
  public boolean supports(MethodParameter returnType,
      Class<? extends HttpMessageConverter<?>> converterType) {

    Class<?> clazz = returnType.getContainingClass();
    Method method = returnType.getMethod();
    boolean isSupported = RestApiController.class.isAssignableFrom(clazz);

    log.trace("className: {}, methodName: {}, isSupported: {}", clazz, method, isSupported);
    return isSupported;
  }

  @Override
  public ApiBaseResponse beforeBodyWrite(ApiBaseResponse responsePayload,
      MethodParameter returnType, MediaType selectedContentType,
      Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
      ServerHttpResponse response) {

    String responseId = response.getHeaders().getFirst(CLIENT_RESPONSE_ID_KEY);
    responsePayload.setResponseId(responseId);

    return responsePayload;
  }

}
