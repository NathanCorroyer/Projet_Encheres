package fr.eni.projet.service;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import fr.eni.projet.bo.Utilisateur;
import jakarta.servlet.http.HttpSession;

@ControllerAdvice
public class GlobalControllerAdvice {

	@ModelAttribute("userSession")
	public Utilisateur addUserToModel(HttpSession session) {
		return (Utilisateur) session.getAttribute("userSession");
	}
}