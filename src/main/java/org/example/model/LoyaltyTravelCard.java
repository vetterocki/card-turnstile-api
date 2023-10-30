package org.example.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.example.service.visitor.TravelCardVisitor;
import org.hibernate.Hibernate;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "loyalty_travel_cards")
public class LoyaltyTravelCard extends TravelCard {

  @Setter(AccessLevel.PUBLIC)
  private BigDecimal cardBalance;

  public LoyaltyTravelCard(BigDecimal cardBalance) {
    super(TravelCardType.ORDINARY);
    this.cardBalance = cardBalance;
  }

  @Override
  public void accept(TravelCardVisitor travelCardVisitor) {
    travelCardVisitor.visitLoyalty(this);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    LoyaltyTravelCard that = (LoyaltyTravelCard) o;
    return getId() != null && Objects.equals(getId(), that.getId());
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
