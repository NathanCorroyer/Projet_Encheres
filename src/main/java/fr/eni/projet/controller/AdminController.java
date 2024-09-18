package fr.eni.projet.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.eni.projet.bll.UtilisateurService;

@Controller
@RequestMapping("/admin")
public class AdminController {

	UtilisateurService utilisateurService;
	
	
	public AdminController(UtilisateurService utilisateurService) {
		this.utilisateurService = utilisateurService;
	}

	@GetMapping("/users")
	public String listerUtilisateurs() {
		return "/admin/view-admin-utilisateurs";
	}
	
}
