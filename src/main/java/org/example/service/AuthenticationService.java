package org.example.service;

import jakarta.servlet.http.HttpServletRequest;
import org.example.model.user.Token;
import org.example.model.user.User;
import org.springframework.data.util.Pair;

public interface AuthenticationService {
  Pair<Token, Token> register(User user);

  Pair<Token, Token> authenticateUserByCredentials(String email, String password);

  Pair<Token, Token> refreshToken(HttpServletRequest request, String email);
}
