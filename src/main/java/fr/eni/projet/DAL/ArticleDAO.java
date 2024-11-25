package fr.eni.projet.DAL;

import java.util.List;

import fr.eni.projet.bo.Article;

public interface ArticleDAO {

	public Article findArticleById(int id);

	public List<Article> findAll();

	public void create(Article article);

	public void update(Article article);

	public List<Article> findByCategorie(int categorieId);

	public List<Article> findByUtilisateur(int utilisateurId);

	public void delete(int id);

}
