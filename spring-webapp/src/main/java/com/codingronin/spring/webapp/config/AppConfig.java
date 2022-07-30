package com.codingronin.spring.webapp.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.codingronin.spring.webapp.filter.TransactionFilter;

@Configuration
public class AppConfig {

  @Bean
  public FilterRegistrationBean<TransactionFilter> logFilter() {
    FilterRegistrationBean<TransactionFilter> registrationBean = new FilterRegistrationBean<>();
    registrationBean.setFilter(new TransactionFilter());
    registrationBean.addUrlPatterns("/api/*");
    return registrationBean;
  }

}
