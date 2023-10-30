package org.example.data;

import org.example.model.TravelCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TravelCardRepository extends JpaRepository<TravelCard, Long> {

}
