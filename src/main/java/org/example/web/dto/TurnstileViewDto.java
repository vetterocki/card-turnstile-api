package org.example.web.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TurnstileViewDto extends TurnstileModifyDto {
  private Long id;
}
