package com.mss.test_backend.web.rest;

import com.mss.test_backend.JhipsterBackendApp;

import com.mss.test_backend.domain.Patient;
import com.mss.test_backend.repository.PatientRepository;
import com.mss.test_backend.service.PatientService;
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
 * Test class for the PatientResource REST controller.
 *
 * @see PatientResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhipsterBackendApp.class)
public class PatientResourceIntTest {

    private static final String DEFAULT_USER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_USER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_DOB = "AAAAAAAAAA";
    private static final String UPDATED_DOB = "BBBBBBBBBB";

    private static final String DEFAULT_SSN = "AAAAAAAAAA";
    private static final String UPDATED_SSN = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_ZIP = "AAAAAAAAAA";
    private static final String UPDATED_ZIP = "BBBBBBBBBB";

    private static final String DEFAULT_STATE = "AAAAAAAAAA";
    private static final String UPDATED_STATE = "BBBBBBBBBB";

    private static final String DEFAULT_INSURANCE = "AAAAAAAAAA";
    private static final String UPDATED_INSURANCE = "BBBBBBBBBB";

    private static final String DEFAULT_DOCTOR = "AAAAAAAAAA";
    private static final String UPDATED_DOCTOR = "BBBBBBBBBB";

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PatientService patientService;

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

    private MockMvc restPatientMockMvc;

    private Patient patient;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PatientResource patientResource = new PatientResource(patientService);
        this.restPatientMockMvc = MockMvcBuilders.standaloneSetup(patientResource)
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
    public static Patient createEntity(EntityManager em) {
        Patient patient = new Patient()
            .userName(DEFAULT_USER_NAME)
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .email(DEFAULT_EMAIL)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .dob(DEFAULT_DOB)
            .ssn(DEFAULT_SSN)
            .address(DEFAULT_ADDRESS)
            .city(DEFAULT_CITY)
            .zip(DEFAULT_ZIP)
            .state(DEFAULT_STATE)
            .insurance(DEFAULT_INSURANCE)
            .doctor(DEFAULT_DOCTOR);
        return patient;
    }

    @Before
    public void initTest() {
        patient = createEntity(em);
    }

    @Test
    @Transactional
    public void createPatient() throws Exception {
        int databaseSizeBeforeCreate = patientRepository.findAll().size();

        // Create the Patient
        restPatientMockMvc.perform(post("/api/patients")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(patient)))
            .andExpect(status().isCreated());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeCreate + 1);
        Patient testPatient = patientList.get(patientList.size() - 1);
        assertThat(testPatient.getUserName()).isEqualTo(DEFAULT_USER_NAME);
        assertThat(testPatient.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testPatient.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testPatient.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testPatient.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testPatient.getDob()).isEqualTo(DEFAULT_DOB);
        assertThat(testPatient.getSsn()).isEqualTo(DEFAULT_SSN);
        assertThat(testPatient.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testPatient.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testPatient.getZip()).isEqualTo(DEFAULT_ZIP);
        assertThat(testPatient.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testPatient.getInsurance()).isEqualTo(DEFAULT_INSURANCE);
        assertThat(testPatient.getDoctor()).isEqualTo(DEFAULT_DOCTOR);
    }

    @Test
    @Transactional
    public void createPatientWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = patientRepository.findAll().size();

        // Create the Patient with an existing ID
        patient.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPatientMockMvc.perform(post("/api/patients")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(patient)))
            .andExpect(status().isBadRequest());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllPatients() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList
        restPatientMockMvc.perform(get("/api/patients?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(patient.getId().intValue())))
            .andExpect(jsonPath("$.[*].userName").value(hasItem(DEFAULT_USER_NAME.toString())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].dob").value(hasItem(DEFAULT_DOB.toString())))
            .andExpect(jsonPath("$.[*].ssn").value(hasItem(DEFAULT_SSN.toString())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
            .andExpect(jsonPath("$.[*].zip").value(hasItem(DEFAULT_ZIP.toString())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.[*].insurance").value(hasItem(DEFAULT_INSURANCE.toString())))
            .andExpect(jsonPath("$.[*].doctor").value(hasItem(DEFAULT_DOCTOR.toString())));
    }
    
    @Test
    @Transactional
    public void getPatient() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get the patient
        restPatientMockMvc.perform(get("/api/patients/{id}", patient.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(patient.getId().intValue()))
            .andExpect(jsonPath("$.userName").value(DEFAULT_USER_NAME.toString()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER.toString()))
            .andExpect(jsonPath("$.dob").value(DEFAULT_DOB.toString()))
            .andExpect(jsonPath("$.ssn").value(DEFAULT_SSN.toString()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY.toString()))
            .andExpect(jsonPath("$.zip").value(DEFAULT_ZIP.toString()))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()))
            .andExpect(jsonPath("$.insurance").value(DEFAULT_INSURANCE.toString()))
            .andExpect(jsonPath("$.doctor").value(DEFAULT_DOCTOR.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPatient() throws Exception {
        // Get the patient
        restPatientMockMvc.perform(get("/api/patients/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePatient() throws Exception {
        // Initialize the database
        patientService.save(patient);

        int databaseSizeBeforeUpdate = patientRepository.findAll().size();

        // Update the patient
        Patient updatedPatient = patientRepository.findById(patient.getId()).get();
        // Disconnect from session so that the updates on updatedPatient are not directly saved in db
        em.detach(updatedPatient);
        updatedPatient
            .userName(UPDATED_USER_NAME)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .dob(UPDATED_DOB)
            .ssn(UPDATED_SSN)
            .address(UPDATED_ADDRESS)
            .city(UPDATED_CITY)
            .zip(UPDATED_ZIP)
            .state(UPDATED_STATE)
            .insurance(UPDATED_INSURANCE)
            .doctor(UPDATED_DOCTOR);

        restPatientMockMvc.perform(put("/api/patients")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPatient)))
            .andExpect(status().isOk());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeUpdate);
        Patient testPatient = patientList.get(patientList.size() - 1);
        assertThat(testPatient.getUserName()).isEqualTo(UPDATED_USER_NAME);
        assertThat(testPatient.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testPatient.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testPatient.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testPatient.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testPatient.getDob()).isEqualTo(UPDATED_DOB);
        assertThat(testPatient.getSsn()).isEqualTo(UPDATED_SSN);
        assertThat(testPatient.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testPatient.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testPatient.getZip()).isEqualTo(UPDATED_ZIP);
        assertThat(testPatient.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testPatient.getInsurance()).isEqualTo(UPDATED_INSURANCE);
        assertThat(testPatient.getDoctor()).isEqualTo(UPDATED_DOCTOR);
    }

    @Test
    @Transactional
    public void updateNonExistingPatient() throws Exception {
        int databaseSizeBeforeUpdate = patientRepository.findAll().size();

        // Create the Patient

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPatientMockMvc.perform(put("/api/patients")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(patient)))
            .andExpect(status().isBadRequest());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePatient() throws Exception {
        // Initialize the database
        patientService.save(patient);

        int databaseSizeBeforeDelete = patientRepository.findAll().size();

        // Delete the patient
        restPatientMockMvc.perform(delete("/api/patients/{id}", patient.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Patient.class);
        Patient patient1 = new Patient();
        patient1.setId(1L);
        Patient patient2 = new Patient();
        patient2.setId(patient1.getId());
        assertThat(patient1).isEqualTo(patient2);
        patient2.setId(2L);
        assertThat(patient1).isNotEqualTo(patient2);
        patient1.setId(null);
        assertThat(patient1).isNotEqualTo(patient2);
    }
}
