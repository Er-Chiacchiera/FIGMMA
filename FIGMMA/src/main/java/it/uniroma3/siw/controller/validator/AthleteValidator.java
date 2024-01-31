package it.uniroma3.siw.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Athlete;
import it.uniroma3.siw.repository.AthleteRepository;

@Component
public class AthleteValidator implements Validator {
	@Autowired
	private AthleteRepository athleteRepository;
	
	@Override
	public void validate(Object o, Errors errors) {
		Athlete athlete = (Athlete)o;
		if (athlete.getName()!=null && athlete.getSurname()!=null && athlete.getDateOfBirth()!=null && athleteRepository.existsByNameAndSurnameAndDateOfBirth(athlete.getName(), athlete.getSurname(),athlete.getDateOfBirth())) {
			errors.reject("athlete.duplicate");
		}
		
	}

	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return false;
	}

}
