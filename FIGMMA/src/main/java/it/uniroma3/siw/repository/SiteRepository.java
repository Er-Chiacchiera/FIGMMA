package it.uniroma3.siw.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Site;

public interface SiteRepository extends CrudRepository<Site, Long> {

	boolean existsByCountryAndCityAndCap(String country, String city, String cap);
	Optional<Site> findByCountryAndCityAndCap(String country, String city, String cap);

}
