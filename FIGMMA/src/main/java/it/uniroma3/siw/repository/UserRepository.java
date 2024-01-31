package it.uniroma3.siw.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.model.User;

public interface UserRepository extends CrudRepository<User, Long> {
	
	 @Query("SELECT u FROM User u WHERE u.credentials.role = :role")
	    Optional<User> findAllPresidentFree(@Param("role") String role);

}
