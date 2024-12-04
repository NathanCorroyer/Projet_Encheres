package fr.eni.projet.bll;

import java.util.List;

import fr.eni.projet.bo.Article;
import fr.eni.projet.bo.Categorie;
import fr.eni.projet.bo.Utilisateur;
import fr.eni.projet.enums.StatutEnchere;

public interface ArticleService {

	public void activerEncheresDuJour();

	public int create(Article article);

	public List<Article> findAll();

	public List<Article> findAllActive();

	public List<Categorie> findAllCategories();

	public void delete(int id);

	public void update(Article article);

	Article findById(int id);

	List<Article> findByCategorie(int categorieId);

	List<Article> findByNom(String nom);

	List<Article> findByUtilisateur(int utilisateurId);
	
	List<Article> findByProprietaireOrAcheteur(int utilisateurId);
	
	List<Article> filtersHomePage(List<Article> articles, Long categorieId, String nom, StatutEnchere statutEnchere);

	public void uploadImage(String fileName, int idArticle);

	public void cloturerEncheresDuJour();

	public boolean canShowRetraitButton(Article article);

	public String titre(Article article, boolean isEncherisseur, String pseudoWinner, boolean hasEnchere);

}
