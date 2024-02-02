package it.uniroma3.siw.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Athlete;

public interface AthleteRepository extends CrudRepository<Athlete, Long> {
	
	public boolean existsByNameAndSurnameAndDateOfBirth(String Name, String surname, LocalDate dateOfBirth);
	
	public List<Athlete> findBySurname(String surname);
	
	@Query("SELECT a FROM Athlete a WHERE a.startOfMembership IS NULL")
    List<Athlete> findAllFreeAthletes();
	
	public List<Athlete> findByName(String name);


}
