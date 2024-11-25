package fr.eni.projet.bll.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.eni.projet.DAL.ArticleDAO;
import fr.eni.projet.bll.ArticleService;
import fr.eni.projet.bo.Article;

@Service
public class ArticleServiceImpl implements ArticleService {

	@Autowired
	private ArticleDAO articleDAO;

	@Override
	public void create(Article article) {
		// Appel au DAO pour insérer l'article dans la base de données
		articleDAO.create(article);
	}

	@Override
	public Article findById(int id) {
		// Appel au DAO pour trouver un article par son ID
		return articleDAO.findArticleById(id);
	}

	@Override
	public List<Article> findAll() {
		// Appel au DAO pour obtenir tous les articles
		return articleDAO.findAll();
	}

	@Override
	public void update(Article article) {
		// Appel au DAO pour mettre à jour un article existant
		articleDAO.update(article);
	}

	@Override
	public void delete(int id) {
		// Appel au DAO pour supprimer un article par son ID
		articleDAO.delete(id);
	}

	@Override
	public List<Article> findByCategorie(int categorieId) {
		// Appel au DAO pour obtenir les articles associés à une catégorie spécifique
		return articleDAO.findByCategorie(categorieId);
	}

	@Override
	public List<Article> findByUtilisateur(int utilisateurId) {
		// Appel au DAO pour obtenir les articles associés à un utilisateur spécifique
		return articleDAO.findByUtilisateur(utilisateurId);
	}

	@Override
	public Article findArticleById(int id) {
		// TODO Auto-generated method stub
		return null;
	}
}
