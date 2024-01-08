package org.example.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.example.model.Interaction;
import org.example.model.Turnstile;
import org.example.model.card.TravelCardType;
import org.example.service.TurnstileService;
import org.example.web.dto.ExceptionResponse;
import org.example.web.dto.InteractionViewDto;
import org.example.web.dto.TravelCardReportViewDto;
import org.example.web.dto.TurnstileModifyDto;
import org.example.web.dto.TurnstileViewDto;
import org.example.web.mapper.InteractionMapper;
import org.example.web.mapper.TravelCardReportMapper;
import org.example.web.mapper.TurnstileMapper;
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
@RequestMapping("/turnstiles")
@Tag(name = "Turnstile controller")
@SecurityRequirement(name = "Bearer", scopes = "Access")
@RestController
public class TurnstileController {
  private final TurnstileService turnstileService;
  private final TurnstileMapper turnstileMapper;
  private final InteractionMapper interactionMapper;
  private final TravelCardReportMapper travelCardReportMapper;

  @GetMapping("/{id}")
  @Operation(summary = "Get turnstile by id", responses = {
      @ApiResponse(responseCode = "200"),
      @ApiResponse(responseCode = "404", description = "Turnstile not found", content = @Content)
  })
  public ResponseEntity<TurnstileViewDto> findById(@PathVariable Long id) {
    return ResponseEntity.of(turnstileService.findById(id).map(turnstileMapper::toDto));
  }

  @GetMapping("/{id}/reports")
  @Operation(summary = "Find all turnstile`s reports", responses = {
      @ApiResponse(responseCode = "200", content = @Content),
      @ApiResponse(responseCode = "404", description = "Turnstile not found",
          content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
  })
  public ResponseEntity<List<TravelCardReportViewDto>> findAllReports(@PathVariable Long id) {
    return ResponseEntity.of(turnstileService.findAllReports(id)
        .map(travelCardReports -> travelCardReports.stream()
            .map(travelCardReportMapper::toDto)
            .toList()));
  }

  @GetMapping("/{id}/reports/grouped-by-type")
  @Operation(summary = "Find all turnstile`s reports, grouping by type", responses = {
      @ApiResponse(responseCode = "200", content = @Content),
      @ApiResponse(responseCode = "404", description = "Turnstile not found",
          content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
  })
  public ResponseEntity<Map<TravelCardType, List<TravelCardReportViewDto>>>
  findAllReportsGroupedByTravelCardType(@PathVariable Long id) {
    return ResponseEntity.of(turnstileService.findAllReportsGroupedByTravelCardType(id)
        .map(groupedByTypeMap -> groupedByTypeMap.entrySet().stream()
            .map(entry -> entry.getValue().stream()
                .map(travelCardReportMapper::toDto)
                .collect(Collectors.collectingAndThen(Collectors.toList(),
                    reports -> Map.entry(entry.getKey(), reports))))
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (v1, v2) -> v1,
                LinkedHashMap::new)
            ))
    );
  }

  @PostMapping
  @Operation(
      summary = "Create turnstile",
      responses = {
          @ApiResponse(responseCode = "201", content = @Content),
          @ApiResponse(responseCode = "400",
              description = "Invalid properties` values",
              content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
      })
  public ResponseEntity<TurnstileViewDto> create(@RequestBody TurnstileModifyDto modifyDto) {
    Turnstile created = turnstileService.create(turnstileMapper.toEntity(modifyDto));
    return ResponseEntity.status(HttpStatus.CREATED).body(turnstileMapper.toDto(created));
  }

  @PatchMapping("/{id}/pass/{card-id}")
  @Operation(summary = "Pass turnstile", responses = {
      @ApiResponse(responseCode = "200", content = @Content),
      @ApiResponse(responseCode = "404", description = "Turnstile or travel card not found by id")
  })
  public ResponseEntity<InteractionViewDto> passTurnstile(@PathVariable Long id,
                                                          @PathVariable(name = "card-id")
                                                          Long cardId) {
    Interaction interaction = turnstileService.passTurnstile(cardId, id);
    return ResponseEntity.ok(interactionMapper.toDto(interaction));
  }

  @PatchMapping("/{id}")
  @Operation(summary = "Update turnstile", responses = {
      @ApiResponse(responseCode = "200", content = @Content),
      @ApiResponse(responseCode = "404", description = "Turnstile not found by id"),
      @ApiResponse(responseCode = "400",
          description = "Invalid properties` values",
          content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
  })
  public ResponseEntity<TurnstileViewDto> partialUpdate(
      @PathVariable Long id,
      @Valid @RequestBody TurnstileModifyDto modifyDto) {
    return ResponseEntity.of(turnstileService.findById(id)
        .map(turnstile -> turnstileMapper.partialUpdate(modifyDto, turnstile))
        .map(turnstileService::create)
        .map(turnstileMapper::toDto));
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete turnstile by id", responses = {
      @ApiResponse(responseCode = "204")
  })
  public ResponseEntity<Void> deleteById(@PathVariable Long id) {
    turnstileService.deleteById(id);
    return ResponseEntity.noContent().build();
  }
}
