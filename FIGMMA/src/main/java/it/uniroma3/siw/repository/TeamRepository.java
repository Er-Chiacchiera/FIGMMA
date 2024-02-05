package it.uniroma3.siw.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Athlete;
import it.uniroma3.siw.model.Team;

public interface TeamRepository extends CrudRepository<Team, Long> {
	
	
	public List<Athlete> findByName(String surname);
	
	public boolean existsByName(String name);

	public Iterable<Team> findByNameContaining(String attribute);
	
	public Iterable<Team> findByPresidentNomeContaining(String attribute);

}
