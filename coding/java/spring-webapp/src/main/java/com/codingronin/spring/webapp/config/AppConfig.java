package com.codingronin.spring.webapp.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.codingronin.spring.webapp.filter.TransactionFilter;

@Configuration
public class AppConfig {

  @Bean(name = "transactionFilterRegistrationBean")
  public FilterRegistrationBean<TransactionFilter> filterRegistrationBean(
      TransactionFilter transactionFilter) {
    FilterRegistrationBean<TransactionFilter> registrationBean = new FilterRegistrationBean<>();
    registrationBean.setName("transactionFilter");
    registrationBean.setFilter(transactionFilter);
    registrationBean.addUrlPatterns("/*");
    registrationBean.setOrder(0);
    return registrationBean;
  }

}
