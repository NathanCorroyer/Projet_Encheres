package fr.eni.projet.configuration.security;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import fr.eni.projet.service.security.UserSecurity;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	private static final String SELECT_USER = "select pseudo, mot_de_passe, 1 from UTILISATEURS WHERE pseudo = ?";
	private static final String SELECT_ROLES = "select u.pseudo, r.role from UTILISATEURS u INNER JOIN roles r on u.code_role = r.id WHERE u.pseudo = ?";
	protected final Log logger = LogFactory.getLog(getClass());

	/**
	 * Récupération des utilisateurs de l'application via la base de données
	 */
	@Bean
	UserDetailsManager userDetailsManager(DataSource dataSource) {
		JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);

		jdbcUserDetailsManager.setUsersByUsernameQuery(SELECT_USER);

		jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(SELECT_ROLES);

		return jdbcUserDetailsManager;
	}

	/**
	 * Restriction des URLs selon la connexion utilisateur et leurs rôles
	 */
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(auth -> {

			// Toutes autres url et méthodes HTTP ne sont pas permises
			auth.requestMatchers("/").permitAll();
			auth.requestMatchers("/css/*").permitAll();
			auth.requestMatchers("/img/*").permitAll();
			auth.requestMatchers("/articles/vendre").authenticated();
			auth.requestMatchers("/users/creer").permitAll();
			auth.requestMatchers("/users/modifiermdp/**").access((authentication, context) -> UserSecurity.hasAccessToUser(authentication.get(), context.getRequest()));
			auth.anyRequest().authenticated();

		});

		http.formLogin(form -> {
			form.loginPage("/login").permitAll().defaultSuccessUrl("/");
		}).logout(logout -> {
			logout.invalidateHttpSession(true).clearAuthentication(true).deleteCookies("JSESSIONID")
					.logoutSuccessUrl("/") // Rediriger vers la page d'accueil après une déconnexion
					.logoutRequestMatcher(new AntPathRequestMatcher("/logout")).permitAll();
		});

		return http.build();
	}

}
