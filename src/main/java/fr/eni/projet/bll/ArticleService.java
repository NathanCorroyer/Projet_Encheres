package fr.eni.projet.bll;

import java.util.List;

import fr.eni.projet.bo.Article;

public interface ArticleService {
	public void create(Article article);

	public Article findArticleById(int id);

	public List<Article> findAll();

	public void delete(Article article);

	public void update(Article article);
}
