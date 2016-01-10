package br.net.neuromancer.bigpolis.tserd14.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.net.neuromancer.bigpolis.tserd14.domain.PolParty;
import br.net.neuromancer.bigpolis.tserd14.repository.PolPartyRepository;
import br.net.neuromancer.bigpolis.tserd14.repository.search.PolPartySearchRepository;
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
 * REST controller for managing PolParty.
 */
@RestController
@RequestMapping("/api")
public class PolPartyResource {

    private final Logger log = LoggerFactory.getLogger(PolPartyResource.class);
        
    @Inject
    private PolPartyRepository polPartyRepository;
    
    @Inject
    private PolPartySearchRepository polPartySearchRepository;
    
    /**
     * POST  /polPartys -> Create a new polParty.
     */
    @RequestMapping(value = "/polPartys",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PolParty> createPolParty(@Valid @RequestBody PolParty polParty) throws URISyntaxException {
        log.debug("REST request to save PolParty : {}", polParty);
        if (polParty.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("polParty", "idexists", "A new polParty cannot already have an ID")).body(null);
        }
        PolParty result = polPartyRepository.save(polParty);
        polPartySearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/polPartys/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("polParty", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /polPartys -> Updates an existing polParty.
     */
    @RequestMapping(value = "/polPartys",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PolParty> updatePolParty(@Valid @RequestBody PolParty polParty) throws URISyntaxException {
        log.debug("REST request to update PolParty : {}", polParty);
        if (polParty.getId() == null) {
            return createPolParty(polParty);
        }
        PolParty result = polPartyRepository.save(polParty);
        polPartySearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("polParty", polParty.getId().toString()))
            .body(result);
    }

    /**
     * GET  /polPartys -> get all the polPartys.
     */
    @RequestMapping(value = "/polPartys",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<PolParty>> getAllPolPartys(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of PolPartys");
        Page<PolParty> page = polPartyRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/polPartys");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /polPartys/:id -> get the "id" polParty.
     */
    @RequestMapping(value = "/polPartys/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PolParty> getPolParty(@PathVariable Long id) {
        log.debug("REST request to get PolParty : {}", id);
        PolParty polParty = polPartyRepository.findOneWithEagerRelationships(id);
        return Optional.ofNullable(polParty)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /polPartys/:id -> delete the "id" polParty.
     */
    @RequestMapping(value = "/polPartys/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePolParty(@PathVariable Long id) {
        log.debug("REST request to delete PolParty : {}", id);
        polPartyRepository.delete(id);
        polPartySearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("polParty", id.toString())).build();
    }

    /**
     * SEARCH  /_search/polPartys/:query -> search for the polParty corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/polPartys/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<PolParty> searchPolPartys(@PathVariable String query) {
        log.debug("REST request to search PolPartys for query {}", query);
        return StreamSupport
            .stream(polPartySearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
