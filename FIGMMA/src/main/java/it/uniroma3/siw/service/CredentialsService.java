package it.uniroma3.siw.service;


import java.util.Optional;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.repository.CredentialsRepository;

@Service
public class CredentialsService {
	
	@Autowired
	private CredentialsRepository credentialsRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Transactional
	public Credentials getCredentials(Long id) {
		Optional<Credentials> result = this.credentialsRepository.findById(id);
		return result.orElse(null);
	}
	
	@Transactional
	public Credentials getCredentials(String username) {
		Optional<Credentials> result = this.credentialsRepository.findByUsername(username);
		return result.orElse(null);
	}
	
	@Transactional
	public Credentials saveCredentials(Credentials credentials) {
		if (credentials.getRole() == null) {
			credentials.setRuolo(Credentials.GENERIC_USER_ROLE);
		}
		credentials.setPassword(this.passwordEncoder.encode(credentials.getPassword()));
		return credentialsRepository.save(credentials);
	}

	public boolean alreadyExist(Credentials credentials) {
		return this.credentialsRepository.existsByUsername(credentials.getUsername());
	}
}