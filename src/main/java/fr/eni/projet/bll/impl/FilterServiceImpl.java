package fr.eni.projet.bll.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import fr.eni.projet.DAL.ArticleDAO;
import fr.eni.projet.bll.FilterService;
import fr.eni.projet.bo.Article;
import fr.eni.projet.bo.Enchere;
import fr.eni.projet.bo.Utilisateur;
import fr.eni.projet.enums.StatutEnchere;

@Service
public class FilterServiceImpl implements FilterService {
	
	ArticleDAO articleDAO;

	public FilterServiceImpl() {
	}

	public List<Article> filterHomePageLogin(List<Article> articles, int categorieId, String nom, StatutEnchere statut,
			int userId, boolean isVendeur, boolean hasEncheri) {
		System.out.println("Filtrage des articles : ");
		System.out.println("Critères -> Categorie: " + categorieId + ", Nom: " + nom + ", Statut: "+ statut + ", Utilisateur: "+ userId);

		return	articles.stream()
				.filter(article -> filterByCategorie(article, categorieId))
				.filter(article -> filterByNom(article, nom))
				.filter(article -> filterByStatut(article, statut))
				.filter(article -> filterByVendeur(article, userId, isVendeur))
//				.filter(article -> filterByAcheteur(article, userId, hasEncheri))
				.collect(Collectors.toList());
	}
	
	public List<Article> filterHomePageLogout(List<Article> articles, int categorieId, String nom, StatutEnchere statutEnchere) {
		return articles.stream()
				.filter(article -> 
					(categorieId == 0 || article.getCategorie().getId() == categorieId) 
					&& (nom == null || article.getNom().toLowerCase().contains(nom.toLowerCase())
					&& (statutEnchere == null || article.getStatut_enchere().equals(statutEnchere))))
				.collect(Collectors.toList());
	}

	// Filtrer par catégorie
	public boolean filterByCategorie(Article article, int categorieId) {
		System.out.println("Filtrage par catégorie : "+ categorieId);
		if (categorieId != 0) {
			return article.getCategorie() != null && article.getCategorie().getId() == categorieId;
		}
		return true;
	}

	// Filtrer par nom
	public boolean filterByNom(Article article, String nom) {
		System.out.println("Filtrage par nom : "+ nom);
		if (nom != null && !nom.isEmpty()) {
			return article.getNom() != null && article.getNom().toLowerCase().contains(nom.toLowerCase());
		}
		return true;
	}

	// Filtrer par statut d'enchère
	public boolean filterByStatut(Article article, StatutEnchere statut) {
		System.out.println("Filtrage par statut : "+ statut);
		switch (statut) {
		case PAS_COMMENCEE:
			return article.getDate_debut().isAfter(LocalDateTime.now());
		case EN_COURS:
			return (article.getDate_fin().isAfter(LocalDateTime.now()) && article.getDate_debut().isBefore(LocalDateTime.now()));
		case CLOTUREE:
			return article.getDate_fin().isBefore(LocalDateTime.now());
		default:
			return true;
		}
	}
	
	// Filtrer si l'utilisateur est le propriétaire
	public boolean filterByVendeur(Article article, int userId, boolean isVendeur) {
		if (isVendeur) {
			return article.getProprietaire().getId() == userId;
		}
		return true;
	}
		
//	// Filtrer si l'utilisateur a fait un enchère
//	public boolean filterByAcheteur(Article article, int userId, boolean hasEncheri) {
//		if (hasEncheri) {
//			// Comment récupérer les informations des enchères de l'utilisateur si on ne passe pas par Enchere ?
//			return 
//		}
//		return true;
//	}

}
