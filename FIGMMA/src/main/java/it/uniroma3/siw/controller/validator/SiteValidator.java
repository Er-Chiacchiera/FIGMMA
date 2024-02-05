package it.uniroma3.siw.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Site;
import it.uniroma3.siw.repository.SiteRepository;

@Component
public class SiteValidator implements Validator{
	@Autowired SiteRepository siteRepository;

	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void validate(Object o, Errors errors) {
		Site site = (Site)o;
		if (site.getCountry()!=null && site.getCity()!=null && site.getCap()!=null && this.siteRepository.existsByCountryAndCityAndCap(site.getCountry(), site.getCity(),site.getCap())) {
			errors.reject("site.duplicate");
		}
		if(site.getCountry().isEmpty())
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "site.country", "NotBlank");

		String cap =site.getCap();

		if(cap.isEmpty()) {
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "site.cap", "NotBlank");
		}
		else {

			for(int i=0; i<cap.length(); i++) {
				if(!Character.isDigit(cap.charAt(i))) {
					errors.rejectValue("site.cap", "NotNumeric");
					break;
				}
			}

			if(cap.length()!=5)
				errors.rejectValue("site.cap", "NotSize5");
		}
		if(site.getCity().isEmpty())
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "site.city", "NotBlank");

	}

	public void validateAddress(Object o, Errors errors) {
		Site site = (Site)o;
		if (site.getCountry()!=null && site.getCity()!=null && site.getCap()!=null && this.siteRepository.existsByCountryAndCityAndCap(site.getCountry(), site.getCity(),site.getCap())) {
			errors.reject("site.duplicate");
		}
		if(site.getCountry().isEmpty())
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "site.country", "NotBlank");

		String cap =site.getCap();

		if(cap.isEmpty()) {
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "site.cap", "NotBlank");
		}
		else {

			for(int i=0; i<cap.length(); i++) {
				if(!Character.isDigit(cap.charAt(i))) {
					errors.rejectValue("site.cap", "NotNumeric");
					break;
				}
			}

			if(cap.length()!=5)
				errors.rejectValue("site.cap", "NotSize5");
		}
		if(site.getCity().isEmpty())
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "site.city", "NotBlank");
		if(site.getAddress().isEmpty())
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "site.address", "NotBlank");


	}


}


