package br.net.neuromancer.bigpolis.tserd14.web.rest;

import br.net.neuromancer.bigpolis.tserd14.Application;
import br.net.neuromancer.bigpolis.tserd14.domain.PolCandidate;
import br.net.neuromancer.bigpolis.tserd14.repository.PolCandidateRepository;
import br.net.neuromancer.bigpolis.tserd14.repository.search.PolCandidateSearchRepository;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the PolCandidateResource REST controller.
 *
 * @see PolCandidateResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class PolCandidateResourceIntTest {

    private static final String DEFAULT_FULL_NAME = "AAAAA";
    private static final String UPDATED_FULL_NAME = "BBBBB";
    private static final String DEFAULT_ALIAS = "AA";
    private static final String UPDATED_ALIAS = "BB";

    @Inject
    private PolCandidateRepository polCandidateRepository;

    @Inject
    private PolCandidateSearchRepository polCandidateSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPolCandidateMockMvc;

    private PolCandidate polCandidate;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PolCandidateResource polCandidateResource = new PolCandidateResource();
        ReflectionTestUtils.setField(polCandidateResource, "polCandidateSearchRepository", polCandidateSearchRepository);
        ReflectionTestUtils.setField(polCandidateResource, "polCandidateRepository", polCandidateRepository);
        this.restPolCandidateMockMvc = MockMvcBuilders.standaloneSetup(polCandidateResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        polCandidate = new PolCandidate();
        polCandidate.setFullName(DEFAULT_FULL_NAME);
        polCandidate.setAlias(DEFAULT_ALIAS);
    }

    @Test
    @Transactional
    public void createPolCandidate() throws Exception {
        int databaseSizeBeforeCreate = polCandidateRepository.findAll().size();

        // Create the PolCandidate

        restPolCandidateMockMvc.perform(post("/api/polCandidates")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(polCandidate)))
                .andExpect(status().isCreated());

        // Validate the PolCandidate in the database
        List<PolCandidate> polCandidates = polCandidateRepository.findAll();
        assertThat(polCandidates).hasSize(databaseSizeBeforeCreate + 1);
        PolCandidate testPolCandidate = polCandidates.get(polCandidates.size() - 1);
        assertThat(testPolCandidate.getFullName()).isEqualTo(DEFAULT_FULL_NAME);
        assertThat(testPolCandidate.getAlias()).isEqualTo(DEFAULT_ALIAS);
    }

    @Test
    @Transactional
    public void getAllPolCandidates() throws Exception {
        // Initialize the database
        polCandidateRepository.saveAndFlush(polCandidate);

        // Get all the polCandidates
        restPolCandidateMockMvc.perform(get("/api/polCandidates?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(polCandidate.getId().intValue())))
                .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME.toString())))
                .andExpect(jsonPath("$.[*].alias").value(hasItem(DEFAULT_ALIAS.toString())));
    }

    @Test
    @Transactional
    public void getPolCandidate() throws Exception {
        // Initialize the database
        polCandidateRepository.saveAndFlush(polCandidate);

        // Get the polCandidate
        restPolCandidateMockMvc.perform(get("/api/polCandidates/{id}", polCandidate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(polCandidate.getId().intValue()))
            .andExpect(jsonPath("$.fullName").value(DEFAULT_FULL_NAME.toString()))
            .andExpect(jsonPath("$.alias").value(DEFAULT_ALIAS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPolCandidate() throws Exception {
        // Get the polCandidate
        restPolCandidateMockMvc.perform(get("/api/polCandidates/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePolCandidate() throws Exception {
        // Initialize the database
        polCandidateRepository.saveAndFlush(polCandidate);

		int databaseSizeBeforeUpdate = polCandidateRepository.findAll().size();

        // Update the polCandidate
        polCandidate.setFullName(UPDATED_FULL_NAME);
        polCandidate.setAlias(UPDATED_ALIAS);

        restPolCandidateMockMvc.perform(put("/api/polCandidates")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(polCandidate)))
                .andExpect(status().isOk());

        // Validate the PolCandidate in the database
        List<PolCandidate> polCandidates = polCandidateRepository.findAll();
        assertThat(polCandidates).hasSize(databaseSizeBeforeUpdate);
        PolCandidate testPolCandidate = polCandidates.get(polCandidates.size() - 1);
        assertThat(testPolCandidate.getFullName()).isEqualTo(UPDATED_FULL_NAME);
        assertThat(testPolCandidate.getAlias()).isEqualTo(UPDATED_ALIAS);
    }

    @Test
    @Transactional
    public void deletePolCandidate() throws Exception {
        // Initialize the database
        polCandidateRepository.saveAndFlush(polCandidate);

		int databaseSizeBeforeDelete = polCandidateRepository.findAll().size();

        // Get the polCandidate
        restPolCandidateMockMvc.perform(delete("/api/polCandidates/{id}", polCandidate.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<PolCandidate> polCandidates = polCandidateRepository.findAll();
        assertThat(polCandidates).hasSize(databaseSizeBeforeDelete - 1);
    }
}
