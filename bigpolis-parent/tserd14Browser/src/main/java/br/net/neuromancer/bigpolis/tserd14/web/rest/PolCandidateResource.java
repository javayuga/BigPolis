package br.net.neuromancer.bigpolis.tserd14.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.net.neuromancer.bigpolis.tserd14.domain.PolCandidate;
import br.net.neuromancer.bigpolis.tserd14.repository.PolCandidateRepository;
import br.net.neuromancer.bigpolis.tserd14.repository.search.PolCandidateSearchRepository;
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
 * REST controller for managing PolCandidate.
 */
@RestController
@RequestMapping("/api")
public class PolCandidateResource {

    private final Logger log = LoggerFactory.getLogger(PolCandidateResource.class);
        
    @Inject
    private PolCandidateRepository polCandidateRepository;
    
    @Inject
    private PolCandidateSearchRepository polCandidateSearchRepository;
    
    /**
     * POST  /polCandidates -> Create a new polCandidate.
     */
    @RequestMapping(value = "/polCandidates",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PolCandidate> createPolCandidate(@Valid @RequestBody PolCandidate polCandidate) throws URISyntaxException {
        log.debug("REST request to save PolCandidate : {}", polCandidate);
        if (polCandidate.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("polCandidate", "idexists", "A new polCandidate cannot already have an ID")).body(null);
        }
        PolCandidate result = polCandidateRepository.save(polCandidate);
        polCandidateSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/polCandidates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("polCandidate", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /polCandidates -> Updates an existing polCandidate.
     */
    @RequestMapping(value = "/polCandidates",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PolCandidate> updatePolCandidate(@Valid @RequestBody PolCandidate polCandidate) throws URISyntaxException {
        log.debug("REST request to update PolCandidate : {}", polCandidate);
        if (polCandidate.getId() == null) {
            return createPolCandidate(polCandidate);
        }
        PolCandidate result = polCandidateRepository.save(polCandidate);
        polCandidateSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("polCandidate", polCandidate.getId().toString()))
            .body(result);
    }

    /**
     * GET  /polCandidates -> get all the polCandidates.
     */
    @RequestMapping(value = "/polCandidates",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<PolCandidate>> getAllPolCandidates(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of PolCandidates");
        Page<PolCandidate> page = polCandidateRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/polCandidates");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /polCandidates/:id -> get the "id" polCandidate.
     */
    @RequestMapping(value = "/polCandidates/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PolCandidate> getPolCandidate(@PathVariable Long id) {
        log.debug("REST request to get PolCandidate : {}", id);
        PolCandidate polCandidate = polCandidateRepository.findOneWithEagerRelationships(id);
        return Optional.ofNullable(polCandidate)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /polCandidates/:id -> delete the "id" polCandidate.
     */
    @RequestMapping(value = "/polCandidates/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePolCandidate(@PathVariable Long id) {
        log.debug("REST request to delete PolCandidate : {}", id);
        polCandidateRepository.delete(id);
        polCandidateSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("polCandidate", id.toString())).build();
    }

    /**
     * SEARCH  /_search/polCandidates/:query -> search for the polCandidate corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/polCandidates/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<PolCandidate> searchPolCandidates(@PathVariable String query) {
        log.debug("REST request to search PolCandidates for query {}", query);
        return StreamSupport
            .stream(polCandidateSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
