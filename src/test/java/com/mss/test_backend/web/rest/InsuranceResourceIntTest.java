package com.mss.test_backend.web.rest;

import com.mss.test_backend.JhipsterBackendApp;

import com.mss.test_backend.domain.Insurance;
import com.mss.test_backend.repository.InsuranceRepository;
import com.mss.test_backend.service.InsuranceService;
import com.mss.test_backend.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;


import static com.mss.test_backend.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the InsuranceResource REST controller.
 *
 * @see InsuranceResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhipsterBackendApp.class)
public class InsuranceResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    @Autowired
    private InsuranceRepository insuranceRepository;

    @Autowired
    private InsuranceService insuranceService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restInsuranceMockMvc;

    private Insurance insurance;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final InsuranceResource insuranceResource = new InsuranceResource(insuranceService);
        this.restInsuranceMockMvc = MockMvcBuilders.standaloneSetup(insuranceResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Insurance createEntity(EntityManager em) {
        Insurance insurance = new Insurance()
            .name(DEFAULT_NAME)
            .address(DEFAULT_ADDRESS);
        return insurance;
    }

    @Before
    public void initTest() {
        insurance = createEntity(em);
    }

    @Test
    @Transactional
    public void createInsurance() throws Exception {
        int databaseSizeBeforeCreate = insuranceRepository.findAll().size();

        // Create the Insurance
        restInsuranceMockMvc.perform(post("/api/insurances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(insurance)))
            .andExpect(status().isCreated());

        // Validate the Insurance in the database
        List<Insurance> insuranceList = insuranceRepository.findAll();
        assertThat(insuranceList).hasSize(databaseSizeBeforeCreate + 1);
        Insurance testInsurance = insuranceList.get(insuranceList.size() - 1);
        assertThat(testInsurance.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testInsurance.getAddress()).isEqualTo(DEFAULT_ADDRESS);
    }

    @Test
    @Transactional
    public void createInsuranceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = insuranceRepository.findAll().size();

        // Create the Insurance with an existing ID
        insurance.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInsuranceMockMvc.perform(post("/api/insurances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(insurance)))
            .andExpect(status().isBadRequest());

        // Validate the Insurance in the database
        List<Insurance> insuranceList = insuranceRepository.findAll();
        assertThat(insuranceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = insuranceRepository.findAll().size();
        // set the field null
        insurance.setName(null);

        // Create the Insurance, which fails.

        restInsuranceMockMvc.perform(post("/api/insurances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(insurance)))
            .andExpect(status().isBadRequest());

        List<Insurance> insuranceList = insuranceRepository.findAll();
        assertThat(insuranceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllInsurances() throws Exception {
        // Initialize the database
        insuranceRepository.saveAndFlush(insurance);

        // Get all the insuranceList
        restInsuranceMockMvc.perform(get("/api/insurances?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(insurance.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())));
    }
    
    @Test
    @Transactional
    public void getInsurance() throws Exception {
        // Initialize the database
        insuranceRepository.saveAndFlush(insurance);

        // Get the insurance
        restInsuranceMockMvc.perform(get("/api/insurances/{id}", insurance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(insurance.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingInsurance() throws Exception {
        // Get the insurance
        restInsuranceMockMvc.perform(get("/api/insurances/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInsurance() throws Exception {
        // Initialize the database
        insuranceService.save(insurance);

        int databaseSizeBeforeUpdate = insuranceRepository.findAll().size();

        // Update the insurance
        Insurance updatedInsurance = insuranceRepository.findById(insurance.getId()).get();
        // Disconnect from session so that the updates on updatedInsurance are not directly saved in db
        em.detach(updatedInsurance);
        updatedInsurance
            .name(UPDATED_NAME)
            .address(UPDATED_ADDRESS);

        restInsuranceMockMvc.perform(put("/api/insurances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedInsurance)))
            .andExpect(status().isOk());

        // Validate the Insurance in the database
        List<Insurance> insuranceList = insuranceRepository.findAll();
        assertThat(insuranceList).hasSize(databaseSizeBeforeUpdate);
        Insurance testInsurance = insuranceList.get(insuranceList.size() - 1);
        assertThat(testInsurance.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testInsurance.getAddress()).isEqualTo(UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void updateNonExistingInsurance() throws Exception {
        int databaseSizeBeforeUpdate = insuranceRepository.findAll().size();

        // Create the Insurance

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInsuranceMockMvc.perform(put("/api/insurances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(insurance)))
            .andExpect(status().isBadRequest());

        // Validate the Insurance in the database
        List<Insurance> insuranceList = insuranceRepository.findAll();
        assertThat(insuranceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteInsurance() throws Exception {
        // Initialize the database
        insuranceService.save(insurance);

        int databaseSizeBeforeDelete = insuranceRepository.findAll().size();

        // Delete the insurance
        restInsuranceMockMvc.perform(delete("/api/insurances/{id}", insurance.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Insurance> insuranceList = insuranceRepository.findAll();
        assertThat(insuranceList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Insurance.class);
        Insurance insurance1 = new Insurance();
        insurance1.setId(1L);
        Insurance insurance2 = new Insurance();
        insurance2.setId(insurance1.getId());
        assertThat(insurance1).isEqualTo(insurance2);
        insurance2.setId(2L);
        assertThat(insurance1).isNotEqualTo(insurance2);
        insurance1.setId(null);
        assertThat(insurance1).isNotEqualTo(insurance2);
    }
}
