package br.net.neuromancer.bigpolis.tserd14.repository;

import br.net.neuromancer.bigpolis.tserd14.domain.PolCandidacy;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the PolCandidacy entity.
 */
public interface PolCandidacyRepository extends JpaRepository<PolCandidacy,Long> {

}
