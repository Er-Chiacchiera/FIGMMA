package it.uniroma3.siw.model;

import java.time.LocalDate;
import java.util.Objects;

import org.springframework.format.annotation.DateTimeFormat;

public class Contract {
	
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private LocalDate startOfMembership;

	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private LocalDate endOfMembership;
	

	public LocalDate getStartOfMembership() {
		return startOfMembership;
	}

	public void setStartOfMembership(LocalDate startOfMembership) {
		this.startOfMembership = startOfMembership;
	}

	public LocalDate getEndOfMembership() {
		return endOfMembership;
	}

	public void setEndOfMembership(LocalDate endOfMembership) {
		this.endOfMembership = endOfMembership;
	}

	@Override
	public int hashCode() {
		return Objects.hash(endOfMembership, startOfMembership);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Contract other = (Contract) obj;
		return Objects.equals(endOfMembership, other.endOfMembership)
				&& Objects.equals(startOfMembership, other.startOfMembership);
	}

}
