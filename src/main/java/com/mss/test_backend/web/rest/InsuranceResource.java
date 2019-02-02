package com.mss.test_backend.web.rest;
import com.mss.test_backend.domain.Insurance;
import com.mss.test_backend.service.InsuranceService;
import com.mss.test_backend.web.rest.errors.BadRequestAlertException;
import com.mss.test_backend.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Insurance.
 */
@RestController
@RequestMapping("/api")
public class InsuranceResource {

    private final Logger log = LoggerFactory.getLogger(InsuranceResource.class);

    private static final String ENTITY_NAME = "insurance";

    private final InsuranceService insuranceService;

    public InsuranceResource(InsuranceService insuranceService) {
        this.insuranceService = insuranceService;
    }

    /**
     * POST  /insurances : Create a new insurance.
     *
     * @param insurance the insurance to create
     * @return the ResponseEntity with status 201 (Created) and with body the new insurance, or with status 400 (Bad Request) if the insurance has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/insurances")
    public ResponseEntity<Insurance> createInsurance(@Valid @RequestBody Insurance insurance) throws URISyntaxException {
        log.debug("REST request to save Insurance : {}", insurance);
        if (insurance.getId() != null) {
            throw new BadRequestAlertException("A new insurance cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Insurance result = insuranceService.save(insurance);
        return ResponseEntity.created(new URI("/api/insurances/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /insurances : Updates an existing insurance.
     *
     * @param insurance the insurance to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated insurance,
     * or with status 400 (Bad Request) if the insurance is not valid,
     * or with status 500 (Internal Server Error) if the insurance couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/insurances")
    public ResponseEntity<Insurance> updateInsurance(@Valid @RequestBody Insurance insurance) throws URISyntaxException {
        log.debug("REST request to update Insurance : {}", insurance);
        if (insurance.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Insurance result = insuranceService.save(insurance);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, insurance.getId().toString()))
            .body(result);
    }

    /**
     * GET  /insurances : get all the insurances.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of insurances in body
     */
    @GetMapping("/insurances")
    public List<Insurance> getAllInsurances() {
        log.debug("REST request to get all Insurances");
        return insuranceService.findAll();
    }

    /**
     * GET  /insurances/:id : get the "id" insurance.
     *
     * @param id the id of the insurance to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the insurance, or with status 404 (Not Found)
     */
    @GetMapping("/insurances/{id}")
    public ResponseEntity<Insurance> getInsurance(@PathVariable Long id) {
        log.debug("REST request to get Insurance : {}", id);
        Optional<Insurance> insurance = insuranceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(insurance);
    }

    /**
     * DELETE  /insurances/:id : delete the "id" insurance.
     *
     * @param id the id of the insurance to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/insurances/{id}")
    public ResponseEntity<Void> deleteInsurance(@PathVariable Long id) {
        log.debug("REST request to delete Insurance : {}", id);
        insuranceService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
