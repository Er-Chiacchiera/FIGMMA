package it.uniroma3.siw.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.User;

public interface UserRepository extends CrudRepository<User, Long> {
	
	@Query("SELECT u FROM User u JOIN u.credentials c WHERE c.role = 'PRESIDENT' AND u.team IS NULL")
    List<User> findPresidentsWithoutTeam();

}
