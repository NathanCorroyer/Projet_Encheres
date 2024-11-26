package fr.eni.projet.controller;

import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import fr.eni.projet.bll.UtilisateurService;
import fr.eni.projet.bo.Utilisateur;
import fr.eni.projet.exceptions.BusinessException;

@Controller
@RequestMapping("/users")
public class UtilisateurController {
	
	private UtilisateurService userService;
	
	
	public UtilisateurController(UtilisateurService userService) {
		this.userService = userService;
	}

	


	@GetMapping("/creer")
	private String creer(@ModelAttribute("user") Utilisateur user) {
		return "/utilisateurs/view-creer-user";
	}
	
	@PostMapping("/creer")
	private String creerUser(@ModelAttribute("user") Utilisateur user, BindingResult bindingResult) {
		if(bindingResult.hasErrors()) {
			return "view-film-form";
		} else {
			try {
				String password = user.getPassword();
				password = PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(password);
				user.setPassword(password);
				
				userService.create(user);
				return "redirect:/";
			} catch (BusinessException e) {
				// Afficher les messages d’erreur - il faut les injecter dans le contexte de
				// BindingResult
				e.getClefsExternalisations().forEach(key -> {
					ObjectError error = new ObjectError("globalError", key);
					bindingResult.addError(error);
				});
				return "view-creer-user";
			}
		}
	}
	
	@GetMapping("/profil")
	private String afficher(@RequestParam (name="id" , required=true) int id, Model model) {
		model.addAttribute("user", userService.findById(id));
		
		return "/utilisateurs/view-profil-user";
	}
	
	@PostMapping("/modifier")
	private String modifier() {
		
		return "/utilisateurs/view-profil-user";
	}
	
}
