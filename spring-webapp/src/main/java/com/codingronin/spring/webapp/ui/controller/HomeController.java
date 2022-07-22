package com.codingronin.spring.webapp.ui.controller;

import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

  @GetMapping
  public String home(Map<String, Object> model) {
    return "index";
  }

}
