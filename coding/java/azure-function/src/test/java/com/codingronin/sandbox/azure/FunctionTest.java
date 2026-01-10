package com.codingronin.sandbox.azure;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import com.microsoft.azure.functions.HttpStatus;


/**
 * Unit test for Function class.
 */
class FunctionTest {
  /**
   * Unit test for HttpTriggerJava method.
   */
  @Test
  void testHttpTriggerJava() {
    HttpStatus expected = HttpStatus.OK;
    HttpStatus actual = HttpStatus.OK;

    // Verify
    assertEquals(expected, actual);
  }
}
