package org.example.web.controller;


import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.model.user.User;
import org.example.service.UserService;
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
@RestController
public class UserController {
  private final UserService userService;
  private final TravelCardMapperDecorator travelCardMapper;
  private final UserMapper userMapper;

  @PreAuthorize("hasAuthority('ADMIN_READ')")
  @GetMapping("/{id}")
  public ResponseEntity<UserViewDto> findById(@PathVariable Long id) {
    return ResponseEntity.of(userService.findById(id).map(userMapper::toDto));
  }

  @GetMapping("/self")
  public ResponseEntity<UserViewDto> findSelf(Principal principal) {
    return ResponseEntity.of(userService.findByEmail(principal.getName()).map(userMapper::toDto));
  }

  @GetMapping("/self/travel-cards")
  public ResponseEntity<List<TravelCardViewDto>> findAllTravelCardsByUser(Principal principal) {
    return ResponseEntity.of(userService.findByEmail(principal.getName())
        .map(user -> userService.findAllTravelCardsByUser(user).stream()
            .map(travelCardMapper::toDto)
            .toList()));
  }

  @PreAuthorize("hasAuthority('ADMIN_CREATE')")
  @PostMapping
  public ResponseEntity<UserViewDto> create(@RequestBody UserModifyDto userModifyDto) {
    User created = userService.create(userMapper.toEntity(userModifyDto));
    return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.toDto(created));
  }

  @PreAuthorize("hasAuthority('ADMIN_UPDATE')")
  @PatchMapping("/{id}")
  public ResponseEntity<UserViewDto> partialUpdate(@PathVariable Long id,
                                                   @Valid @RequestBody UserModifyDto modifyDto) {
    return ResponseEntity.of(userService.findById(id)
        .map(turnstile -> userMapper.partialUpdate(modifyDto, turnstile))
        .map(userService::create)
        .map(userMapper::toDto));
  }

  @PreAuthorize("hasAuthority('ADMIN_DELETE')")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteById(@PathVariable Long id) {
    userService.deleteById(id);
    return ResponseEntity.noContent().build();
  }
}
