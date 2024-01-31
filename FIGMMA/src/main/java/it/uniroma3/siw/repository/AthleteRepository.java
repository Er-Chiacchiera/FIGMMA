package it.uniroma3.siw.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Athlete;

public interface AthleteRepository extends CrudRepository<Athlete, Long> {
	
	public boolean existsByNameAndSurnameAndDateOfBirth(String Name, String surname, LocalDate dateOfBirth);
	
	public List<Athlete> findBySurname(String surname);
	
	public List<Athlete> findByName(String name);


}
