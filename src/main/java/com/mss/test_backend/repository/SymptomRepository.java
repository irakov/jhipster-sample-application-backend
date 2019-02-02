package com.mss.test_backend.repository;

import com.mss.test_backend.domain.Symptom;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Symptom entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SymptomRepository extends JpaRepository<Symptom, Long> {

}
