package org.example.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  private final JwtService jwtService;
  private final UserDetailsService userDetailsService;
  private final HandlerExceptionResolver handlerExceptionResolver;
  private static final String[] REFRESH_TOKEN_URLS = {"/auth/refresh"};

  @Override
  protected void doFilterInternal(@NonNull HttpServletRequest request,
                                  @NonNull HttpServletResponse response,
                                  @NonNull FilterChain filterChain)
      throws ServletException, IOException {
    var authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (!this.isHeaderValid(authHeader)) {
      filterChain.doFilter(request, response);
      return;
    }

    if (SecurityContextHolder.getContext().getAuthentication() == null) {
      try {
        jwtService.verifier(authHeader.substring(7))
            .allowsRefreshTokenUrls(request.getServletPath(), REFRESH_TOKEN_URLS)
            .validationFilters(
                decodedJWT -> decodedJWT.getExpiresAtAsInstant().isAfter(Instant.now()))
            .ifTokenValid(token -> {
              var email = token.getClaim("email").asString();
              var userDetails = userDetailsService.loadUserByUsername(email);
              var authToken = new UsernamePasswordAuthenticationToken(
                  userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities()
              );
              SecurityContextHolder.getContext().setAuthentication(authToken);
            });
      } catch (RuntimeException exception) {
        handlerExceptionResolver.resolveException(request, response, null, exception);
        log.error("custom" + exception.getMessage());
      }
      filterChain.doFilter(request, response);
    }
  }

  private boolean isHeaderValid(String authHeader) {
    return authHeader != null && authHeader.startsWith("Bearer ");
  }
}
