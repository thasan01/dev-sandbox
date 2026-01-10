package com.codingronin.sandbox.azure.javasandboxazurewebapp;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

  @GetMapping("/")
  public String greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
    return "hello world";
  }

  @GetMapping("Admin")
  @ResponseBody
  @PreAuthorize("hasAuthority('APPROLE_Admin')")
  public String admin(Authentication authentication) {
    return "This is the Admin page. You are logged in as " + authentication.getName() + ", "
        + authentication.getAuthorities();
  }

}
