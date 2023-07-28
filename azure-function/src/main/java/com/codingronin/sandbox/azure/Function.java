package com.codingronin.sandbox.azure;

import java.util.Optional;
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

  @FunctionName("Ping")
  public HttpResponseMessage ping(
      @HttpTrigger(name = "req", methods = {HttpMethod.GET, HttpMethod.POST},
          authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request,
      final ExecutionContext context) {
    context.getLogger().info("Java HTTP trigger processed a request.");

    // Parse query parameter
    final String query = request.getQueryParameters().get("name");
    final String name = request.getBody().orElse(query);

    if (name == null) {
      return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
          .body("request is missing name in url").build();
    } else {
      return request.createResponseBuilder(HttpStatus.OK).body("Service is up." + name).build();
    }
  }
}
