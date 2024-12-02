package fr.eni.projet.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import fr.eni.projet.bll.AdresseService;
import fr.eni.projet.bll.ArticleService;
import fr.eni.projet.bll.CategorieService;
import fr.eni.projet.bll.UtilisateurService;
import fr.eni.projet.bo.Adresse;
import fr.eni.projet.bo.Article;
import fr.eni.projet.bo.Categorie;
import fr.eni.projet.bo.Utilisateur;
import fr.eni.projet.enums.StatutEnchere;
import fr.eni.projet.exceptions.BusinessException;

@Controller
public class ArticleController {

	private ArticleService articleService;
	private CategorieService categorieService;
	private UtilisateurService userService;
	private AdresseService adresseService;

	public ArticleController(ArticleService articleService, CategorieService categorieService,
			UtilisateurService utilisateurService, AdresseService adresseService) {
		this.articleService = articleService;
		this.categorieService = categorieService;
		this.userService = utilisateurService;
		this.adresseService = adresseService;
	}

	/**
	 * Charger les catégories en session pour les utiliser dans toutes les vues de
	 * ce contrôleur.
	 */
	@ModelAttribute("categories")
	public List<Categorie> chargerCategories() {
		return categorieService.findAll();
	}

	@ModelAttribute("adressesENI")
	public List<Adresse> chargerAdresses() {
		return adresseService.findByAdresseENI(true);
	}

	@GetMapping("/articles/vendre")
	public String vendreArticleForm(@ModelAttribute("article") Article article, Authentication auth, Model model) {
		String pseudoUserConnected = auth != null ? auth.getName() : null;
		if (pseudoUserConnected != null) {
			Optional<Utilisateur> optionalUtilisateur = userService.findByPseudo(pseudoUserConnected);
			if (optionalUtilisateur.isPresent()) {
				Utilisateur utilisateur = optionalUtilisateur.get();
				model.addAttribute("pseudo", pseudoUserConnected);
				List<Adresse> adressesUtilisateur = adresseService.findByUtilisateur(optionalUtilisateur);
				model.addAttribute("adressesUtilisateur", adressesUtilisateur);

				List<Adresse> adressesENI = adresseService.findByAdresseENI(true);
				model.addAttribute("adressesENI", adressesENI);
			} else {
				model.addAttribute("pseudo", "Utilisateur non trouvé");
			}
		} else {
			model.addAttribute("pseudo", "Utilisateur anonyme");
		}

		return "articles/view-vendre-article";
	}

	@PostMapping("/articles/vendre")
	public String vendreArticle(@ModelAttribute("article") Article article, BindingResult bindingResult, Model model,
			Authentication auth) {
		if (bindingResult.hasErrors()) {
			bindingResult.getAllErrors().forEach(error -> System.out.println(error.getDefaultMessage()));
			return "/articles/view-vendre-article";
		}
		System.out.println(article.getAdresse().getId());
		try {
			String pseudoUserConnected = auth != null ? auth.getName() : null;
			if (pseudoUserConnected != null) {
				Optional<Utilisateur> optionalUtilisateur = userService.findByPseudo(pseudoUserConnected);
				if (optionalUtilisateur.isPresent()) {
					Utilisateur principal = optionalUtilisateur.get();
					article.setProprietaire(principal);
				} else {
					model.addAttribute("pseudo", "Utilisateur non trouvé");
					return "/articles/view-vendre-article";
				}
			} else {
				model.addAttribute("pseudo", "Utilisateur anonyme");
				return "/articles/view-vendre-article";
			}

			if (article.getDate_debut() != null) {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
				LocalDateTime dateDebut = LocalDateTime.parse(article.getDate_debut().toString(), formatter);
				article.setDate_debut(dateDebut);
			}

			if (article.getDate_fin() != null) {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
				LocalDateTime dateFin = LocalDateTime.parse(article.getDate_fin().toString(), formatter);
				article.setDate_fin(dateFin);
			}
			article.setStatut_enchere(StatutEnchere.PAS_COMMENCEE);

			return "redirect:/articles/details/" + articleService.create(article);

		} catch (BusinessException e) {
			e.printStackTrace();
			e.getClefsExternalisations().forEach(key -> {
				bindingResult.addError(new ObjectError("globalError", key));
			});
			model.addAttribute("pseudo", "Erreur lors de la vente de l'article");
			return "/articles/view-vendre-article";
		}
	}

	@GetMapping("/")
	public String afficherActiveEncheres(Model model) {
		List<Article> lstArticles = articleService.findAllActive();
		model.addAttribute("articles", lstArticles);

		return "index";
	}

	@GetMapping("/articles/editer/{id}")
	public String editerArticle(@PathVariable("id") int id, Model model, Authentication auth) {
		Article article = articleService.findById(id);

		// Vérification que l'enchère n'a pas déjà commencé
		if (article.getDate_debut() != null && article.getDate_debut().isBefore(LocalDateTime.now())) {
			model.addAttribute("errorMessage",
					"Vous ne pouvez pas modifier un article dont l'enchère a déjà commencé.");
			return "redirect:/articles"; // Rediriger si l'enchère a déjà commencé
		}

		model.addAttribute("article", article);
		// Récupérer l'utilisateur connecté
		if (auth != null) {
			String pseudoUserConnected = auth.getName();
			Optional<Utilisateur> optionalUtilisateur = userService.findByPseudo(pseudoUserConnected);

			if (optionalUtilisateur.isPresent()) {
				Utilisateur utilisateur = optionalUtilisateur.get();
				// Ajouter les adresses personnelles au modèle
				List<Adresse> adressesUtilisateur = adresseService.findByUtilisateur(optionalUtilisateur);
				model.addAttribute("adressesUtilisateur", adressesUtilisateur);
			}
		}
		return "articles/view-edit-article";
	}

	@PostMapping("articles/editer/{id}")
	public String enregistrerModifications(@PathVariable("id") int id, @ModelAttribute Article article,
			RedirectAttributes redirectAttributes) {

		try {
			// Récupérer l'article existant
			Article existingArticle = articleService.findById(id);

			if (existingArticle == null) {
				throw new IllegalArgumentException("L'article n'existe pas.");
			}

			System.out.println(article);

			articleService.update(article);

			// Message de succès et redirection vers la page de détails de l'article
			redirectAttributes.addFlashAttribute("successMessage", "Article modifié avec succès.");
			return "redirect:/articles/details/" + article.getId(); // Redirection vers la page de détails
		} catch (BusinessException e) {
			// En cas d'erreur (ex. problème de base de données, validation échouée, etc.)
			redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de la modification de l'article.");
			return "redirect:/articles/editer/" + id; // Rester sur la page d'édition
		}
	}

	@GetMapping("/articles/details/{id}")
	public String afficherDetailsArticle(@PathVariable("id") int id, Model model, Authentication authentication) {

		Article article = articleService.findById(id);

		if (article == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Article non trouvé");
		}

		// Vérification de la catégorie
		if (article.getCategorie() != null) {
			model.addAttribute("categorie", article.getCategorie().getId());
		} else {
			model.addAttribute("categorie", "Catégorie non définie");
		}

		// Vérification de l'adresse
		if (article.getAdresse() != null) {
			model.addAttribute("adresse", article.getAdresse().getId());
		} else {
			model.addAttribute("adresse", "Adresse non définie");
		}

		if (article.getProprietaire() != null) {
			String pseudoUserConnected = article.getProprietaire().getPseudo();
			model.addAttribute("pseudo", pseudoUserConnected);

			// Vérification si l'utilisateur connecté est le propriétaire
			boolean isOwner = pseudoUserConnected.equals(authentication.getName());
			model.addAttribute("isOwner", isOwner); // Passer l'information au modèle
		} else {
			model.addAttribute("pseudo", "Utilisateur anonyme");
			model.addAttribute("isOwner", false); // L'utilisateur anonyme ne peut pas modifier
		}

		// Ajouter l'article à l'attribut modèle
		model.addAttribute("article", article);

		return "articles/details-article";
	}

	@GetMapping("/delete/{id}")
	public String supprimerArticle(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
		try {
			articleService.delete(id);
			redirectAttributes.addFlashAttribute("successMessage", "L'article a été supprimé avec succès.");
		} catch (BusinessException be) {
			// Gestion des erreurs métier
			redirectAttributes.addFlashAttribute("errorMessage", "Impossible de supprimer l'article : " + be.isValid());
		} catch (IllegalArgumentException iae) {
			// Gestion des erreurs générales
			redirectAttributes.addFlashAttribute("errorMessage", "Erreur : " + iae.getMessage());
		}
		return "redirect:/";
	}

}
