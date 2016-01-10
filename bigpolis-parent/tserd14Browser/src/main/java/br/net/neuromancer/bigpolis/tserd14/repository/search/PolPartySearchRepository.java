package br.net.neuromancer.bigpolis.tserd14.repository.search;

import br.net.neuromancer.bigpolis.tserd14.domain.PolParty;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the PolParty entity.
 */
public interface PolPartySearchRepository extends ElasticsearchRepository<PolParty, Long> {
}
