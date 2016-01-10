package br.net.neuromancer.bigpolis.tserd14.repository;

import br.net.neuromancer.bigpolis.tserd14.domain.PolCandidate;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the PolCandidate entity.
 */
public interface PolCandidateRepository extends JpaRepository<PolCandidate,Long> {

    @Query("select distinct polCandidate from PolCandidate polCandidate left join fetch polCandidate.candidaciess")
    List<PolCandidate> findAllWithEagerRelationships();

    @Query("select polCandidate from PolCandidate polCandidate left join fetch polCandidate.candidaciess where polCandidate.id =:id")
    PolCandidate findOneWithEagerRelationships(@Param("id") Long id);

}
