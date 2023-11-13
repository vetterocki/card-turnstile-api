package org.example.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.example.service.AuthenticationService;
import org.example.web.dto.auth.AuthenticationRequest;
import org.example.web.dto.auth.AuthenticationResponse;
import org.example.web.dto.user.UserModifyDto;
import org.example.web.mapper.TokenMapper;
import org.example.web.mapper.UserMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthenticationController {
  private final AuthenticationService authenticationService;
  private final UserMapper userMapper;
  private final TokenMapper tokenMapper;

  @PostMapping("/sign-up")
  public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody UserModifyDto user) {
    var tokenPair = authenticationService.register(userMapper.toEntity(user));
    return ResponseEntity.status(HttpStatus.CREATED).body(tokenMapper.toResponse(tokenPair));
  }

  @PostMapping("/authenticate")
  public ResponseEntity<AuthenticationResponse> authenticate(@Valid @RequestBody
                                                             AuthenticationRequest request) {
    var tokenPair = authenticationService.authenticateUserByCredentials(
        request.getLogin(),
        request.getPassword()
    );
    return ResponseEntity.ok(tokenMapper.toResponse(tokenPair));
  }

  @PostMapping("/refresh")
  public ResponseEntity<AuthenticationResponse> refreshToken(HttpServletRequest request,
                                                             Principal principal) {
    return Optional.ofNullable(principal)
        .map(Principal::getName)
        .map(email -> authenticationService.refreshToken(request, email))
        .map(tokenMapper::toResponse)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
  }
}
