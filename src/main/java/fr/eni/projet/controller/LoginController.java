package fr.eni.projet.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import fr.eni.projet.bll.UtilisateurService;
import fr.eni.projet.bo.Utilisateur;
import jakarta.servlet.http.HttpSession;

@Controller
@SessionAttributes({ "userSession" })
public class LoginController {

	@Autowired
	UtilisateurService userService;

	@ModelAttribute("userSession")
	public Utilisateur chargerUserSession() {
		return userService.findByPseudo(SecurityContextHolder.getContext().getAuthentication().getName())
				.orElse(new Utilisateur());
	}

	@GetMapping("/session")
	public String chargerUserEnSession(Principal principal, HttpSession session) {

		Utilisateur updatedUser = userService.findByPseudo(principal.getName()).orElse(new Utilisateur());
		session.setAttribute("userSession", updatedUser);
		System.out.println("Utilisateur mis à jour : " + updatedUser.getPseudo());

		System.out.println("Utilisateur dans la session : " + session.getAttribute("userSession"));
		return "redirect:/";
	}

	@GetMapping("/login")
	public String showLoginPage() {

		return "login";
	}

}
