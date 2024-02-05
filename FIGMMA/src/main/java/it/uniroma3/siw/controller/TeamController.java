package it.uniroma3.siw.controller;

import java.lang.ProcessBuilder.Redirect;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import it.uniroma3.siw.controller.validator.AthleteValidator;
import it.uniroma3.siw.controller.validator.ContractValidator;
import it.uniroma3.siw.controller.validator.SiteValidator;
import it.uniroma3.siw.controller.validator.TeamValidator;
import it.uniroma3.siw.model.Contract;
import it.uniroma3.siw.model.Team;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.AthleteService;
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
	@Autowired AthleteService athleteService;
	@Autowired AthleteValidator athleteValidator;
	@Autowired ContractValidator contractValidator;

	public static final String TEAM_DIR = "team/";



	/*Form per aggiungere un nuovo Istruttore*/
	@GetMapping("/add/new")
	public String formNewTeam(Model model) {
		model.addAttribute("team", new Team());
		List<User> users=this.userService.findPresidentsWithoutTeam();
		model.addAttribute("users",users);
		return TEAM_DIR + "teamAdd";
	}

	/*Verifico se il nuovo istruttore rispetta i criteri e lo aggiungo al database altirmenti torno alla form*/
	@PostMapping("/add")
	public String newTeam(@Valid @ModelAttribute("team") Team team,BindingResult bindingResult , Model model) {
		this.teamValidator.validate(team, bindingResult);
		this.siteValidator.validateAddress(team.getSite(),bindingResult);
		System.out.println(bindingResult);
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

	/*Modifico il team*/
	@GetMapping("/edit/{id}")
	public String formEditTeam(@PathVariable("id") Long id, Model model) {
		model.addAttribute("team", this.teamService.GetTeamById(id));
		List<User> presidents =this.userService.findPresidentsWithoutTeam();
		presidents.add(this.teamService.GetTeamById(id).getPresident());
		model.addAttribute("users",presidents);
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
	

	/*Modifico gli atleti nel team*/
	@GetMapping("/editAthletes/{id}")
	public String formEditAthletesInTeam(@PathVariable("id") Long id, Model model) {
		model.addAttribute("team", this.teamService.GetTeamById(id));
		model.addAttribute("athletesFree", this.athleteService.getAllFreeAthletes());
		//List<Athlete> atleti = this.athleteService.getAllFreeAthletes();
		//System.out.println(atleti.get(0).getName());
		return TEAM_DIR + "teamEditAthletes";
	}
	
	@GetMapping("/removeAthlete/{id}")
	public String removeAthletesInTeam(@PathVariable("id") Long id, Model model) {
		this.athleteService.EndOfContract(this.athleteService.GetAthleteById(id));
		return "redirect:/athlete/" + id;
	}

	@GetMapping("/addAthlete/{idTeam}/{idAthlete}")
	public String formAddAthleteInTeam(@PathVariable("idTeam") Long idTeam,@PathVariable("idAthlete") Long idAthlete, Model model) {
		model.addAttribute("team", this.teamService.GetTeamById(idTeam));
		model.addAttribute("athlete", this.athleteService.GetAthleteById(idAthlete));
		model.addAttribute("contract",  new Contract());
		return TEAM_DIR + "teamAddAthlete";
	}

	/*Aggiungo un atleta senza contatto ad un team*/
	@PostMapping("/addAthlete/{idTeam}/{idAthlete}")
	public String addAthleteInTeam(@PathVariable("idTeam") Long idTeam, @PathVariable("idAthlete") Long idAthlete, @Valid @ModelAttribute("contract") Contract contract, BindingResult bindingResult , Model model) {
		System.out.println("Controllo le date per metterlo nel tem \n");
		this.contractValidator.validate(contract, bindingResult);
		System.out.println(bindingResult);
		System.out.println("ho fatto il bindingResult \n\n\n\n\n\n");
		if (!bindingResult.hasErrors()) {
			this.teamService.addAtleteinTeam(idTeam,idAthlete,contract);
			return "redirect:/team/editAthletes/" + idTeam;
		} 

		else {
			model.addAttribute("team", this.teamService.GetTeamById(idTeam));
			model.addAttribute("athlete", this.athleteService.GetAthleteById(idAthlete));
			return TEAM_DIR + "teamAddAthlete";
		}
	}





	/**********************************************
	ancora non implementato per rimuoverlo la team
	 **********************************************/

	/*Cancella il team dal sitema*/
	@GetMapping("/delete/{id}")
	public String deleteTeam(@PathVariable("id") Long id, Model model) {
		this.teamService.deleteById(id);
		return "redirect:/team/all";
	}
}