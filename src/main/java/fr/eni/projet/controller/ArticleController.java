package fr.eni.projet.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
@RequestMapping("/articles")
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

	@GetMapping("/vendre")
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

	@PostMapping("/vendre")
	public String vendreArticle(@ModelAttribute("article") Article article, BindingResult bindingResult, Model model,
			Authentication auth) {
		// Vérifier si des erreurs de validation existent
		if (bindingResult.hasErrors()) {
			bindingResult.getAllErrors().forEach(error -> System.out.println(error.getDefaultMessage()));
			return "/articles/view-vendre-article"; // Retourner à la page de formulaire si des erreurs
		}
		System.out.println(article.getAdresse().getId());
		try {
			// Récupérer le nom d'utilisateur de l'authentification
			String pseudoUserConnected = auth != null ? auth.getName() : null;

			// Si l'utilisateur est connecté, récupérer ses informations
			if (pseudoUserConnected != null) {
				Optional<Utilisateur> optionalUtilisateur = userService.findByPseudo(pseudoUserConnected);
				if (optionalUtilisateur.isPresent()) {
					Utilisateur principal = optionalUtilisateur.get();
					article.setProprietaire(principal); // Associer l'utilisateur comme propriétaire de l'article
				} else {
					model.addAttribute("pseudo", "Utilisateur non trouvé");
					return "/articles/view-vendre-article"; // Si l'utilisateur n'est pas trouvé
				}
			} else {
				model.addAttribute("pseudo", "Utilisateur anonyme");
				return "/articles/view-vendre-article";
			}

			// Si les dates ne sont pas nulles, on les parse et les assigne
			if (article.getDate_debut() != null) {
				// Exemple de format de date : 2024-11-28T16:15 (pour datetime-local)
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
				LocalDateTime dateDebut = LocalDateTime.parse(article.getDate_debut().toString(), formatter);
				article.setDate_debut(dateDebut); // Assigner la date correctement
			}

			if (article.getDate_fin() != null) {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
				LocalDateTime dateFin = LocalDateTime.parse(article.getDate_fin().toString(), formatter);
				article.setDate_fin(dateFin); // Assigner la date correctement
			}
			article.setStatut_enchere(StatutEnchere.PAS_COMMENCEE);

			// Créer l'article si tout est valide
			articleService.create(article);

			return "redirect:/"; // Rediriger vers la page d'accueil après la création de l'article

		} catch (BusinessException e) {
			e.printStackTrace();
			e.getClefsExternalisations().forEach(key -> {
				bindingResult.addError(new ObjectError("globalError", key));
			});
			model.addAttribute("pseudo", "Erreur lors de la vente de l'article");
			return "/articles/view-vendre-article";
		}
	}

}
