package org.example.web.dto.card;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.example.model.TravelCardType;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes(value = {
    @JsonSubTypes.Type(name = "default", value = DefaultTravelCardModifyDto.class),
    @JsonSubTypes.Type(name = "loyalty", value = LoyaltyTravelCardModifyDto.class)
})
@Data
public abstract class TravelCardModifyDto {
  @NotNull(message = "Specify travel card type")
  private TravelCardType type;
}
