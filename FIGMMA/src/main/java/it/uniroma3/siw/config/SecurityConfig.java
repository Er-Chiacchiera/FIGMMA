package it.uniroma3.siw.config;

import  static it.uniroma3.siw.model.Credentials.ADMIN_ROLE;
import static it.uniroma3.siw.model.Credentials.PRESIDENT_ROLE;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private DataSource dataSource;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication()
		.dataSource(dataSource)
		.authoritiesByUsernameQuery("SELECT username, role from credentials WHERE username=?")
		.usersByUsernameQuery("SELECT username, password, 1 as enabled FROM credentials WHERE username=?");
	}
	@Bean
	public PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder(); }

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
		.cors(cors -> cors.disable())
		.csrf(csrf -> csrf.disable())
		.authorizeHttpRequests((authorize) -> authorize
				// chiunque (autenticato o no) può accedere alle pagine index, login, register, ai css e alle immagini
				.requestMatchers(HttpMethod.GET,"/","login","/index","/register","/css/**", "/images/**", "/loghi/**","/carousel/*").permitAll()
				// chiunque (autenticato o no) può mandare richieste POST al punto di accesso per login e register
				.requestMatchers(HttpMethod.POST,"/register", "/login").permitAll()
				.requestMatchers(HttpMethod.GET, "/*/add/*","/*/edit/*","/*/delete/*","*/update/*").hasAnyAuthority(ADMIN_ROLE)
				.requestMatchers(HttpMethod.POST, "/*/add/*","/*/edit/*","/*/delete/*","*/update/*").hasAnyAuthority(ADMIN_ROLE)
				//solo gli utenti autenticati con ruolo admin possono accedere a risorse con path /admin/**
				
				// tutti gli utenti autenticati possono accere alle pagine rimanenti
				.anyRequest().authenticated())
		.formLogin(form -> form
				.loginPage("/login")
				.defaultSuccessUrl("/", true)
				.failureUrl("/login")
				.permitAll())
		.logout(logout ->logout
				// il logout è attivato con una richiesta GET a "/logout"
				.logoutUrl("/logout")
				// in caso di successo, si viene reindirizzati alla home
				.logoutSuccessUrl("/default")
				.invalidateHttpSession(true)
				.deleteCookies("JSESSIONID")
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.clearAuthentication(true).permitAll());

		return http.build();
	}

}
