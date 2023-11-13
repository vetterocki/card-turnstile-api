package org.example.model.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_tokens")
@Entity
public class Token {

  @Id
  @SequenceGenerator(name = "token_seq",
      sequenceName = "token_sequence"
  )
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "token_seq")
  private Long id;

  @Column(unique = true)
  private String value;

  private boolean isRevoked = false;

  private boolean isExpired = false;

  @Enumerated(EnumType.STRING)
  private TokenType tokenType;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  @ToString.Exclude
  private User user;

  public enum TokenType {
    ACCESS,
    REFRESH
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    Token token = (Token) o;
    return id != null && Objects.equals(id, token.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
