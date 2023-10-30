package org.example.web.mapper.card;

import org.example.model.DefaultTravelCard;
import org.example.model.LoyaltyTravelCard;
import org.example.model.TravelCard;
import org.example.web.dto.card.DefaultTravelCardModifyDto;
import org.example.web.dto.card.DefaultTravelCardViewDto;
import org.example.web.dto.card.LoyaltyTravelCardModifyDto;
import org.example.web.dto.card.LoyaltyTravelCardViewDto;
import org.example.web.dto.card.TravelCardModifyDto;
import org.example.web.dto.card.TravelCardViewDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.SubclassExhaustiveStrategy;
import org.mapstruct.SubclassMapping;

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    subclassExhaustiveStrategy = SubclassExhaustiveStrategy.RUNTIME_EXCEPTION
)
public abstract class TravelCardMapper {

  @Mapping(target = "travelCardType", source = "type")
  @SubclassMapping(source = DefaultTravelCardModifyDto.class, target = DefaultTravelCard.class)
  @SubclassMapping(source = LoyaltyTravelCardModifyDto.class, target = LoyaltyTravelCard.class)
  public abstract TravelCard toEntity(TravelCardModifyDto dto);

  @InheritInverseConfiguration(name = "toEntity")
  @Mapping(target = "lastPassedId", source = "lastPassed.id")
  @SubclassMapping(source = DefaultTravelCard.class, target = DefaultTravelCardViewDto.class)
  @SubclassMapping(source = LoyaltyTravelCard.class, target = LoyaltyTravelCardViewDto.class)
  public abstract TravelCardViewDto toDto(TravelCard travelCard);

  @InheritInverseConfiguration(name = "toDto")
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  protected abstract DefaultTravelCard partialDefault(DefaultTravelCardModifyDto dto,
                                                      @MappingTarget DefaultTravelCard tc);

  @InheritInverseConfiguration(name = "toDto")
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  protected abstract LoyaltyTravelCard partialLoyalty(LoyaltyTravelCardModifyDto dto,
                                                      @MappingTarget LoyaltyTravelCard tc);
}
