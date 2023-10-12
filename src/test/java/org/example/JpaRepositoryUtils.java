package org.example;

import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.repository.JpaRepository;

@UtilityClass
public class JpaRepositoryUtils {
  public static <T, ID> T findById(ID id, JpaRepository<T, ID> jpaRepository) {
    return jpaRepository.findById(id).orElseThrow(AssertionError::new);
  }
}
