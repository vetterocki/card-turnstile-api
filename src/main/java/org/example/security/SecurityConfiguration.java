package org.example.security;

import static org.example.model.user.Permission.ADMIN_CREATE;
import static org.example.model.user.Permission.ADMIN_DELETE;
import static org.example.model.user.Permission.ADMIN_READ;
import static org.example.model.user.Permission.ADMIN_UPDATE;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.PATCH;
import static org.springframework.http.HttpMethod.POST;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity

@Configuration
public class SecurityConfiguration {
  private static final String[] WHITE_LIST_URL = {
      "/v3/api-docs",
      "/v3/api-docs/**",
      "/swagger-resources",
      "/swagger-resources/**",
      "/configuration/ui",
      "/configuration/security",
      "/swagger-ui/**",
      "/webjars/**",
      "/swagger-ui.html",
      "/error",
      "/auth/**"
  };
  private static final String TURNSTILES_URL = "/turnstiles";
  private static final String TRAVEL_CARDS_URL = "/travel-cards";

  private final JwtAuthenticationFilter jwtAuthenticationFilter;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(requests -> requests
            .requestMatchers(WHITE_LIST_URL).permitAll()
            .requestMatchers(POST, TRAVEL_CARDS_URL).hasAuthority(ADMIN_CREATE.name())
            .requestMatchers(GET, TRAVEL_CARDS_URL + "/**").hasAuthority(ADMIN_READ.name())
            .requestMatchers(PATCH, TRAVEL_CARDS_URL + "/**").hasAuthority(ADMIN_UPDATE.name())
            .requestMatchers(DELETE, TRAVEL_CARDS_URL + "/**").hasAuthority(ADMIN_DELETE.name())
            .requestMatchers(POST, TURNSTILES_URL).hasAuthority(ADMIN_CREATE.name())
            .requestMatchers(GET, TURNSTILES_URL + "/**").hasAuthority(ADMIN_READ.name())
            .requestMatchers(PATCH, TURNSTILES_URL + "/**").hasAuthority(ADMIN_UPDATE.name())
            .requestMatchers(DELETE, TURNSTILES_URL + "/**").hasAuthority(ADMIN_DELETE.name())
            .anyRequest().authenticated())
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .exceptionHandling(
            exceptionHandler -> exceptionHandler.authenticationEntryPoint(new HttpStatusEntryPoint(
                HttpStatus.UNAUTHORIZED))
        );
    return http.build();
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
