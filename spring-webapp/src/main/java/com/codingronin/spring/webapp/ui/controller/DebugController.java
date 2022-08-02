package com.codingronin.spring.webapp.ui.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DebugController {

  @GetMapping(value = "/debug/auth/default", produces = MediaType.TEXT_PLAIN_VALUE)
  @ResponseBody
  public String defaultAuth() {
    return "This is a debug page. You can only access it using the Basic Auth: username=user, password=the randomly generated password by Spring in the console.";
  }

  @GetMapping(value = "/debug/auth/inmem", produces = MediaType.TEXT_PLAIN_VALUE)
  @ResponseBody
  public String inMemoryAuth() {
    return "This is a debug page. You can only access it using the Basic Auth: with the credentials hardcoded in the source code.";
  }

}
