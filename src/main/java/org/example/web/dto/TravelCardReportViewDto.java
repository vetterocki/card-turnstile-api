package org.example.web.dto;

import java.util.List;
import lombok.Data;
import org.example.model.Interaction;

@Data
public class TravelCardReportViewDto {
  private Long id;

  private Long travelCardId;

  private Long turnstileId;

  private List<Interaction> interactions;
}
