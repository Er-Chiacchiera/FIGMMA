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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.controller.validator.AthleteValidator;
import it.uniroma3.siw.controller.validator.SiteValidator;
import it.uniroma3.siw.model.Athlete;
import it.uniroma3.siw.presentation.FileStorer;
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
	public String newAthlete(@Valid @ModelAttribute("athlete") Athlete athlete,BindingResult bindingResult , @RequestParam("file") MultipartFile file, Model model) {
		this.athleteValidator.validate(athlete, bindingResult);
		this.siteValidator.validate(athlete.getSite(),bindingResult);

		if (!bindingResult.hasErrors()) {
			this.athleteService.addNewAthlete(athlete);

			if(!file.isEmpty()) {
				System.out.println("devo salvare la foto \n\n\n\n");
				athlete.setPathImg(FileStorer.store(file,"athlete",athlete.getId()));
				this.athleteService.updateAthlete(athlete);
			}

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

	/********** Ritorna una form per modificare i dati di un atleta **********/
	@GetMapping("/edit/{id}")
	public String formEditAthlete(@PathVariable("id") Long id, Model model) {
		model.addAttribute("athlete", this.athleteService.GetAthleteById(id));
		return ATHLETE_DIR + "athleteEdit"; 
	}

	/********** Convalida i dati raccolti dalla form update **********/
	@PostMapping("/update/{id}")
	public String updateAthlete(@Valid @ModelAttribute("athlete") Athlete athlete,BindingResult bindingResult, @RequestParam("file")MultipartFile file, Model model) {
		this.athleteValidator.validate(athlete, bindingResult);
		
		if (bindingResult.hasErrors()) {
			System.out.println(bindingResult);
			return ATHLETE_DIR + "athleteEdit";
		}
		if(!file.isEmpty()) {
			athlete.setPathImg(FileStorer.store(file, "athlete",athlete.getId()));
		}
		
		this.athleteService.updateAthlete(athlete);
		return "redirect:/athlete/" + athlete.getId();
	}

	/*Cancella l'atleta dal sitema*/
	@GetMapping("/delete/{id}")
	public String deleteAthlete(@PathVariable("id") Long id, Model model) {
		this.athleteService.deleteById(id);
		return "redirect:/athlete/all";
	}

	/*Ricerco dei specifici ATLETI su dei parametri */
	@PostMapping("/search")
	public String searchathletes(@RequestParam(name = "type") String type,@RequestParam(name = "attribute", defaultValue = "") String attribute,
			Model model) {
		model.addAttribute("athletes",this.athleteService.GetAllAthletesByTypeAndAttribute(type,attribute));
		return ATHLETE_DIR + "athleteList";
	}
}