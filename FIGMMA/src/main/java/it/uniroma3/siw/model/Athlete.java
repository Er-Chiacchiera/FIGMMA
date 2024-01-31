package it.uniroma3.siw.model;

import java.time.LocalDate;
import java.util.Objects;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

@Entity
public class Athlete {


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(nullable = false)
	@NotBlank
	private String name;

	@Column(nullable = false)
	@NotBlank
	private String surname;

	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@Past
	private LocalDate dateOfBirth;

	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private LocalDate startOfMembership;

	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private LocalDate endOfMembership;

	@OneToOne(cascade = CascadeType.ALL)
	@NotNull
	private Site site;

	@ManyToOne
	private Team team;

	public Athlete() {
		this.site=new Site();
	}

	/***************** Get e Set metod *****************/

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

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

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	/***************** hashCode e equals metod *****************/

	@Override
	public int hashCode() {
		return Objects.hash(dateOfBirth, name, surname);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Athlete other = (Athlete) obj;
		return Objects.equals(dateOfBirth, other.dateOfBirth) && Objects.equals(name, other.name)
				&& Objects.equals(surname, other.surname);
	}


}
