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
import fr.eni.projet.bo.Adresse;
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
		if (user.getAdresse() == null) {
			user.setAdresse(new Adresse());
		}
		return "/utilisateurs/view-creer-user";
	}

	@PostMapping("/creer")
	private String creerUser(@ModelAttribute("user") Utilisateur user, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			// Trace les erreurs pour le debug
			bindingResult.getAllErrors().forEach(error -> System.out.println(error.getDefaultMessage()));
			return "/utilisateurs/view-creer-user";
		}

		try {
			// Encode le mot de passe
			String password = user.getPassword();
			password = PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(password);
			user.setPassword(password);

			// Sauvegarde l'utilisateur
			userService.create(user);
			return "redirect:/"; // Redirige vers la page d'accueil ou une autre vue
		} catch (BusinessException e) {
			// Ajoute les messages d'erreur métier au BindingResult
			e.getClefsExternalisations().forEach(key -> {
				ObjectError error = new ObjectError("globalError", key);
				bindingResult.addError(error);
			});
			return "/utilisateurs/view-creer-user";
		}
	}

	@GetMapping("/profil")
	private String afficher(@RequestParam(name = "pseudo", required = true) String pseudo, Model model) {
		model.addAttribute("user", userService.findByPseudo(pseudo));
		return "/utilisateurs/view-profil-user";
	}

	@PostMapping("/modifier")
	private String modifier() {
		return "/utilisateurs/view-profil-user";
	}

}
