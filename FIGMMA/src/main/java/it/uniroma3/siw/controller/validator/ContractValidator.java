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
		return false;
	}

	@Override
	public void validate(Object o, Errors errors) {
		Contract contract =(Contract)o;
		LocalDate startDate =contract.getStartOfMembership();
		LocalDate endDate=contract.getEndOfMembership();
		if (startDate.isBefore(LocalDate.now())) {
			errors.rejectValue("startOfMembership", "NotPast");
		}
		if (endDate.isBefore(LocalDate.now())) {
			errors.rejectValue("endOfMembership", "NotPast");
		}
		if (startDate.isAfter(endDate)) {
			errors.reject("endBeforeStart");
		}		
	}
}