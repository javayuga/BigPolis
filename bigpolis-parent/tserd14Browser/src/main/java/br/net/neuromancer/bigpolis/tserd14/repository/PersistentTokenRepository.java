package br.net.neuromancer.bigpolis.tserd14.repository;

import br.net.neuromancer.bigpolis.tserd14.domain.PersistentToken;
import br.net.neuromancer.bigpolis.tserd14.domain.User;
import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository for the PersistentToken entity.
 */
public interface PersistentTokenRepository extends JpaRepository<PersistentToken, String> {

    List<PersistentToken> findByUser(User user);

    List<PersistentToken> findByTokenDateBefore(LocalDate localDate);

}
