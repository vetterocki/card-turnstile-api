package org.example.web.mapper;

import org.example.model.Interaction;
import org.example.web.dto.InteractionViewDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface InteractionMapper {

  @Mapping(target = "time", source = "time", dateFormat = "yyyy-MM-dd-HH-mm-ss")
  InteractionViewDto toDto(Interaction interaction);
}
