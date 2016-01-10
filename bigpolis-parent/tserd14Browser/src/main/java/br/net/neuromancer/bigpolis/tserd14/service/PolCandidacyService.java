package br.net.neuromancer.bigpolis.tserd14.service;

import br.net.neuromancer.bigpolis.tserd14.domain.PolCandidacy;
import br.net.neuromancer.bigpolis.tserd14.repository.PolCandidacyRepository;
import br.net.neuromancer.bigpolis.tserd14.repository.search.PolCandidacySearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing PolCandidacy.
 */
@Service
@Transactional
public class PolCandidacyService {

    private final Logger log = LoggerFactory.getLogger(PolCandidacyService.class);
    
    @Inject
    private PolCandidacyRepository polCandidacyRepository;
    
    @Inject
    private PolCandidacySearchRepository polCandidacySearchRepository;
    
    /**
     * Save a polCandidacy.
     * @return the persisted entity
     */
    public PolCandidacy save(PolCandidacy polCandidacy) {
        log.debug("Request to save PolCandidacy : {}", polCandidacy);
        PolCandidacy result = polCandidacyRepository.save(polCandidacy);
        polCandidacySearchRepository.save(result);
        return result;
    }

    /**
     *  get all the polCandidacys.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<PolCandidacy> findAll(Pageable pageable) {
        log.debug("Request to get all PolCandidacys");
        Page<PolCandidacy> result = polCandidacyRepository.findAll(pageable); 
        return result;
    }

    /**
     *  get one polCandidacy by id.
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public PolCandidacy findOne(Long id) {
        log.debug("Request to get PolCandidacy : {}", id);
        PolCandidacy polCandidacy = polCandidacyRepository.findOne(id);
        return polCandidacy;
    }

    /**
     *  delete the  polCandidacy by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete PolCandidacy : {}", id);
        polCandidacyRepository.delete(id);
        polCandidacySearchRepository.delete(id);
    }

    /**
     * search for the polCandidacy corresponding
     * to the query.
     */
    @Transactional(readOnly = true) 
    public List<PolCandidacy> search(String query) {
        
        log.debug("REST request to search PolCandidacys for query {}", query);
        return StreamSupport
            .stream(polCandidacySearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
