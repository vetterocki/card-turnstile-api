package org.example.web.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.model.user.User;
import org.example.service.UserService;
import org.example.web.dto.ExceptionResponse;
import org.example.web.dto.card.DefaultTravelCardViewDto;
import org.example.web.dto.card.LoyaltyTravelCardViewDto;
import org.example.web.dto.card.TravelCardViewDto;
import org.example.web.dto.user.UserModifyDto;
import org.example.web.dto.user.UserViewDto;
import org.example.web.mapper.UserMapper;
import org.example.web.mapper.card.TravelCardMapperDecorator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/users")
@Tag(name = "User controller")
@SecurityRequirement(name = "Bearer", scopes = "Access")
@RestController
public class UserController {
  private final UserService userService;
  private final TravelCardMapperDecorator travelCardMapper;
  private final UserMapper userMapper;

  @PreAuthorize("hasAuthority('ADMIN_READ')")
  @GetMapping("/{id}")
  @Operation(summary = "Get user by id", responses = {
      @ApiResponse(responseCode = "200"),
      @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
  })
  public ResponseEntity<UserViewDto> findById(@PathVariable Long id) {
    return ResponseEntity.of(userService.findById(id).map(userMapper::toDto));
  }

  @GetMapping("/self")
  @Operation(summary = "Get authorized user by access token", responses = {
      @ApiResponse(responseCode = "200"),
      @ApiResponse(responseCode = "404", content = @Content)
  })
  public ResponseEntity<UserViewDto> findSelf(Principal principal) {
    return ResponseEntity.of(userService.findByEmail(principal.getName()).map(userMapper::toDto));
  }

  @GetMapping("/self/travel-cards")
  @Operation(summary = "Get user`s owned travel cards", responses = {
      @ApiResponse(responseCode = "200", content = @Content(
          schema = @Schema(
              anyOf = {DefaultTravelCardViewDto.class, LoyaltyTravelCardViewDto.class},
              description = "Depending on travel card type, corresponding DTO returns"
          ))),
      @ApiResponse(responseCode = "404", content = @Content)
  })
  public ResponseEntity<List<TravelCardViewDto>> findAllTravelCardsByUser(Principal principal) {
    return ResponseEntity.of(userService.findByEmail(principal.getName())
        .map(user -> userService.findAllTravelCardsByUser(user).stream()
            .map(travelCardMapper::toDto)
            .toList()));
  }

  @PreAuthorize("hasAuthority('ADMIN_CREATE')")
  @PostMapping
  @Operation(
      summary = "Create user account",
      responses = {
          @ApiResponse(responseCode = "201", content = @Content),
          @ApiResponse(responseCode = "400",
              description = "Another user with such email already exists",
              content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
      })
  public ResponseEntity<UserViewDto> create(@RequestBody UserModifyDto userModifyDto) {
    User created = userService.create(userMapper.toEntity(userModifyDto));
    return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.toDto(created));
  }

  @PreAuthorize("hasAuthority('ADMIN_UPDATE')")
  @PatchMapping("/{id}")
  @Operation(summary = "Update user account", responses = {
      @ApiResponse(responseCode = "200", content = @Content),
      @ApiResponse(responseCode = "404", description = "User not found by id"),
      @ApiResponse(responseCode = "400",
          description = "Another user with such email already exists",
          content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
  })
  public ResponseEntity<UserViewDto> partialUpdate(@PathVariable Long id,
                                                   @Valid @RequestBody UserModifyDto modifyDto) {
    return ResponseEntity.of(userService.findById(id)
        .map(turnstile -> userMapper.partialUpdate(modifyDto, turnstile))
        .map(userService::update)
        .map(userMapper::toDto));
  }

  @PreAuthorize("hasAuthority('ADMIN_DELETE')")
  @DeleteMapping("/{id}")
  @Operation(summary = "Delete user by id", responses = {
      @ApiResponse(responseCode = "204")
  })
  public ResponseEntity<Void> deleteById(@PathVariable Long id) {
    userService.deleteById(id);
    return ResponseEntity.noContent().build();
  }
}
