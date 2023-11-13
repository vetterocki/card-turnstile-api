package org.example.security;

import static org.example.model.user.Token.TokenType.ACCESS;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import lombok.RequiredArgsConstructor;
import org.example.data.TokenRepository;
import org.example.model.user.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

@RequiredArgsConstructor
@Component
public class JwtService {
  @Value("${jwt.secret}")
  private String secret;

  @Value("${spring.application.name}")
  private String issuer;

  private final TokenRepository tokenRepository;


  public String generateJwt(User user, int expirationTime, ChronoUnit expirationTimeUnit) {
    return JWT.create()
        .withSubject(user.getId().toString())
        .withIssuer(issuer)
        .withIssuedAt(Instant.now())
        .withExpiresAt(Instant.now().plus(expirationTime, expirationTimeUnit))
        .withClaim("email", user.getEmail())
        .sign(Algorithm.HMAC256(secret));
  }

  public JwtVerifierBuilder verifier(String bearerToken) {
    return new JwtVerifierBuilder(bearerToken);
  }


  public class JwtVerifierBuilder {
    private Optional<DecodedJWT> bearerToken;

    public JwtVerifierBuilder(String bearerToken) {
      this.bearerToken = verifyDecodedJwt(bearerToken);
    }


    public JwtVerifierBuilder allowsRefreshTokenUrls(String servletPath, String... patterns) {
      if (!isRefreshAllowedForCurrentUrl(servletPath, patterns)) {
        this.bearerToken = filterByAccessTokenType(this.bearerToken);
      }
      return this;
    }

    @SafeVarargs
    public final JwtVerifierBuilder validationFilters(Predicate<DecodedJWT>... predicates) {
      this.bearerToken = Arrays.stream(predicates)
          .reduce(bearerToken, (Optional::filter),
              (opt1, opt2) -> opt1.flatMap(t1 -> opt2.map(t2 -> t1)));
      return this;
    }

    public void ifTokenValid(Consumer<DecodedJWT> action) {
      this.bearerToken.ifPresent(action);
    }

    private Optional<DecodedJWT> verifyDecodedJwt(String token) {
      return Optional.of(JWT.require(Algorithm.HMAC256(secret))
              .withIssuer(issuer)
              .build()
              .verify(token))
          .filter(decodedJWT -> tokenRepository.existsByValue(decodedJWT.getToken()))
          .filter(decodedJWT -> tokenRepository.findByValue(decodedJWT.getToken())
              .filter(tokenInDb -> !tokenInDb.isRevoked() && !tokenInDb.isExpired()).isPresent());

    }

    private boolean isRefreshAllowedForCurrentUrl(String servletPath, String... patterns) {
      AntPathMatcher pathMatcher = new AntPathMatcher();
      return Arrays.stream(patterns).anyMatch(pattern -> pathMatcher.match(pattern, servletPath));
    }

    private Optional<DecodedJWT> filterByAccessTokenType(Optional<DecodedJWT> token) {
      return token.filter(decodedJWT -> tokenRepository.findByValue(decodedJWT.getToken())
          .filter(t -> t.getTokenType().equals(ACCESS))
          .isPresent());
    }

  }

}