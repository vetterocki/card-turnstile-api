package org.example.service.impl;


import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.example.model.user.Token.TokenType.ACCESS;
import static org.example.model.user.Token.TokenType.REFRESH;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.example.data.TokenRepository;
import org.example.model.user.Token;
import org.example.model.user.Token.TokenType;
import org.example.model.user.User;
import org.example.security.JwtService;
import org.example.service.TokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor(onConstructor_ = {@Lazy})
@Transactional(readOnly = true)
@Service
public class TokenServiceImpl implements TokenService {
  @Value("${jwt.access.expiration.seconds}")
  private Integer accessTokenExpirationInSeconds;

  @Value("${jwt.refresh.expiration.days}")
  private Integer refreshTokenExpirationInDays;

  private final TokenRepository tokenRepository;
  private final JwtService jwtService;
  private final TokenServiceImpl self;

  @Override
  @Transactional(propagation = Propagation.SUPPORTS)
  public Pair<Token, Token> create(User user) {
    Token accessToken = self.generateToken(user, accessTokenExpirationInSeconds, SECONDS, ACCESS);
    Token refreshToken = self.generateToken(user, refreshTokenExpirationInDays, DAYS, REFRESH);
    return Pair.of(accessToken, refreshToken);
  }

  @Transactional
  public Token generateToken(User user, int expiration, ChronoUnit unit, TokenType tokenType) {
    String generatedToken = jwtService.generateJwt(user, expiration, unit);
    Token token = new Token();
    token.setTokenType(tokenType);
    token.setUser(user);
    token.setValue(generatedToken);
    return tokenRepository.save(token);
  }

  public Optional<List<Token>> findAllValidByUserId(Long userId) {
    return tokenRepository.findAllValidTokensByUserId(userId);
  }

  public Optional<Token> findByTokenValue(String token) {
    return tokenRepository.findByValue(token);
  }

  @Transactional(propagation = Propagation.MANDATORY)
  public void invalidateAllUserTokens(Long userId) {
    findAllValidByUserId(userId).ifPresent(tokens ->
        tokens.forEach(token -> {
          token.setExpired(true);
          token.setRevoked(true);
        })
    );
  }
}
