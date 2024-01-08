package org.example.web.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.example.model.card.TravelCard;
import org.example.service.TravelCardService;
import org.example.web.dto.ExceptionResponse;
import org.example.web.dto.card.DefaultTravelCardModifyDto;
import org.example.web.dto.card.DefaultTravelCardViewDto;
import org.example.web.dto.card.LoyaltyTravelCardModifyDto;
import org.example.web.dto.card.LoyaltyTravelCardViewDto;
import org.example.web.dto.card.TravelCardModifyDto;
import org.example.web.dto.card.TravelCardViewDto;
import org.example.web.mapper.card.TravelCardMapperDecorator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/travel-cards")
@Tag(name = "Travel card controller")
@SecurityRequirement(name = "Bearer", scopes = "Access")
@RestController
public class TravelCardController {
  private final TravelCardService travelCardService;
  private final TravelCardMapperDecorator travelCardMapper;

  @GetMapping
  @Operation(summary = "Find all travel cards", responses = {
      @ApiResponse(responseCode = "200", content = @Content(
          schema = @Schema(
              anyOf = {DefaultTravelCardViewDto.class, LoyaltyTravelCardViewDto.class},
              description = "Depending on travel card type, corresponding DTO returns"
          )))
  })
  public ResponseEntity<List<TravelCardViewDto>> findAll() {
    return travelCardService.findAll().stream()
        .map(travelCardMapper::toDto)
        .collect(Collectors.collectingAndThen(Collectors.toList(), ResponseEntity::ok));
  }

  @GetMapping("/{id}")
  @Operation(summary = "Find travel card by id", responses = {
      @ApiResponse(responseCode = "200", content = @Content(
          schema = @Schema(
              oneOf = {DefaultTravelCardViewDto.class, LoyaltyTravelCardViewDto.class},
              description = "Depending on travel card type, corresponding DTO returns"
          ))),
      @ApiResponse(responseCode = "404", content = @Content)
  })
  public ResponseEntity<TravelCardViewDto> findById(@PathVariable Long id) {
    return ResponseEntity.of(travelCardService.findById(id).map(travelCardMapper::toDto));
  }

  @PostMapping
  @Operation(
      summary = "Create travel card",
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
          content = @Content(schema = @Schema(
              oneOf = {DefaultTravelCardModifyDto.class, LoyaltyTravelCardModifyDto.class}
          ))
      ),
      responses = {
          @ApiResponse(responseCode = "201", content = @Content),
          @ApiResponse(responseCode = "400",
              content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
      })
  public ResponseEntity<TravelCardViewDto> create(@Valid @RequestBody TravelCardModifyDto modifyDto) {
    TravelCard created = travelCardService.save(travelCardMapper.toEntity(modifyDto));
    return ResponseEntity.status(HttpStatus.CREATED).body(travelCardMapper.toDto(created));
  }

  @PatchMapping("/{id}")
  @Operation(
      summary = "Update travel card",
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
          content = @Content(schema = @Schema(
              oneOf = {DefaultTravelCardModifyDto.class, LoyaltyTravelCardModifyDto.class}
          ))
      ),
      responses = {
          @ApiResponse(responseCode = "200", content = @Content),
          @ApiResponse(responseCode = "400",
              description = "Invalid properties` values",
              content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
      })
  public ResponseEntity<TravelCardViewDto> partialUpdate(
      @Valid @RequestBody TravelCardModifyDto modifyDto,
      @PathVariable Long id) {
    return ResponseEntity.of(travelCardService.findById(id)
        .map(travelCard -> travelCardMapper.partialUpdate(modifyDto, travelCard))
        .map(travelCardService::save)
        .map(travelCardMapper::toDto));
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete travel card by id", responses = {
      @ApiResponse(responseCode = "204")
  })
  public ResponseEntity<Void> deleteById(@PathVariable Long id) {
    travelCardService.deleteById(id);
    return ResponseEntity.noContent().build();
  }
}
