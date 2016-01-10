package br.net.neuromancer.bigpolis.tserd14.web.rest;

import br.net.neuromancer.bigpolis.tserd14.Application;
import br.net.neuromancer.bigpolis.tserd14.domain.PolParty;
import br.net.neuromancer.bigpolis.tserd14.repository.PolPartyRepository;
import br.net.neuromancer.bigpolis.tserd14.repository.search.PolPartySearchRepository;

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
 * Test class for the PolPartyResource REST controller.
 *
 * @see PolPartyResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class PolPartyResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_ACRONYM = "AA";
    private static final String UPDATED_ACRONYM = "BB";

    @Inject
    private PolPartyRepository polPartyRepository;

    @Inject
    private PolPartySearchRepository polPartySearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPolPartyMockMvc;

    private PolParty polParty;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PolPartyResource polPartyResource = new PolPartyResource();
        ReflectionTestUtils.setField(polPartyResource, "polPartySearchRepository", polPartySearchRepository);
        ReflectionTestUtils.setField(polPartyResource, "polPartyRepository", polPartyRepository);
        this.restPolPartyMockMvc = MockMvcBuilders.standaloneSetup(polPartyResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        polParty = new PolParty();
        polParty.setName(DEFAULT_NAME);
        polParty.setAcronym(DEFAULT_ACRONYM);
    }

    @Test
    @Transactional
    public void createPolParty() throws Exception {
        int databaseSizeBeforeCreate = polPartyRepository.findAll().size();

        // Create the PolParty

        restPolPartyMockMvc.perform(post("/api/polPartys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(polParty)))
                .andExpect(status().isCreated());

        // Validate the PolParty in the database
        List<PolParty> polPartys = polPartyRepository.findAll();
        assertThat(polPartys).hasSize(databaseSizeBeforeCreate + 1);
        PolParty testPolParty = polPartys.get(polPartys.size() - 1);
        assertThat(testPolParty.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPolParty.getAcronym()).isEqualTo(DEFAULT_ACRONYM);
    }

    @Test
    @Transactional
    public void getAllPolPartys() throws Exception {
        // Initialize the database
        polPartyRepository.saveAndFlush(polParty);

        // Get all the polPartys
        restPolPartyMockMvc.perform(get("/api/polPartys?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(polParty.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].acronym").value(hasItem(DEFAULT_ACRONYM.toString())));
    }

    @Test
    @Transactional
    public void getPolParty() throws Exception {
        // Initialize the database
        polPartyRepository.saveAndFlush(polParty);

        // Get the polParty
        restPolPartyMockMvc.perform(get("/api/polPartys/{id}", polParty.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(polParty.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.acronym").value(DEFAULT_ACRONYM.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPolParty() throws Exception {
        // Get the polParty
        restPolPartyMockMvc.perform(get("/api/polPartys/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePolParty() throws Exception {
        // Initialize the database
        polPartyRepository.saveAndFlush(polParty);

		int databaseSizeBeforeUpdate = polPartyRepository.findAll().size();

        // Update the polParty
        polParty.setName(UPDATED_NAME);
        polParty.setAcronym(UPDATED_ACRONYM);

        restPolPartyMockMvc.perform(put("/api/polPartys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(polParty)))
                .andExpect(status().isOk());

        // Validate the PolParty in the database
        List<PolParty> polPartys = polPartyRepository.findAll();
        assertThat(polPartys).hasSize(databaseSizeBeforeUpdate);
        PolParty testPolParty = polPartys.get(polPartys.size() - 1);
        assertThat(testPolParty.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPolParty.getAcronym()).isEqualTo(UPDATED_ACRONYM);
    }

    @Test
    @Transactional
    public void deletePolParty() throws Exception {
        // Initialize the database
        polPartyRepository.saveAndFlush(polParty);

		int databaseSizeBeforeDelete = polPartyRepository.findAll().size();

        // Get the polParty
        restPolPartyMockMvc.perform(delete("/api/polPartys/{id}", polParty.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<PolParty> polPartys = polPartyRepository.findAll();
        assertThat(polPartys).hasSize(databaseSizeBeforeDelete - 1);
    }
}
