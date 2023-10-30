package org.example.web;


import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.example.model.TravelCard;
import org.example.service.TravelCardService;
import org.example.web.dto.card.TravelCardModifyDto;
import org.example.web.dto.card.TravelCardViewDto;
import org.example.web.mapper.card.TravelCardMapperDecorator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/travel-cards")
@RestController
public class TravelCardController {
  private final TravelCardService travelCardService;
  private final TravelCardMapperDecorator travelCardMapper;

  @GetMapping
  public ResponseEntity<List<TravelCardViewDto>> findAll() {
    return travelCardService.findAll().stream()
        .map(travelCardMapper::toDto)
        .collect(Collectors.collectingAndThen(Collectors.toList(), ResponseEntity::ok));
  }

  @GetMapping("/{id}")
  public ResponseEntity<TravelCardViewDto> findById(@PathVariable Long id) {
    return ResponseEntity.of(travelCardService.findById(id).map(travelCardMapper::toDto));
  }

  @PostMapping
  public ResponseEntity<TravelCardViewDto> create(@RequestBody TravelCardModifyDto modifyDto) {
    TravelCard created = travelCardService.save(travelCardMapper.toEntity(modifyDto));
    return ResponseEntity.status(HttpStatus.CREATED).body(travelCardMapper.toDto(created));
  }

  @PatchMapping("/{id}")
  public ResponseEntity<TravelCardViewDto> partialUpdate(
      @Valid @RequestBody TravelCardModifyDto modifyDto,
      @PathVariable Long id) {
    return ResponseEntity.of(travelCardService.findById(id)
        .map(travelCard -> travelCardMapper.partialUpdate(modifyDto, travelCard))
        .map(travelCardService::save)
        .map(travelCardMapper::toDto));
  }
}
