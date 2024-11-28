package fr.eni.projet.controller;

import org.springframework.security.core.Authentication;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import fr.eni.projet.bll.UtilisateurService;
import fr.eni.projet.bo.Adresse;
import fr.eni.projet.bo.Utilisateur;
import fr.eni.projet.exceptions.BusinessException;
import jakarta.validation.Valid;

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
	private String creerUser(@Valid @ModelAttribute("user") Utilisateur user, BindingResult bindingResult) {
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
			return "redirect:/login";
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
	private String afficher(@RequestParam(name = "pseudo", required = true) String pseudo, Model model, RedirectAttributes redirectAttributes) {
		model.addAttribute("user", userService.findByPseudo(pseudo).get());
		redirectAttributes.addAttribute("pseudo", pseudo);
		return "/utilisateurs/view-profil-user";
	}

	@PostMapping("/profil")
	private String modifier(@ModelAttribute("user") Utilisateur user, BindingResult bindingResult, RedirectAttributes redirectAttributes, Authentication auth) {
		Utilisateur origine = userService.findById(user.getId());
		String pseudoUserConnected = auth.getName();
		if(!pseudoUserConnected.equals(origine.getPseudo())) {
			redirectAttributes.addAttribute("pseudo", auth.getName());
			redirectAttributes.addFlashAttribute("error", "Vous ne pouvez pas modifier le profil d'un autre utilisateur.");

			return "redirect:/users/profil";
		}
		redirectAttributes.addAttribute("pseudo", origine.getPseudo());
		if (bindingResult.hasErrors()) {
			// Trace les erreurs pour le debug
			bindingResult.getAllErrors().forEach(error -> System.out.println(error.getDefaultMessage()));
			return "redirect:/users/profil";
		}
		
		try {
			// Sauvegarde l'utilisateur
			boolean emailModifie = !origine.getEmail().equals(user.getEmail());
			userService.update(user,  emailModifie);
			// Ajouter le paramètre "pseudo" à l'URL de redirection
	        redirectAttributes.addFlashAttribute("message", "Profil mis à jour avec succès.");
	        // Rediriger vers le profil de l'utilisateur avec le paramètre "pseudo"
	        return "redirect:/users/profil";
		} catch (BusinessException e) {
			// Ajoute les messages d'erreur métier au BindingResult
			e.getClefsExternalisations().forEach(key -> {
				ObjectError error = new ObjectError("globalError", key);
				bindingResult.addError(error);
			});
			return "redirect:/users/profil";
		}
	}
	
	@GetMapping("/modifiermdp")
	private String modifierMdp() {
		return "/utilisateurs/view-modifier-password";
	}
	
	@PostMapping("/modifiermdp")
	private String updateMdp() {
		
		return "/utilisateurs/view-modifier-password";
	}
}
