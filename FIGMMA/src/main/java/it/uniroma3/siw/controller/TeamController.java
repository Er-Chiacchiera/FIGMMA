package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import it.uniroma3.siw.controller.validator.TeamValidator;
import it.uniroma3.siw.controller.validator.SiteValidator;
import it.uniroma3.siw.model.Team;
import it.uniroma3.siw.service.TeamService;
import it.uniroma3.siw.service.UserService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/team")
public class TeamController {
	@Autowired TeamValidator teamValidator;
	@Autowired TeamService teamService;
	@Autowired SiteValidator siteValidator;
	@Autowired UserService userService;
	
	public static final String TEAM_DIR = "team/";


	
	/*Form per aggiungere un nuovo Istruttore*/
	@GetMapping("/add/new")
	public String formNewTeam(Model model) {
		model.addAttribute("team", new Team());
		model.addAttribute("presidents",this.userService.getAllPresidentHaveNotTeam());
		return TEAM_DIR + "teamAdd";
	}
	
	/*Verifico se il nuovo istruttore rispetta i criteri e lo aggiungo al database altirmenti torno alla form*/
	@PostMapping("/add")
	public String newTeam(@Valid @ModelAttribute("team") Team team,BindingResult bindingResult , Model model) {
	this.teamValidator.validate(team, bindingResult);
	this.siteValidator.validateAddress(team.getSite(),bindingResult);
		if (!bindingResult.hasErrors()) {
			this.teamService.addNewTeam(team);
			model.addAttribute("team", team);
			return TEAM_DIR + "teamProfile";
		} 
		else {
			return TEAM_DIR + "teamAdd";
		}
	}
	
	/*Mostra la lista di tutti gli atleti*/
	@GetMapping("/all")
	public String getTeams(Model model) {
		model.addAttribute("teams", this.teamService.GetAllTeams());
		return  TEAM_DIR + "teamList";
	}
	
	/*Mostra la pagina delll'atleta*/
	@GetMapping("/{id}")
	public String getTeam(@PathVariable("id") Long id, Model model) {
		model.addAttribute("team", this.teamService.GetTeamById(id));
		return TEAM_DIR + "teamProfile";
	}
	
	
	@GetMapping("/edit/{id}")
	public String formEditTeam(@PathVariable("id") Long id, Model model) {
		model.addAttribute("team", this.teamService.GetTeamById(id));
		return TEAM_DIR + "teamEdit"; 
	}
	
	 @PostMapping("/update/{id}")
	    public String updateTeam(@Valid @ModelAttribute("team") Team team, BindingResult bindingResult, Model model) {
	        if (bindingResult.hasErrors()) {
	        	System.out.println("sono negli errori");
	        	System.out.println(bindingResult);
	        	return TEAM_DIR + "teamEdit";
	        }
	        System.out.println("non sono negli errori!!!!!!!!!\n");
	        this.teamService.updateTeam(team);
	        return "redirect:/team/" + team.getId();
	    }
	
	/**********************************************
	ancora non implementato per rimuoverlo la team
	**********************************************/
	
	/*Cancella l'atleta dal sitema*/
	  @GetMapping("/delete/{id}")
		public String deleteTeam(@PathVariable("id") Long id, Model model) {
			this.teamService.deleteById(id);
			return "redirect:/instructor/all";
		}
}