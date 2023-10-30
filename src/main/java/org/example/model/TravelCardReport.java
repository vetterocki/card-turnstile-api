package org.example.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.Hibernate;

@ToString
@Getter
@NoArgsConstructor
@Table(name = "travel_card_reports")
@Entity
public class TravelCardReport {

  @Id
  @SequenceGenerator(name = "card_report_seq",
      sequenceName = "card_report_sequence"
  )
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "card_report_seq")
  private Long id;

  @JsonIgnore
  @ManyToOne
  @JoinColumn(name = "travel_card_id")
  private TravelCard travelCard;

  @JsonIgnore
  @ManyToOne
  @JoinColumn(name = "turnstile_id")
  private Turnstile turnstile;

  @ElementCollection
  @CollectionTable(name = "report_interactions", joinColumns = {
      @JoinColumn(name = "report_id", referencedColumnName = "id")})
  private final List<Interaction> interactions = new LinkedList<>();

  public TravelCardReport(TravelCard travelCard, Turnstile turnstile) {
    this.travelCard = travelCard;
    this.turnstile = turnstile;
  }

  public void addInteraction(Interaction interaction) {
    this.interactions.add(interaction);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    TravelCardReport report = (TravelCardReport) o;
    return id != null && Objects.equals(id, report.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
