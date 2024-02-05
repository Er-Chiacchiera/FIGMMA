package it.uniroma3.siw.controller.validator;

import java.time.LocalDate;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Contract;

@Component
public class ContractValidator implements Validator {
			
	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void validate(Object o, Errors errors) {
		System.out.println("Inizio a vedere il binding di contract\n\n\n");
		Contract contract =(Contract)o;
		LocalDate startDate =contract.getStartOfMembership();
		LocalDate endDate=contract.getEndOfMembership();
		System.out.println("Fatti tutti i get\n\n\n");
		if (startDate.isBefore(LocalDate.now())) {
			errors.rejectValue("startOfMembership", "NotPast");
		}
		if (endDate.isBefore(LocalDate.now())) {
			errors.rejectValue("endOfMembership", "NotPast");
		}
		if (startDate.isAfter(endDate)) {
			errors.reject("endBeforeStart");
		}
		System.out.println("finito di vedere il binding di contract\n\n\n");
		
	}

}
