package it.uniroma3.siw.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Athlete;
import it.uniroma3.siw.model.Contract;
import it.uniroma3.siw.model.Site;
import it.uniroma3.siw.model.Team;
import it.uniroma3.siw.repository.AthleteRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service
public class AthleteService {
	@Autowired AthleteRepository athleteRepository;
	@Autowired TeamService teamService;

	@Transactional
	public void addNewAthlete(Athlete athlete) {
		this.athleteRepository.save(athlete);
	}

	public Iterable<Athlete> GetAllAthletes() {
		return this.athleteRepository.findAll();

	}

	public Athlete GetAthleteById(Long id) {
		// TODO Auto-generated method stub
		return this.athleteRepository.findById(id).get();
	}

	public void deleteById(Long id) {
		this.athleteRepository.deleteById(id);	
	}

	public void updateAthlete(@Valid Athlete athlete) {

		Athlete oldAthlete =this.athleteRepository.findById(athlete.getId()).get();
		oldAthlete.setName(athlete.getName());
		oldAthlete.setSurname(athlete.getSurname());
		oldAthlete.setDateOfBirth(athlete.getDateOfBirth());

		Site oldSite = oldAthlete.getSite();
		oldSite.setCountry(athlete.getSite().getCountry());
		oldSite.setCity(athlete.getSite().getCity());
		oldSite.setCap(athlete.getSite().getCap());
		oldAthlete.setSite(oldSite);
		this.athleteRepository.save(oldAthlete);


	}

	public List<Athlete> getAllFreeAthletes() {
		return this.athleteRepository.findAllFreeAthletes();

	}

	public void addAthleteInTeam(Team team, Long idAthlete,Contract contract) {
		Athlete a =this.athleteRepository.findById(idAthlete).get();
		a.setStartOfMembership(contract.getStartOfMembership());
		a.setEndOfMembership(contract.getEndOfMembership());
		a.setTeam(team);
		this.athleteRepository.save(a);
		return;
	}

	public void checkAllContract() {
		List<Athlete> athletes= this.athleteRepository.findAthletesWithExpiredMembership(LocalDate.now());
		for(Athlete athlete: athletes)
			this.EndOfContract(athlete);
	}

	public void EndOfContract( Athlete athlete) {
		athlete.setEndOfMembership(null);
		athlete.setStartOfMembership(null);
		this.teamService.removeAthlete(athlete,athlete.getTeam().getId());
		athlete.setTeam(null);
		this.athleteRepository.save(athlete);

	}



}