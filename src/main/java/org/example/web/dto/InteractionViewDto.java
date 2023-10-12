package org.example.web.dto;

import lombok.Data;
import org.example.model.Interaction.InteractionType;

@Data
public class InteractionViewDto {
  private InteractionType interactionType;

  private String description;

  private String time;
}
