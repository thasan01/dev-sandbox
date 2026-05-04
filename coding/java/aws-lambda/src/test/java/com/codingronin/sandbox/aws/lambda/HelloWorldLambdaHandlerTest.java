package com.codingronin.sandbox.aws.lambda;

import static org.junit.Assert.assertTrue;
import java.util.HashMap;
import org.junit.Test;
import com.amazonaws.services.lambda.runtime.Context;

public class HelloWorldLambdaHandlerTest {

  @Test
  public void shouldAnswerWithTrue() {

    HashMap<String, String> event = new HashMap<>();
    Context context = new TestContext();
    HelloWorldLambdaHandler handler = new HelloWorldLambdaHandler();
    String result = handler.handleRequest(event, context);
    assertTrue(result.contains("200 OK"));
  }
}
