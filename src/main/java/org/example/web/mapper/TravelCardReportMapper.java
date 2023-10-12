package org.example.web.mapper;

import org.example.model.TravelCardReport;
import org.example.web.dto.TravelCardReportViewDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TravelCardReportMapper {

  @Mapping(target = "travelCardId", source = "travelCard.id")
  @Mapping(target = "turnstileId", source = "turnstile.id")
  TravelCardReportViewDto toDto(TravelCardReport travelCardReport);
}
