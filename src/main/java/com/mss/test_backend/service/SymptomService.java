package com.mss.test_backend.service;

import com.mss.test_backend.domain.Symptom;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing Symptom.
 */
public interface SymptomService {

    /**
     * Save a symptom.
     *
     * @param symptom the entity to save
     * @return the persisted entity
     */
    Symptom save(Symptom symptom);

    /**
     * Get all the symptoms.
     *
     * @return the list of entities
     */
    List<Symptom> findAll();


    /**
     * Get the "id" symptom.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Symptom> findOne(Long id);

    /**
     * Delete the "id" symptom.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
