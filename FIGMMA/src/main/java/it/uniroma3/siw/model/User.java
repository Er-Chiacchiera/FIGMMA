package it.uniroma3.siw.model;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "users")
public class User {

	@Id 
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotBlank
	@Size(min=1)
	private String nome;
	
	@NotBlank
	@Size(min=1)
	private String cognome;
	
	@NotBlank
	@Size(min=1)
	private String cF;
	
	@NotNull
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private LocalDate dateOfBirth;
	
	@OneToOne(cascade = CascadeType.ALL)
	@NotNull
	private Site site;
	
	@OneToOne(fetch = FetchType.EAGER, mappedBy="user", cascade = CascadeType.ALL)
	private Credentials credentials;
	
	@OneToOne
	private Team team;
	
	public User() {
		this.site=new Site();
	}
	
	/***************** Get e Set metod *****************/

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getcF() {
		return cF;
	}

	public void setcF(String cF) {
		this.cF = cF;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public Credentials getCredentials() {
		return credentials;
	}

	public void setCredentials(Credentials credentials) {
		this.credentials = credentials;
	}
	
	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}
	
	
}