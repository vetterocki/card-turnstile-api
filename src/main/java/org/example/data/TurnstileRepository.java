package org.example.data;

import org.example.model.Turnstile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TurnstileRepository extends JpaRepository<Turnstile, Long> {
}
