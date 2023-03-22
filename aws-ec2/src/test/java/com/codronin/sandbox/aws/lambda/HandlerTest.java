package com.codronin.sandbox.aws.lambda;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class HandlerTest {
  private static final Logger log = LoggerFactory.getLogger(HandlerTest.class);

  @Test
  void invokeTest() {
    log.info("Invoke TEST");
    assertTrue(true);
  }

}
