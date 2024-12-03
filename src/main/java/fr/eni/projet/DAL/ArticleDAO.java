package fr.eni.projet.DAL;

import java.time.LocalDateTime;
import java.util.List;

import fr.eni.projet.bo.Article;
import fr.eni.projet.enums.StatutEnchere;

public interface ArticleDAO {

	public Article findArticleById(int id);

	public List<Article> findAll();

	public List<Article> findAllActive();

	public int create(Article article);

	public void update(Article article);

	public List<Article> findByCategorie(int categorieId);

	public List<Article> findByUtilisateur(int utilisateurId);

	public void delete(int id);

	public List<Article> findByDateDebutAndStatutEnchere(LocalDateTime today, int i);

	void updateStatutEnchere(Article article, StatutEnchere statutEnchere);
}
