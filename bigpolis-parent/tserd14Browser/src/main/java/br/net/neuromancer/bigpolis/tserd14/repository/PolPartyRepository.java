package br.net.neuromancer.bigpolis.tserd14.repository;

import br.net.neuromancer.bigpolis.tserd14.domain.PolParty;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the PolParty entity.
 */
public interface PolPartyRepository extends JpaRepository<PolParty,Long> {

    @Query("select distinct polParty from PolParty polParty left join fetch polParty.candidaciess")
    List<PolParty> findAllWithEagerRelationships();

    @Query("select polParty from PolParty polParty left join fetch polParty.candidaciess where polParty.id =:id")
    PolParty findOneWithEagerRelationships(@Param("id") Long id);

}
