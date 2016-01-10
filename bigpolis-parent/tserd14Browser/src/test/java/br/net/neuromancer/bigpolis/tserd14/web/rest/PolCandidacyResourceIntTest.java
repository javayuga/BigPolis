package br.net.neuromancer.bigpolis.tserd14.web.rest;

import br.net.neuromancer.bigpolis.tserd14.Application;
import br.net.neuromancer.bigpolis.tserd14.domain.PolCandidacy;
import br.net.neuromancer.bigpolis.tserd14.repository.PolCandidacyRepository;
import br.net.neuromancer.bigpolis.tserd14.service.PolCandidacyService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.net.neuromancer.bigpolis.tserd14.domain.enumeration.PolOffice;
import br.net.neuromancer.bigpolis.tserd14.domain.enumeration.GeoAdminRegion;

/**
 * Test class for the PolCandidacyResource REST controller.
 *
 * @see PolCandidacyResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class PolCandidacyResourceIntTest {



    private static final PolOffice DEFAULT_OFFICE = PolOffice.PRESIDENTE;
    private static final PolOffice UPDATED_OFFICE = PolOffice.VICE_PRESIDENTE;


    private static final GeoAdminRegion DEFAULT_REGION = GeoAdminRegion.RJ;
    private static final GeoAdminRegion UPDATED_REGION = GeoAdminRegion.SP;

    private static final LocalDate DEFAULT_ELECTION_DAY = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ELECTION_DAY = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private PolCandidacyRepository polCandidacyRepository;

    @Inject
    private PolCandidacyService polCandidacyService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPolCandidacyMockMvc;

    private PolCandidacy polCandidacy;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PolCandidacyResource polCandidacyResource = new PolCandidacyResource();
        ReflectionTestUtils.setField(polCandidacyResource, "polCandidacyService", polCandidacyService);
        this.restPolCandidacyMockMvc = MockMvcBuilders.standaloneSetup(polCandidacyResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        polCandidacy = new PolCandidacy();
        polCandidacy.setOffice(DEFAULT_OFFICE);
        polCandidacy.setRegion(DEFAULT_REGION);
        polCandidacy.setElectionDay(DEFAULT_ELECTION_DAY);
    }

    @Test
    @Transactional
    public void createPolCandidacy() throws Exception {
        int databaseSizeBeforeCreate = polCandidacyRepository.findAll().size();

        // Create the PolCandidacy

        restPolCandidacyMockMvc.perform(post("/api/polCandidacys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(polCandidacy)))
                .andExpect(status().isCreated());

        // Validate the PolCandidacy in the database
        List<PolCandidacy> polCandidacys = polCandidacyRepository.findAll();
        assertThat(polCandidacys).hasSize(databaseSizeBeforeCreate + 1);
        PolCandidacy testPolCandidacy = polCandidacys.get(polCandidacys.size() - 1);
        assertThat(testPolCandidacy.getOffice()).isEqualTo(DEFAULT_OFFICE);
        assertThat(testPolCandidacy.getRegion()).isEqualTo(DEFAULT_REGION);
        assertThat(testPolCandidacy.getElectionDay()).isEqualTo(DEFAULT_ELECTION_DAY);
    }

    @Test
    @Transactional
    public void checkOfficeIsRequired() throws Exception {
        int databaseSizeBeforeTest = polCandidacyRepository.findAll().size();
        // set the field null
        polCandidacy.setOffice(null);

        // Create the PolCandidacy, which fails.

        restPolCandidacyMockMvc.perform(post("/api/polCandidacys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(polCandidacy)))
                .andExpect(status().isBadRequest());

        List<PolCandidacy> polCandidacys = polCandidacyRepository.findAll();
        assertThat(polCandidacys).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRegionIsRequired() throws Exception {
        int databaseSizeBeforeTest = polCandidacyRepository.findAll().size();
        // set the field null
        polCandidacy.setRegion(null);

        // Create the PolCandidacy, which fails.

        restPolCandidacyMockMvc.perform(post("/api/polCandidacys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(polCandidacy)))
                .andExpect(status().isBadRequest());

        List<PolCandidacy> polCandidacys = polCandidacyRepository.findAll();
        assertThat(polCandidacys).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkElectionDayIsRequired() throws Exception {
        int databaseSizeBeforeTest = polCandidacyRepository.findAll().size();
        // set the field null
        polCandidacy.setElectionDay(null);

        // Create the PolCandidacy, which fails.

        restPolCandidacyMockMvc.perform(post("/api/polCandidacys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(polCandidacy)))
                .andExpect(status().isBadRequest());

        List<PolCandidacy> polCandidacys = polCandidacyRepository.findAll();
        assertThat(polCandidacys).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPolCandidacys() throws Exception {
        // Initialize the database
        polCandidacyRepository.saveAndFlush(polCandidacy);

        // Get all the polCandidacys
        restPolCandidacyMockMvc.perform(get("/api/polCandidacys?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(polCandidacy.getId().intValue())))
                .andExpect(jsonPath("$.[*].office").value(hasItem(DEFAULT_OFFICE.toString())))
                .andExpect(jsonPath("$.[*].region").value(hasItem(DEFAULT_REGION.toString())))
                .andExpect(jsonPath("$.[*].electionDay").value(hasItem(DEFAULT_ELECTION_DAY.toString())));
    }

    @Test
    @Transactional
    public void getPolCandidacy() throws Exception {
        // Initialize the database
        polCandidacyRepository.saveAndFlush(polCandidacy);

        // Get the polCandidacy
        restPolCandidacyMockMvc.perform(get("/api/polCandidacys/{id}", polCandidacy.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(polCandidacy.getId().intValue()))
            .andExpect(jsonPath("$.office").value(DEFAULT_OFFICE.toString()))
            .andExpect(jsonPath("$.region").value(DEFAULT_REGION.toString()))
            .andExpect(jsonPath("$.electionDay").value(DEFAULT_ELECTION_DAY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPolCandidacy() throws Exception {
        // Get the polCandidacy
        restPolCandidacyMockMvc.perform(get("/api/polCandidacys/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePolCandidacy() throws Exception {
        // Initialize the database
        polCandidacyRepository.saveAndFlush(polCandidacy);

		int databaseSizeBeforeUpdate = polCandidacyRepository.findAll().size();

        // Update the polCandidacy
        polCandidacy.setOffice(UPDATED_OFFICE);
        polCandidacy.setRegion(UPDATED_REGION);
        polCandidacy.setElectionDay(UPDATED_ELECTION_DAY);

        restPolCandidacyMockMvc.perform(put("/api/polCandidacys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(polCandidacy)))
                .andExpect(status().isOk());

        // Validate the PolCandidacy in the database
        List<PolCandidacy> polCandidacys = polCandidacyRepository.findAll();
        assertThat(polCandidacys).hasSize(databaseSizeBeforeUpdate);
        PolCandidacy testPolCandidacy = polCandidacys.get(polCandidacys.size() - 1);
        assertThat(testPolCandidacy.getOffice()).isEqualTo(UPDATED_OFFICE);
        assertThat(testPolCandidacy.getRegion()).isEqualTo(UPDATED_REGION);
        assertThat(testPolCandidacy.getElectionDay()).isEqualTo(UPDATED_ELECTION_DAY);
    }

    @Test
    @Transactional
    public void deletePolCandidacy() throws Exception {
        // Initialize the database
        polCandidacyRepository.saveAndFlush(polCandidacy);

		int databaseSizeBeforeDelete = polCandidacyRepository.findAll().size();

        // Get the polCandidacy
        restPolCandidacyMockMvc.perform(delete("/api/polCandidacys/{id}", polCandidacy.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<PolCandidacy> polCandidacys = polCandidacyRepository.findAll();
        assertThat(polCandidacys).hasSize(databaseSizeBeforeDelete - 1);
    }
}
