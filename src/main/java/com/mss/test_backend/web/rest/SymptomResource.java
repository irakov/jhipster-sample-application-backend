package com.mss.test_backend.web.rest;
import com.mss.test_backend.domain.Symptom;
import com.mss.test_backend.service.SymptomService;
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
 * REST controller for managing Symptom.
 */
@RestController
@RequestMapping("/api")
public class SymptomResource {

    private final Logger log = LoggerFactory.getLogger(SymptomResource.class);

    private static final String ENTITY_NAME = "symptom";

    private final SymptomService symptomService;

    public SymptomResource(SymptomService symptomService) {
        this.symptomService = symptomService;
    }

    /**
     * POST  /symptoms : Create a new symptom.
     *
     * @param symptom the symptom to create
     * @return the ResponseEntity with status 201 (Created) and with body the new symptom, or with status 400 (Bad Request) if the symptom has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/symptoms")
    public ResponseEntity<Symptom> createSymptom(@Valid @RequestBody Symptom symptom) throws URISyntaxException {
        log.debug("REST request to save Symptom : {}", symptom);
        if (symptom.getId() != null) {
            throw new BadRequestAlertException("A new symptom cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Symptom result = symptomService.save(symptom);
        return ResponseEntity.created(new URI("/api/symptoms/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /symptoms : Updates an existing symptom.
     *
     * @param symptom the symptom to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated symptom,
     * or with status 400 (Bad Request) if the symptom is not valid,
     * or with status 500 (Internal Server Error) if the symptom couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/symptoms")
    public ResponseEntity<Symptom> updateSymptom(@Valid @RequestBody Symptom symptom) throws URISyntaxException {
        log.debug("REST request to update Symptom : {}", symptom);
        if (symptom.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Symptom result = symptomService.save(symptom);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, symptom.getId().toString()))
            .body(result);
    }

    /**
     * GET  /symptoms : get all the symptoms.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of symptoms in body
     */
    @GetMapping("/symptoms")
    public List<Symptom> getAllSymptoms() {
        log.debug("REST request to get all Symptoms");
        return symptomService.findAll();
    }

    /**
     * GET  /symptoms/:id : get the "id" symptom.
     *
     * @param id the id of the symptom to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the symptom, or with status 404 (Not Found)
     */
    @GetMapping("/symptoms/{id}")
    public ResponseEntity<Symptom> getSymptom(@PathVariable Long id) {
        log.debug("REST request to get Symptom : {}", id);
        Optional<Symptom> symptom = symptomService.findOne(id);
        return ResponseUtil.wrapOrNotFound(symptom);
    }

    /**
     * DELETE  /symptoms/:id : delete the "id" symptom.
     *
     * @param id the id of the symptom to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/symptoms/{id}")
    public ResponseEntity<Void> deleteSymptom(@PathVariable Long id) {
        log.debug("REST request to delete Symptom : {}", id);
        symptomService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
