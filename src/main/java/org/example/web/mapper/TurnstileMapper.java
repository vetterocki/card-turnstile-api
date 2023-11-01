package org.example.web.mapper;


import org.example.model.Turnstile;
import org.example.web.dto.TurnstileModifyDto;
import org.example.web.dto.TurnstileViewDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TurnstileMapper {

  @Mapping(target = "farePrice", source = "farePrice", numberFormat = "₴#.##")
  TurnstileViewDto toDto(Turnstile turnstile);

  @InheritInverseConfiguration(name = "toDto")
  Turnstile toEntity(TurnstileModifyDto modifyDto);

  @InheritConfiguration(name = "toEntity")
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  Turnstile partialUpdate(TurnstileModifyDto dto, @MappingTarget Turnstile turnstile);
}
