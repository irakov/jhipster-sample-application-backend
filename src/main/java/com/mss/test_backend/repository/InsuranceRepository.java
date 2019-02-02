package com.mss.test_backend.repository;

import com.mss.test_backend.domain.Insurance;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Insurance entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InsuranceRepository extends JpaRepository<Insurance, Long> {

}
