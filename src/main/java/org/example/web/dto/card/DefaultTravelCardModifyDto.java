package org.example.web.dto.card;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.model.TravelAmount;
import org.example.model.ValidityPeriod;

@JsonTypeName("default")
@EqualsAndHashCode(callSuper = true)
@Data
public class DefaultTravelCardModifyDto extends TravelCardModifyDto {
  private TravelAmount travelAmount;
  private ValidityPeriod validityPeriod;
}
