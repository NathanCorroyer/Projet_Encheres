package fr.eni.projet.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import fr.eni.projet.bll.ArticleService;
import fr.eni.projet.bll.UtilisateurService;
import fr.eni.projet.bo.Adresse;
import fr.eni.projet.bo.Utilisateur;
import fr.eni.projet.exceptions.BusinessException;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/users")
@SessionAttributes({ "userSession" })
public class UtilisateurController {

	private UtilisateurService userService;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private ArticleService articleService;

	public UtilisateurController(UtilisateurService userService) {
		this.userService = userService;
	}

	// Méthode pour charger l'utilisateur en session
	/**
	 * Charger le user en session pour les utiliser dans toutes les vues de ce
	 * contrôleur.
	 */
  
	@ModelAttribute("userSession")
	public Utilisateur chargerUtilisateurConnecte(Authentication auth) {
		return userService.findByPseudo(auth.getName()).get();
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
			bindingResult.getAllErrors().forEach(error -> System.out.println(error.getDefaultMessage()));

			return "/utilisateurs/view-creer-user";
		}

		try {
			userService.create(user);
			return "redirect:/login";
		} catch (BusinessException e) {
			e.getClefsExternalisations().forEach(key -> {
				ObjectError error = new ObjectError("globalError", key);
				bindingResult.addError(error);
			});
			return "/utilisateurs/view-creer-user";
		}
	}

	@GetMapping("/profil")
	private String afficher(@RequestParam(name = "pseudo", required = true) String pseudo, Model model,
			RedirectAttributes redirectAttributes) {
		model.addAttribute("user", userService.findByPseudo(pseudo).get());
		redirectAttributes.addAttribute("pseudo", pseudo);
		return "/utilisateurs/view-profil-user";
	}

	@PostMapping("/profil")
	private String modifier(@ModelAttribute("user") Utilisateur user, BindingResult bindingResult,
			RedirectAttributes redirectAttributes, Authentication auth) {
		Utilisateur origine = userService.findById(user.getId());
		String pseudoUserConnected = auth.getName();
		if (!pseudoUserConnected.equals(origine.getPseudo())) {
			redirectAttributes.addAttribute("pseudo", auth.getName());
			redirectAttributes.addFlashAttribute("error",
					"Vous ne pouvez pas modifier le profil d'un autre utilisateur.");

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
			userService.update(user, emailModifie);
			// Ajouter le paramètre "pseudo" à l'URL de redirection
			redirectAttributes.addFlashAttribute("messageKey", "success.profile.updated");
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
	private String modifierMdp(@RequestParam("pseudo") String pseudo, Model model) {
		model.addAttribute("pseudo", pseudo);
		return "/utilisateurs/view-modifier-password";
	}

	@PostMapping("/modifiermdp")
	private String updateMdp(@RequestParam(name = "pseudo", required = true) String pseudo,
			@RequestParam(name = "currentPassword", required = true) String currentPassword,
			@RequestParam(name = "newPassword", required = true) String newPassword,
			@RequestParam(name = "confirmPassword", required = true) String confirmPassword,
			RedirectAttributes redirectAttributes, Authentication auth) {

		Locale locale = LocaleContextHolder.getLocale();
		String pseudoUserConnected = auth.getName();
		Utilisateur user = userService.findByPseudo(pseudoUserConnected).get();

		if (!pseudoUserConnected.equals(pseudo)) {
			redirectAttributes.addFlashAttribute("error",
					messageSource.getMessage("modification.mdp.acces.interdit", null, locale));
			return "redirect:/users/profil?pseudo=" + pseudo;
		}
		try {
			userService.updatePassword(user, currentPassword, newPassword, confirmPassword);
			redirectAttributes.addFlashAttribute("message",
					messageSource.getMessage("modification.mdp.reussite", null, locale));
			return "redirect:/users/profil?pseudo=" + pseudo;
		} catch (BusinessException e) {
			List<String> errorMessages = new ArrayList<>();
			e.getClefsExternalisations().forEach(key -> {
				String errorMessage = messageSource.getMessage(key, null, locale);
				errorMessages.add(errorMessage);
			});
			redirectAttributes.addFlashAttribute("errorMessages", errorMessages);
			return "redirect:/users/modifiermdp?pseudo=" + pseudo;
		}
	}

	@PostMapping("/retrait/{articleId}")
	@ResponseBody
	public ResponseEntity<?> retirerArticle(@PathVariable("articleId") int id) {
		System.out.println(id);
		try {
			if (id == 0) {
				return ResponseEntity.badRequest().body("ID de l'article non fourni.");
			}

			// Appeler le service pour mettre à jour le statut
			articleService.marquerCommeLivre(id);

			return ResponseEntity.ok("L'article a été marqué comme livré.");
		} catch (IllegalArgumentException e) {
			e.printStackTrace(); // Afficher les détails de l'erreur
			return ResponseEntity.badRequest().body("Erreur dans les paramètres : " + e.getMessage());
		} catch (BusinessException e) {
			e.printStackTrace(); // Afficher les détails de l'erreur
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Erreur lors du traitement. Détails : " + e.getMessage());
		}
	}

}
