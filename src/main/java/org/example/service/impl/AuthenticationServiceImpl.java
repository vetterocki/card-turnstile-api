package org.example.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.exception.InvalidPasswordException;
import org.example.exception.InvalidTokenTypeException;
import org.example.model.user.Token;
import org.example.model.user.User;
import org.example.service.AuthenticationService;
import org.example.service.TokenService;
import org.example.service.UserService;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class AuthenticationServiceImpl implements AuthenticationService {
  private final UserService userService;
  private final PasswordEncoder passwordEncoder;
  private final TokenService tokenService;

  @Override
  @Transactional(propagation = Propagation.SUPPORTS)
  public Pair<Token, Token> register(User user) {
    User created = userService.create(user);
    return tokenService.create(created);
  }

  @Override
  @Transactional
  public Pair<Token, Token> authenticateUserByCredentials(String email, String password) {
    return userService.findByEmail(email)
        .map(user -> {
          if (passwordEncoder.matches(password, user.getPassword())) {
            tokenService.invalidateAllUserTokens(user.getId());
            return tokenService.create(user);
          }
          throw new InvalidPasswordException("Invalid password");
        })
        .orElseThrow(() -> new UsernameNotFoundException(
            "User with email %s does not exist".formatted(email)
        ));
  }

  @Override
  @Transactional
  public Pair<Token, Token> refreshToken(HttpServletRequest request, String email) {
    String token = request.getHeader(HttpHeaders.AUTHORIZATION).substring(7);
    return userService.findByEmail(email)
        .map(user -> tokenService.findByTokenValue(token)
            .map(tokenInDb -> {
              if (tokenInDb.getTokenType().equals(Token.TokenType.REFRESH)) {
                return tokenService.create(user);
              }
              throw new InvalidTokenTypeException("Given token is not REFRESH-typed");
            })
            .orElseThrow(() -> new AccessDeniedException("Access denied")))
        .orElseThrow(() -> new UsernameNotFoundException(
            "User with email: %s does not exist".formatted(email)
        ));
  }
}
