package it.uniroma3.siw.controller;

import org.antlr.v4.runtime.atn.ErrorInfo;
import org.hibernate.query.sqm.ComparisonOperator;
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
import it.uniroma3.siw.controller.validator.SiteValidator;
import it.uniroma3.siw.model.Athlete;
import it.uniroma3.siw.service.AthleteService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/athlete")
public class AthleteController {
	@Autowired AthleteValidator athleteValidator;
	@Autowired AthleteService athleteService;
	@Autowired SiteValidator siteValidator;
	
	public static final String ATHLETE_DIR = "athlete/";


	
	/*Form per aggiungere un nuovo Istruttore*/
	@GetMapping("/add/new")
	public String formNewAthlete(Model model) {
		model.addAttribute("athlete", new Athlete());
		return ATHLETE_DIR + "athleteAdd";
	}
	
	/*Verifico se il nuovo istruttore rispetta i criteri e lo aggiungo al database altirmenti torno alla form*/
	@PostMapping("/add")
	public String newAthlete(@Valid @ModelAttribute("athlete") Athlete athlete,BindingResult bindingResult , Model model) {
	this.athleteValidator.validate(athlete, bindingResult);
	this.siteValidator.validate(athlete.getSite(),bindingResult);
		if (!bindingResult.hasErrors()) {
			this.athleteService.addNewAthlete(athlete);
			model.addAttribute("athlete", athlete);
			return ATHLETE_DIR + "athleteProfile";
		} 
		else {
			return ATHLETE_DIR + "athleteAdd";
		}
	}
	
	/*Mostra la lista di tutti gli atleti*/
	@GetMapping("/all")
	public String getAthletes(Model model) {
		model.addAttribute("athletes", this.athleteService.GetAllAthletes());
		return  ATHLETE_DIR + "athleteList";
	}
	
	/*Mostra la pagina delll'atleta*/
	@GetMapping("/{id}")
	public String getAthlete(@PathVariable("id") Long id, Model model) {
		model.addAttribute("athlete", this.athleteService.GetAthleteById(id));
		return ATHLETE_DIR + "athleteProfile";
	}
	
	
	@GetMapping("/edit/{id}")
	public String formEditAthlete(@PathVariable("id") Long id, Model model) {
		model.addAttribute("athlete", this.athleteService.GetAthleteById(id));
		return ATHLETE_DIR + "athleteEdit"; 
	}
	
	 @PostMapping("/update")
	    public String updateAthlete(@Valid @ModelAttribute("athlete") Athlete athlete, BindingResult bindingResult, Model model) {
	        if (bindingResult.hasErrors()) {
	        	System.out.println("sono negli errori");
	        	System.out.println(bindingResult);
	        	return ATHLETE_DIR + "athleteEdit";
	        }
	        this.athleteService.updateAthlete(athlete);
	        return "redirect:/athlete/" + athlete.getId();
	    }
	
	/**********************************************
	ancora non implementato per rimuoverlo la team
	**********************************************/
	
	/*Cancella l'atleta dal sitema*/
	  @GetMapping("/delete/{id}")
		public String deleteAthlete(@PathVariable("id") Long id, Model model) {
			this.athleteService.deleteById(id);
			return "redirect:/instructor/all";
		}
}