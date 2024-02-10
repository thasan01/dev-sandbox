package com.codingronin.spring.webapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import com.codingronin.spring.webapp.scim.dto.v2.ScimUserMapper;

@Configuration
public class ScimApiConfig {

  @Bean
  ScimUserMapper scimUserMapper() {
    return new ScimUserMapper();
  }

  @Bean
  RestTemplate localClient() {
    return new RestTemplate();
  }

}
