package com.codingronin.sandbox.azure;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;

/**
 * Azure Functions with HTTP Trigger.
 */
public class Function {

  static final String ERROR_RESP_TEXT = "Encountered an error while processing the request. ";

  @FunctionName("Ping")
  public HttpResponseMessage ping(//
      @HttpTrigger(name = "req", methods = {HttpMethod.GET, HttpMethod.POST},
          authLevel = AuthorizationLevel.FUNCTION) //
      HttpRequestMessage<Optional<String>> request, //
      final ExecutionContext context//
  ) //
  {
    Logger log = context.getLogger();
    log.info("Java HTTP trigger processed a request.");

    try {
      // Parse query parameter
      final String query = request.getQueryParameters().get("name");
      final String name = request.getBody().orElse(query);

      if (name == null)
        throw new IllegalArgumentException("Request is missing name in url");

      return request.createResponseBuilder(HttpStatus.OK).body("Service is up." + name).build();

    } catch (Exception ex) {
      log.log(Level.SEVERE, ERROR_RESP_TEXT, ex);
      return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
          .body(ERROR_RESP_TEXT + ex.getMessage()).build();
    }
  }
}
