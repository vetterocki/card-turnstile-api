package org.example.web.dto.auth;

import lombok.Data;

@Data
public class AuthenticationResponse {
  private String accessToken;
  private String refreshToken;
}
