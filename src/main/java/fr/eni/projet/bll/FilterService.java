package fr.eni.projet.bll;

import java.util.List;

import fr.eni.projet.bo.Article;
import fr.eni.projet.enums.StatutEnchere;

public interface FilterService {

	// Filtrer quand connecté
	public List<Article> filterHomePageLogin(List<Article> articles, int categorieId, String nom);
	
	// Filtrer quand déconnecté
	public List<Article> filterHomePageLogout(List<Article> articles, int categorieId, String nom, StatutEnchere statutEnchere);	
	
	// Filtrer par catégorie
	public boolean filterByCategorie(Article article, int categorieId);
	
	// Filtrer par nom
	public boolean filterByNom(Article article, String nom);
	
	// Filtrer par statut d'enchère
	public boolean filterByStatut(Article article, StatutEnchere statut);

	// Filtrer si l'utilisateur est le propriétaire
	public boolean filterByVendeur(Article article, int userId, boolean isVendeur);
	
	// Filtrer si l'utilisateur a fait un enchère
//	public boolean filterByAcheteur(Article article, Utilisateur userconnecte,  boolean hasEncheri);

}