package org.example.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.example.service.AuthenticationService;
import org.example.web.dto.ExceptionResponse;
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
@Tag(name = "Authentication controller")
@RestController
public class AuthenticationController {
  private final AuthenticationService authenticationService;
  private final UserMapper userMapper;
  private final TokenMapper tokenMapper;

  @Operation(
      summary = "Register client account",
      responses = {
          @ApiResponse(responseCode = "201",  content = @Content(
              schema = @Schema(implementation = AuthenticationResponse.class)
          )),
          @ApiResponse(responseCode = "400",
              description = "Another user with such email already exists",
              content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
      })
  @PostMapping("/sign-up")
  public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody UserModifyDto user) {
    var tokenPair = authenticationService.register(userMapper.toEntity(user));
    return ResponseEntity.status(HttpStatus.CREATED).body(tokenMapper.toResponse(tokenPair));
  }

  @Operation(
      summary = "Authenticate user account, and return tokens associated with it",
      responses = {
          @ApiResponse(responseCode = "200", content = @Content(
              schema = @Schema(implementation = AuthenticationResponse.class)
          )),
          @ApiResponse(responseCode = "400", description = "Invalid password",
              content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
          @ApiResponse(responseCode = "404", description = "User with given login does not exist",
              content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
      })
  @PostMapping("/authenticate")
  public ResponseEntity<AuthenticationResponse> authenticate(@Valid @RequestBody
                                                             AuthenticationRequest request) {
    var tokenPair = authenticationService.authenticateUserByCredentials(
        request.getLogin(),
        request.getPassword()
    );
    return ResponseEntity.ok(tokenMapper.toResponse(tokenPair));
  }

  @Operation(
      summary = "Update user`s refresh token",
      security =  @SecurityRequirement(name = "Bearer", scopes = "Refresh"),
      responses = {
          @ApiResponse(responseCode = "200", content = @Content(
              schema = @Schema(implementation = AuthenticationResponse.class)
          )),
          @ApiResponse(responseCode = "400", description = "Invalid password",
              content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
          @ApiResponse(responseCode = "400", description = "Bearer token is not refresh one",
              content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
          @ApiResponse(responseCode = "404", description = "User with given email does not exist",
              content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
      })
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
