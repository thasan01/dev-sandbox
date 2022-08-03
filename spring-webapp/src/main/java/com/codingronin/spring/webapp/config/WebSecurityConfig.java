package com.codingronin.spring.webapp.config;

import static org.springframework.security.config.Customizer.withDefaults;
import javax.sql.DataSource;
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
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import com.codingronin.spring.webapp.ui.handler.AppAccessDeniedHandler;


/**
 * Reference:
 * https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter
 *
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http, //
      AppAccessDeniedHandler accessDeniedHandler, //
      InMemoryUserDetailsManager inMemUserDetails, //
      JdbcUserDetailsManager jdbcUserDetailsManager//
  ) throws Exception {

    http.authorizeRequests().antMatchers("/", "/home").permitAll();

    http.authorizeRequests().antMatchers("/debug/auth/default").authenticated().and()
        .httpBasic(withDefaults());

    http.authorizeRequests().antMatchers("/debug/auth/inmem").hasRole("ADMIN").and()
        .userDetailsService(inMemUserDetails).exceptionHandling()
        .accessDeniedHandler(accessDeniedHandler);

    http.authorizeRequests().antMatchers("/api/**").authenticated().and()
        .userDetailsService(jdbcUserDetailsManager).exceptionHandling()
        .accessDeniedHandler(accessDeniedHandler);

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

  @Bean
  public JdbcUserDetailsManager jdbcUserDetailsManager(DataSource dataSource) {

    String getUsersByUserNameQuery =
        "SELECT u.user_name AS username, ap.password AS password, ap.enabled  as enabled " //
            + "FROM app_user u " //
            + "INNER JOIN app_user_auth_profiles uap ON u.id = uap.user_id " //
            + "INNER JOIN app_auth_profile ap ON uap.auth_profile_id = ap.id " //
            + "WHERE u.user_name = ?";

    String getAuthoritiesByUserNameQuery = "WITH user_details AS ( " //
        + "  SELECT u.user_name AS username, r.name AS role, p.name AS permission " //
        + "       FROM app_user u " //
        + "       INNER JOIN app_user_roles ur ON u.id = ur.user_id "//
        + "       INNER JOIN app_role r ON r.id = ur.role_id "//
        + "       INNER JOIN app_role_permissions rp ON r.id = rp.role_id "//
        + "       INNER JOIN app_permission p ON p.id = rp.permission_id " //
        + "   WHERE u.user_name = ? "//
        + " ) "//
        + " SELECT ud.username AS username, CONCAT('ROLE_', ud.role) AS authority FROM user_details ud "//
        + " UNION "//
        + " SELECT ud.username AS username, ud.permission AS authority FROM user_details ud";

    JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);
    manager.setUsersByUsernameQuery(getUsersByUserNameQuery);
    manager.setAuthoritiesByUsernameQuery(getAuthoritiesByUserNameQuery);
    return manager;
  }

}
