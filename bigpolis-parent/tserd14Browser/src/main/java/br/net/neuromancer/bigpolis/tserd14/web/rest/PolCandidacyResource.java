package br.net.neuromancer.bigpolis.tserd14.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.net.neuromancer.bigpolis.tserd14.domain.PolCandidacy;
import br.net.neuromancer.bigpolis.tserd14.service.PolCandidacyService;
import br.net.neuromancer.bigpolis.tserd14.web.rest.util.HeaderUtil;
import br.net.neuromancer.bigpolis.tserd14.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing PolCandidacy.
 */
@RestController
@RequestMapping("/api")
public class PolCandidacyResource {

    private final Logger log = LoggerFactory.getLogger(PolCandidacyResource.class);
        
    @Inject
    private PolCandidacyService polCandidacyService;
    
    /**
     * POST  /polCandidacys -> Create a new polCandidacy.
     */
    @RequestMapping(value = "/polCandidacys",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PolCandidacy> createPolCandidacy(@Valid @RequestBody PolCandidacy polCandidacy) throws URISyntaxException {
        log.debug("REST request to save PolCandidacy : {}", polCandidacy);
        if (polCandidacy.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("polCandidacy", "idexists", "A new polCandidacy cannot already have an ID")).body(null);
        }
        PolCandidacy result = polCandidacyService.save(polCandidacy);
        return ResponseEntity.created(new URI("/api/polCandidacys/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("polCandidacy", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /polCandidacys -> Updates an existing polCandidacy.
     */
    @RequestMapping(value = "/polCandidacys",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PolCandidacy> updatePolCandidacy(@Valid @RequestBody PolCandidacy polCandidacy) throws URISyntaxException {
        log.debug("REST request to update PolCandidacy : {}", polCandidacy);
        if (polCandidacy.getId() == null) {
            return createPolCandidacy(polCandidacy);
        }
        PolCandidacy result = polCandidacyService.save(polCandidacy);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("polCandidacy", polCandidacy.getId().toString()))
            .body(result);
    }

    /**
     * GET  /polCandidacys -> get all the polCandidacys.
     */
    @RequestMapping(value = "/polCandidacys",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<PolCandidacy>> getAllPolCandidacys(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of PolCandidacys");
        Page<PolCandidacy> page = polCandidacyService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/polCandidacys");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /polCandidacys/:id -> get the "id" polCandidacy.
     */
    @RequestMapping(value = "/polCandidacys/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PolCandidacy> getPolCandidacy(@PathVariable Long id) {
        log.debug("REST request to get PolCandidacy : {}", id);
        PolCandidacy polCandidacy = polCandidacyService.findOne(id);
        return Optional.ofNullable(polCandidacy)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /polCandidacys/:id -> delete the "id" polCandidacy.
     */
    @RequestMapping(value = "/polCandidacys/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePolCandidacy(@PathVariable Long id) {
        log.debug("REST request to delete PolCandidacy : {}", id);
        polCandidacyService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("polCandidacy", id.toString())).build();
    }

    /**
     * SEARCH  /_search/polCandidacys/:query -> search for the polCandidacy corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/polCandidacys/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<PolCandidacy> searchPolCandidacys(@PathVariable String query) {
        log.debug("Request to search PolCandidacys for query {}", query);
        return polCandidacyService.search(query);
    }
}
