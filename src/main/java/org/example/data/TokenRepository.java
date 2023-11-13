package org.example.data;

import java.util.List;
import java.util.Optional;
import org.example.model.user.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
  Optional<Token> findByValue(String value);

  @Query("""
         SELECT t FROM Token t
         WHERE t.user.id = :userId AND (t.isExpired = false OR t.isRevoked = false )
         """)
  Optional<List<Token>> findAllValidTokensByUserId(Long userId);

  boolean existsByValue(String tokenValue);
}
