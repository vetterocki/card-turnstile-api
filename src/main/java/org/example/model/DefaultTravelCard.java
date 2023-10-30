package org.example.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.example.service.visitor.TravelCardVisitor;
import org.hibernate.Hibernate;
import org.springframework.format.annotation.DateTimeFormat;

@Setter
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "default_travel_cards")
public class DefaultTravelCard extends TravelCard {

  @Enumerated(EnumType.STRING)
  private ValidityPeriod validityPeriod;

  @Enumerated(EnumType.STRING)
  private TravelAmount travelAmount;

  private int travelsLeft;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private LocalDateTime expiresAt;

  public DefaultTravelCard(TravelCardType travelCardType, ValidityPeriod validityPeriod,
                           TravelAmount travelAmount) {
    super(travelCardType);
    this.validityPeriod = validityPeriod;
    this.travelAmount = travelAmount;
    this.travelsLeft = travelAmount.value;
    this.expiresAt = validityPeriod.dateTime;
  }

  @Override
  public void accept(TravelCardVisitor travelCardVisitor) {
    travelCardVisitor.visitDefault(this);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    DefaultTravelCard that = (DefaultTravelCard) o;
    return getId() != null && Objects.equals(getId(), that.getId());
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
