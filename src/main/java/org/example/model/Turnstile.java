package org.example.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "turnstiles")
public class Turnstile {


  @Id
  @SequenceGenerator(name = "turn_seq",
      sequenceName = "turnstile_sequence",
      allocationSize = 20)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "turn_seq")
  private Long id;

  private BigDecimal farePrice;

  @OneToMany(mappedBy = "turnstile", cascade = CascadeType.PERSIST)
  @ToString.Exclude
  private final List<TravelCardReport> reports = new ArrayList<>();

  public Turnstile(BigDecimal farePrice) {
    this.farePrice = farePrice;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    Turnstile turnstile = (Turnstile) o;
    return id != null && Objects.equals(id, turnstile.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
