package fr.eni.projet.configuration.security;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
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
	@SuppressWarnings("removal")
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http, UserSecurity userSecurity) throws Exception {
		http.csrf(csrf -> csrf.ignoringRequestMatchers("/articles/editer/**", "/admin/users/activation/**", "/admin/users/delete/**", "/categories/delete/**")).authorizeHttpRequests(auth -> {
			auth.requestMatchers("/").permitAll();
			auth.requestMatchers("/css/*").permitAll();
			auth.requestMatchers("/img/*").permitAll();
			auth.requestMatchers("/articles/vendre").authenticated();
			auth.requestMatchers("/users/creer").permitAll();
			auth.requestMatchers("/uploads/**").permitAll();

			// Configurer les règles de sécurité pour des routes spécifiques avant
			// 'anyRequest'
			auth.requestMatchers("/articles/editer/**").access((authentication, context) -> {
				AuthorizationDecision decision = userSecurity.hasAccessToArticle(authentication.get(),
						context.getRequest());
				return decision;
			});
			auth.requestMatchers("/articles/delete/**").access((authentication, context) -> {
				AuthorizationDecision decision = userSecurity.hasAccessToArticle(authentication.get(),
						context.getRequest());
				return decision;
			});
			auth.requestMatchers("/users/modifiermdp/**").access((authentication, context) -> UserSecurity
					.hasAccessToUser(authentication.get(), context.getRequest()));

			auth.requestMatchers("/admin/**").hasRole("ADMIN");
			auth.requestMatchers("/categories/**").hasRole("ADMIN");
			auth.anyRequest().authenticated();
		});

		// Configurer la gestion de la session et de la déconnexion
		http.formLogin(
				form -> form.loginPage("/login").permitAll().defaultSuccessUrl("/", true).failureUrl("/login?error=true"))
				.logout(logout -> logout.invalidateHttpSession(true).clearAuthentication(true)
						.deleteCookies("JSESSIONID").logoutSuccessUrl("/")
						.logoutRequestMatcher(new AntPathRequestMatcher("/logout")).permitAll())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
						.maximumSessions(1).expiredUrl("/login").and().invalidSessionUrl("/login"));

		return http.build();
	}

}
