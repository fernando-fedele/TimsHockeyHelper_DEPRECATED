package ca.fedelef.repositories;

import org.springframework.data.repository.CrudRepository;

import ca.fedelef.model.Player;

public interface PlayerRepository extends CrudRepository<Player, Integer> {

}
