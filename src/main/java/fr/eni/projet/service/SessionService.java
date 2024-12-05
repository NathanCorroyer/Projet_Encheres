package fr.eni.projet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.eni.projet.bo.Utilisateur;
import jakarta.servlet.http.HttpSession;

@Service
public class SessionService {

	@Autowired
	private final HttpSession http;

	public SessionService(HttpSession http) {
		super();
		this.http = http;
	}

	public Utilisateur getUserSessionAttribute() {
		return (Utilisateur) http.getAttribute("userSession");
	}

}
