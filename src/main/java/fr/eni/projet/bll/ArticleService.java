package fr.eni.projet.bll;

import java.util.List;

import fr.eni.projet.bo.Article;
import fr.eni.projet.bo.Categorie;

public interface ArticleService {
	public void create(Article article);

	public List<Article> findAll();
	
	public List<Article> findAllActive();
	
	public List<Categorie> findAllCategories();
 
	public void delete(int id);

	public void update(Article article);

	Article findById(int id);

	List<Article> findByCategorie(int categorieId);
		
	List<Article> findByNom(String nom);

	List<Article> findByUtilisateur(int utilisateurId);
	
	List<Article> filterByCategorie(List<Article> articles, Long categorieId);

}
 