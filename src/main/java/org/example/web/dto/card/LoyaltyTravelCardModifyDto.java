package org.example.web.dto.card;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import lombok.EqualsAndHashCode;


@JsonTypeName("loyalty")
@EqualsAndHashCode(callSuper = true)
@Data
public class LoyaltyTravelCardModifyDto extends TravelCardModifyDto {
  private String cardBalance;
}
