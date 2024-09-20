package fr.eni.projet.configuration.security;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

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
			auth.requestMatchers("/users/creer").permitAll();
			auth.requestMatchers("/css/*").permitAll();
			auth.requestMatchers("/img/*").permitAll();
			
			auth.anyRequest().authenticated();
		});

		
		http.formLogin(Customizer.withDefaults());
		
//		http.formLogin( form -> {
//			form.loginPage("/login").permitAll()
//			.defaultSuccessUrl("/").permitAll();
//			
//		});
		
		
		
		// Logout --> vider la session et le contexte de sécurité
		http.logout(logout -> 
			logout
				.invalidateHttpSession(true)
				.clearAuthentication(true)
				.deleteCookies("JSESSIONID")
//				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutSuccessUrl("/")
				.permitAll()
		);


		return http.build();
	}


}
