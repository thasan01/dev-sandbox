package com.codingronin.spring.webapp.config;

import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import com.codingronin.spring.webapp.ui.handler.CustomAccessDeniedHandler;


/**
 * reference:
 * https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter
 *
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {


  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http,
      InMemoryUserDetailsManager inMemUserDetails) throws Exception {

    http.authorizeRequests().antMatchers("/", "/home").permitAll();

    http.authorizeRequests().antMatchers("/debug/auth/default").authenticated().and()
        .httpBasic(withDefaults());

    http.authorizeRequests().antMatchers("/debug/auth/inmem").hasRole("ADMIN").and()
        .userDetailsService(inMemUserDetails).exceptionHandling()
        .accessDeniedHandler(new CustomAccessDeniedHandler());

    http.authorizeRequests().antMatchers("/api/**").authenticated().and().httpBasic(withDefaults());

    // Enable /h2-console endpoint
    http.csrf().ignoringAntMatchers("/h2-console/**").and().headers().frameOptions().sameOrigin();

    return http.build();
  }


  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return web -> web.ignoring().antMatchers("/ignore1", "/ignore2");
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  @Bean
  public InMemoryUserDetailsManager inMemoryUserDetailsManager(PasswordEncoder passwordEncoder) {

    UserDetails user1 = User.builder().username("user1").password("pass1").roles("USER")
        .passwordEncoder(passwordEncoder::encode).build();

    UserDetails user2 = User.builder().username("user2").password("pass2").roles("ADMIN")
        .passwordEncoder(passwordEncoder::encode).build();

    return new InMemoryUserDetailsManager(user1, user2);
  }

}
