package org.example.web.dto.card;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.example.model.card.TravelAmount;
import org.example.model.card.ValidityPeriod;

@JsonTypeName("default")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Data
public class DefaultTravelCardModifyDto extends TravelCardModifyDto {
  private TravelAmount travelAmount;
  private ValidityPeriod validityPeriod;
}
