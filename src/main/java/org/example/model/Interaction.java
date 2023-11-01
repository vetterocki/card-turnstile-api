package org.example.model;

import static org.example.model.Interaction.InteractionType.SUCCESSFUL_ACCESS;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Embeddable
public class Interaction {

  @Enumerated(EnumType.STRING)
  private InteractionType interactionType;

  private String description;

  private final LocalDateTime time = LocalDateTime.now();

  public static final Interaction SUCCESS = new Interaction(
      SUCCESSFUL_ACCESS, successfulDescription()
  );

  public static Interaction deniedFrom(String description) {
    return new Interaction(InteractionType.DENIED, description);
  }

  private Interaction(InteractionType interactionType, String description) {
    this.interactionType = interactionType;
    this.description = description;
  }

  public enum InteractionType {
    SUCCESSFUL_ACCESS,
    DENIED
  }

  public boolean isSuccessful() {
    return interactionType.equals(SUCCESSFUL_ACCESS);
  }

  private static String successfulDescription() {
    String success = SUCCESSFUL_ACCESS.name().replace("_", " ");
    return success.charAt(0) + success.substring(1).toLowerCase();
  }

}
