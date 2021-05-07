package ca.fedelef.repositories;

import org.springframework.data.repository.CrudRepository;
import ca.fedelef.model.Team;

public interface TeamRepository extends CrudRepository<Team, Integer> {

}
