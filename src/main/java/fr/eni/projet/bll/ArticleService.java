package fr.eni.projet.bll;

import java.util.List;

import fr.eni.projet.bo.Article;

public interface ArticleService {
	public int create(Article article);

	public List<Article> findAll();
	
	public List<Article> findAllActive();
 
	public void delete(int id);

	public void update(Article article);

	Article findById(int id);

	List<Article> findByCategorie(int categorieId);

	List<Article> findByUtilisateur(int utilisateurId);

}
