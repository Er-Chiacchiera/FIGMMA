package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import it.uniroma3.siw.controller.validator.AthleteValidator;
import it.uniroma3.siw.controller.validator.SiteValidator;
import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.CredentialsRepository;
import it.uniroma3.siw.service.CredentialsService;

@Controller
@RequestMapping(value = {"/user", "/genericUser", "/president"})
public class UserController {
	@Autowired AthleteValidator athleteValidator;
	@Autowired CredentialsService credentialsService;
	@Autowired SiteValidator siteValidator;
	@Autowired CredentialsRepository credentialsRepository;

	public static final String GENERIC_USER_DIR = "user/genericUser/";
	public static final String PRESIDENT_DIR = "user/president/";
	public static final String USER_DIR = "user/";



	/*Mostra la pagina dell'utente(diversa se l'utente è un presidente)*/
	@GetMapping("/profile")
	public String getPersonalProfile( Model model) {
		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
		User user=credentials.getUser();
		if (credentials.getRole().equals(Credentials.PRESIDENT_ROLE)) {
			model.addAttribute("president", user);
			return PRESIDENT_DIR + "presidentProfile";
		}
		else {
			model.addAttribute("user", user);
			return GENERIC_USER_DIR +"genericUserProfile";
		}
	}

}