package fr.eni.projet.bll;

import java.util.List;

import fr.eni.projet.bo.Article;

public interface ArticleService {
	public void create(Article article);

	public List<Article> findAll();
	
	public List<Article> findAllActive(int categorieId, String nom);
 
	public void delete(int id);

	public void update(Article article);

	Article findById(int id);

	List<Article> findByCategorie(int categorieId);
	
	List<Article> findByNom(String nom);

	List<Article> findByUtilisateur(int utilisateurId);

}
