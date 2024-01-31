package it.uniroma3.siw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Team;
import it.uniroma3.siw.model.Site;
import it.uniroma3.siw.repository.TeamRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service
public class TeamService {
	@Autowired TeamRepository teamRepository;

	@Transactional
	public void addNewTeam(Team team) {
		this.teamRepository.save(team);
	}

	public Iterable<Team> GetAllTeams() {
		return this.teamRepository.findAll();
		
	}

	public Team GetTeamById(Long id) {
		// TODO Auto-generated method stub
		return this.teamRepository.findById(id).get();
	}

	public void deleteById(Long id) {
		this.teamRepository.deleteById(id);	
	}

	public void updateTeam(@Valid Team team) {
		
		Team oldTeam =this.teamRepository.findById(team.getId()).get();
		oldTeam.setName(team.getName());
		oldTeam.setDateOfCreation(team.getDateOfCreation());
		Site oldSite = oldTeam.getSite();
		oldSite.setCountry(team.getSite().getCountry());
		oldSite.setCity(team.getSite().getCity());
		oldSite.setCap(team.getSite().getCap());
		oldSite.setAddress(team.getSite().getAddress());
		oldTeam.setSite(oldSite);
		this.teamRepository.save(oldTeam);
		
		
	}
	
	

}