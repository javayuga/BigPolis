package br.net.neuromancer.bigpolis.tserd14.repository.search;

import br.net.neuromancer.bigpolis.tserd14.domain.PolCandidate;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the PolCandidate entity.
 */
public interface PolCandidateSearchRepository extends ElasticsearchRepository<PolCandidate, Long> {
}
