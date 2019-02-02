package com.mss.test_backend.service;

import com.mss.test_backend.domain.Insurance;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing Insurance.
 */
public interface InsuranceService {

    /**
     * Save a insurance.
     *
     * @param insurance the entity to save
     * @return the persisted entity
     */
    Insurance save(Insurance insurance);

    /**
     * Get all the insurances.
     *
     * @return the list of entities
     */
    List<Insurance> findAll();


    /**
     * Get the "id" insurance.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Insurance> findOne(Long id);

    /**
     * Delete the "id" insurance.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
