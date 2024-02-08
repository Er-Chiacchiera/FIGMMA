package it.uniroma3.siw.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Athlete;
import it.uniroma3.siw.model.Contract;
import it.uniroma3.siw.model.Site;
import it.uniroma3.siw.model.Team;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.presentation.FileStorer;
import it.uniroma3.siw.repository.TeamRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service
public class TeamService {
	@Autowired TeamRepository teamRepository;
	@Autowired UserService userService;
	@Autowired AthleteService athleteService;

	@Transactional
	public void addNewTeam(Team team) {
		this.teamRepository.save(team);
		User president =team.getPresident();
		president.setTeam(team);
		this.userService.saveUser(president);
	}

	public Iterable<Team> GetAllTeams() {
		return this.teamRepository.findAll();

	}

	public Team GetTeamById(Long id) {
		return this.teamRepository.findById(id).get();
	}

	public void deleteById(Long id) {
		List<Athlete> athletes =this.GetTeamById(id).getAthletes();
		for(Athlete athlete:athletes)
			this.athleteService.EndOfContract(athlete);
		this.userService.removePresidentFromTeam(this.GetTeamById(id).getPresident().getId());
		if(this.GetTeamById(id).getPathImg()!=null)
			FileStorer.removeImg("team", this.GetTeamById(id).getPathImg());
		this.teamRepository.deleteById(id);
	}

	public void updateTeam(@Valid Team team) {
		System.out.println("Sono entrato nel valid\n\n\n\n");
		Team oldTeam =this.teamRepository.findById(team.getId()).get();
		oldTeam.setName(team.getName());
		System.out.println("primo step fatto\n\n\n\n");
		if(team.getPresident().getId()!=oldTeam.getPresident().getId()) {
			this.userService.changePresident(oldTeam.getPresident().getId(),team.getPresident().getId());
			oldTeam.setPresident(team.getPresident());
			System.out.println("secondo step fatto\n\n\n\n");
		}

		oldTeam.setDateOfCreation(team.getDateOfCreation());
		if(team.getPathImg()!=null)
			oldTeam.setPathImg(team.getPathImg());

		Site oldSite = oldTeam.getSite();
		oldSite.setCountry(team.getSite().getCountry());
		oldSite.setCity(team.getSite().getCity());
		oldSite.setCap(team.getSite().getCap());
		oldSite.setAddress(team.getSite().getAddress());
		this.teamRepository.save(oldTeam);
	}

	public void addAtleteinTeam(Long idTeam,  Long idAthlete,Contract contract) {
		Athlete athlete = this.athleteService.GetAthleteById(idAthlete);
		Team team = this.teamRepository.findById(idTeam).get();


		this.athleteService.addAthleteInTeam(team,idAthlete,contract);
		team.getAthletes().add(athlete);
		this.teamRepository.save(team);

	}

	@Transactional
	public void removeAthlete(Athlete athlete) {
		Team team=this.teamRepository.findById(athlete.getTeam().getId()).get();
		team.getAthletes().remove(athlete);
		this.teamRepository.save(team);

	}

	public Iterable<Team> GetAllTeamsByTypeAndAttribute(String type, String attribute) {
		if(type.equals("team"))
			return this.teamRepository.findByNameContaining(attribute);
		else
			return this.teamRepository.findByPresidentNomeContaining(attribute);

	}

}