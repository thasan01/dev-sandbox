package com.codingronin.spring.webapp.ui.controller;

import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

  @GetMapping("/")
  public ModelAndView home(Map<String, Object> model) {
    return new ModelAndView("index");
  }

}
