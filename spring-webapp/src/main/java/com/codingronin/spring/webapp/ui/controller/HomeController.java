package com.codingronin.spring.webapp.ui.controller;

import java.util.Map;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

  @GetMapping("/")
  public ModelAndView home(Map<String, Object> model) {
    return new ModelAndView("index");
  }


  @GetMapping("/access-denied")
  public ModelAndView accessDenied(Map<String, Object> model) {
    return new ModelAndView("error-access-denied");
  }

  @GetMapping("/login")
  public ModelAndView login(Map<String, Object> model) {
    return new ModelAndView("login");
  }

  @GetMapping("/user")
  public ModelAndView userHome(Map<String, Object> model) {
    Authentication authUser = SecurityContextHolder.getContext().getAuthentication();
    model.put("authUser", authUser);
    return new ModelAndView("user");
  }


}
