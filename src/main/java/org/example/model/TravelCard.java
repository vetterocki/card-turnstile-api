package org.example.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
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
@Table(name = "travel_cards")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class TravelCard {

  @Id
  @SequenceGenerator(name = "card_seq",
      sequenceName = "card_sequence"
  )
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "card_seq")
  private Long id;

  @Setter(AccessLevel.PUBLIC)
  @Enumerated(EnumType.STRING)
  private TravelCardType travelCardType;

  @Setter(AccessLevel.PUBLIC)
  @ManyToOne
  @JoinColumn(name = "last_passed_turnstile_id")
  private Turnstile lastPassed;

  @OneToMany(mappedBy = "travelCard", cascade = CascadeType.REMOVE)
  @ToString.Exclude
  private final List<TravelCardReport> reports = new ArrayList<>();

  protected TravelCard(TravelCardType travelCardType) {
    this.travelCardType = travelCardType;
  }

  public abstract void accept(TravelCardVisitor travelCardVisitor);

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    TravelCard that = (TravelCard) o;
    return id != null && Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
