package it.uniroma3.siw.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.controller.validator.AthleteValidator;
import it.uniroma3.siw.controller.validator.ContractValidator;
import it.uniroma3.siw.controller.validator.SiteValidator;
import it.uniroma3.siw.controller.validator.TeamValidator;
import it.uniroma3.siw.model.Contract;
import it.uniroma3.siw.model.Team;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.presentation.FileStorer;
import it.uniroma3.siw.service.AthleteService;
import it.uniroma3.siw.service.TeamService;
import it.uniroma3.siw.service.UserService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/team")
public class TeamController {
	@Autowired
	TeamValidator teamValidator;
	@Autowired
	TeamService teamService;
	@Autowired
	SiteValidator siteValidator;
	@Autowired
	UserService userService;
	@Autowired
	AthleteService athleteService;
	@Autowired
	AthleteValidator athleteValidator;
	@Autowired
	ContractValidator contractValidator;

	public static final String TEAM_DIR = "team/";

	/* restituisco una form per aggiungere un nuovo team */
	@GetMapping("/add/new")
	public String formNewTeam(Model model) {
		model.addAttribute("team", new Team());
		List<User> users = this.userService.findPresidentsWithoutTeam();
		model.addAttribute("users", users);
		return TEAM_DIR + "teamAdd";
	}

	/*
	 * Verifico se il nuovo team rispetta i criteri e lo aggiungo al database
	 * altirmenti torno alla form
	 */
	@PostMapping("/add")
	public String newTeam(@Valid @ModelAttribute("team") Team team, BindingResult bindingResult, @RequestParam("file") MultipartFile file, Model model) {
		this.teamValidator.validate(team, bindingResult);
		this.siteValidator.validateAddress(team.getSite(), bindingResult);
		System.out.println(bindingResult);
		if (!bindingResult.hasErrors()) {
			this.teamService.addNewTeam(team);

			if(!file.isEmpty()) {
				System.out.println("devo salvare la foto \n\n\n\n");
				team.setPathImg(FileStorer.store(file,"team",team.getId()));
				this.teamService.updateTeam(team);
			}

			model.addAttribute("team", team);
			return TEAM_DIR + "teamProfile";
		} else {
			List<User> users = this.userService.findPresidentsWithoutTeam();
			model.addAttribute("users", users);
			return TEAM_DIR + "teamAdd";
		}
	}

	/* Mostra la lista di tutti i team */
	@GetMapping("/all")
	public String getTeams(Model model) {
		model.addAttribute("teams", this.teamService.GetAllTeams());
		return TEAM_DIR + "teamList";
	}

	/* Mostra la pagina del team */
	@GetMapping("/{id}")
	public String getTeam(@PathVariable("id") Long id, Model model) {
		model.addAttribute("team", this.teamService.GetTeamById(id));
		return TEAM_DIR + "teamProfile";
	}

	/* Restituisco una form per modificare il team */
	@GetMapping("/edit/{id}")
	public String formEditTeam(@PathVariable("id") Long id, Model model) {
		model.addAttribute("team", this.teamService.GetTeamById(id));
		List<User> presidents = this.userService.findPresidentsWithoutTeam();
		presidents.add(this.teamService.GetTeamById(id).getPresident());
		model.addAttribute("users", presidents);
		return TEAM_DIR + "teamEdit";
	}

	/* Aggiorno tutte le informazioni relative al team */
	@PostMapping("/update/{id}")
	public String updateTeam(@Valid @ModelAttribute("team") Team team, BindingResult bindingResult,@PathVariable("id") Long id ,Model model) {
		this.teamValidator.validate(team, bindingResult);
		if (bindingResult.hasErrors()) {
			System.out.println(bindingResult);
			List<User> presidents = this.userService.findPresidentsWithoutTeam();
			presidents.add(this.teamService.GetTeamById(id).getPresident());
			model.addAttribute("users", presidents);
			return TEAM_DIR + "teamEdit";
		}
		this.teamService.updateTeam(team);
		return "redirect:/team/" + team.getId();
	}

	/* Modifico gli atleti nel team */
	@GetMapping("/editAthletes/{id}")
	public String formEditAthletesInTeam(@PathVariable("id") Long id, Model model) {
		model.addAttribute("team", this.teamService.GetTeamById(id));
		model.addAttribute("athletesFree", this.athleteService.getAllFreeAthletes());
		// List<Athlete> atleti = this.athleteService.getAllFreeAthletes();
		// System.out.println(atleti.get(0).getName());
		return TEAM_DIR + "teamEditAthletes";
	}

	/* Rimuove l'atleta dal team */
	@GetMapping("/removeAthlete/{id}")
	public String removeAthletesInTeam(@PathVariable("id") Long id, Model model) {
		this.athleteService.EndOfContract(this.athleteService.GetAthleteById(id));
		return "redirect:/athlete/" + id;
	}

	/*
	 * Restituisco un form per inserire le date di inizio e fine contratto per
	 * l'ateta nel team
	 */
	@GetMapping("/addAthlete/{idTeam}/{idAthlete}")
	public String formAddAthleteInTeam(@PathVariable("idTeam") Long idTeam, @PathVariable("idAthlete") Long idAthlete,
			Model model) {
		model.addAttribute("team", this.teamService.GetTeamById(idTeam));
		model.addAttribute("athlete", this.athleteService.GetAthleteById(idAthlete));
		model.addAttribute("contract", new Contract());
		return TEAM_DIR + "teamAddAthlete";
	}

	/* Aggiungo un atleta senza contatto ad un team */
	@PostMapping("/addAthlete/{idTeam}/{idAthlete}")
	public String addAthleteInTeam(@PathVariable("idTeam") Long idTeam, @PathVariable("idAthlete") Long idAthlete,
			@Valid @ModelAttribute("contract") Contract contract, BindingResult bindingResult, Model model) {
		this.contractValidator.validate(contract, bindingResult);
		System.out.println(bindingResult);
		if (!bindingResult.hasErrors()) {
			this.teamService.addAtleteinTeam(idTeam, idAthlete, contract);
			return "redirect:/team/editAthletes/" + idTeam;
		}

		else {
			model.addAttribute("team", this.teamService.GetTeamById(idTeam));
			model.addAttribute("athlete", this.athleteService.GetAthleteById(idAthlete));
			return TEAM_DIR + "teamAddAthlete";
		}
	}

	/* Cancella il team dal sitema */
	@GetMapping("/delete/{id}")
	public String deleteTeam(@PathVariable("id") Long id, Model model) {
		this.teamService.deleteById(id);
		return "redirect:/team/all";
	}

	/* Ricerco dei specifici team su dei parametri */
	@PostMapping("/search")
	public String searchTeams(@RequestParam(name = "type") String type,
			@RequestParam(name = "attribute", defaultValue = "") String attribute,Model model) {
		model.addAttribute("teams", this.teamService.GetAllTeamsByTypeAndAttribute(type, attribute));
		return TEAM_DIR + "teamList";
	}
}