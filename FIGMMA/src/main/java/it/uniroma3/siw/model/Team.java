package it.uniroma3.siw.model;



import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;


@Entity
public class Team {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotBlank
	private String name;

	@NotNull
	@Past
	private LocalDate dateOfCreation;

	@NotNull
	@OneToOne(cascade = CascadeType.ALL)
	private Site site;

	@NotNull
	@OneToOne
	private User president;

	@OneToMany(mappedBy = "team")
	private List<Athlete> athletes;

	public Team() {
		this.site= new Site();
		this.athletes= new LinkedList<>();
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

	public LocalDate getDateOfCreation() {
		return dateOfCreation;
	}

	public void setDateOfCreation(LocalDate dateOfCreation) {
		this.dateOfCreation = dateOfCreation;
	}

	public List<Athlete> getAthletes() {
		return athletes;
	}


	public void setAthletes(List<Athlete> athletes) {
		this.athletes = athletes;
	}


	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public User getPresident() {
		return president;
	}

	public void setPresident(User president) {
		this.president = president;
	}

	/***************** hashCode e equals metod *****************/

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Team other = (Team) obj;
		return Objects.equals(name, other.name);
	}	
}