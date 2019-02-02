package com.mss.test_backend.service.impl;

import com.mss.test_backend.service.InsuranceService;
import com.mss.test_backend.domain.Insurance;
import com.mss.test_backend.repository.InsuranceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing Insurance.
 */
@Service
@Transactional
public class InsuranceServiceImpl implements InsuranceService {

    private final Logger log = LoggerFactory.getLogger(InsuranceServiceImpl.class);

    private final InsuranceRepository insuranceRepository;

    public InsuranceServiceImpl(InsuranceRepository insuranceRepository) {
        this.insuranceRepository = insuranceRepository;
    }

    /**
     * Save a insurance.
     *
     * @param insurance the entity to save
     * @return the persisted entity
     */
    @Override
    public Insurance save(Insurance insurance) {
        log.debug("Request to save Insurance : {}", insurance);
        return insuranceRepository.save(insurance);
    }

    /**
     * Get all the insurances.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<Insurance> findAll() {
        log.debug("Request to get all Insurances");
        return insuranceRepository.findAll();
    }


    /**
     * Get one insurance by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Insurance> findOne(Long id) {
        log.debug("Request to get Insurance : {}", id);
        return insuranceRepository.findById(id);
    }

    /**
     * Delete the insurance by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Insurance : {}", id);        insuranceRepository.deleteById(id);
    }
}
