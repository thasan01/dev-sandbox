package com.codingronin.spring.webapp.ui.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class AppErrorController implements ErrorController {

  @GetMapping(value = "/error", produces = MediaType.TEXT_PLAIN_VALUE)
  public String handleError(HttpServletRequest request) {
    Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
    Exception exception = (Exception) request.getAttribute("javax.servlet.error.exception");
    return String.format(
        "<html><body><h2>Error Page</h2><div>Status code: <b>%s</b></div>"
            + "<div>Exception Message: <b>%s</b></div><body></html>",
        statusCode, exception == null ? "N/A" : exception.getMessage());
  }

  @GetMapping("/error/accessDenied")
  public ModelAndView errorAccessDenied() {
    return new ModelAndView("index");
  }


}
