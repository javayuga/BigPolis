package br.net.neuromancer.bigpolis.tserd14.repository.search;

import br.net.neuromancer.bigpolis.tserd14.domain.PolCandidacy;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the PolCandidacy entity.
 */
public interface PolCandidacySearchRepository extends ElasticsearchRepository<PolCandidacy, Long> {
}
