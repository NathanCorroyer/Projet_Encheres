package fr.eni.projet.bll.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.eni.projet.DAL.ArticleDAO;
import fr.eni.projet.bll.ArticleService;
import fr.eni.projet.bo.Adresse;
import fr.eni.projet.bo.Article;
import fr.eni.projet.enums.StatutEnchere;
import fr.eni.projet.exceptions.BusinessCode;
import fr.eni.projet.exceptions.BusinessException;

@Service
public class ArticleServiceImpl implements ArticleService {

	@Autowired
	private ArticleDAO articleDAO;

	@Override
	public void create(Article article) {
		BusinessException be = new BusinessException();

		if (!validerArticle(article, be)) {
			System.err.println("Erreur de validation pour l'article: " + article);
			throw be;
		}

		try {
			System.out.println("L'article est valide. Tentative de création dans la base de données.");
			articleDAO.create(article);
			System.out.println("L'article a été créé avec succès: " + article);
		} catch (Exception e) {

			System.err.println("Erreur lors de la création de l'article: " + article);
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public Article findById(int id) {
		if (id <= 0) {
			throw new IllegalArgumentException("L'ID de l'article doit être supérieur à 0.");
		}
		return articleDAO.findArticleById(id);
	}

	@Override
	public List<Article> findAll() {
		return articleDAO.findAll();
	}

	@Override
	public void update(Article article) {
		// Validation avant la mise à jour
		BusinessException be = new BusinessException();
		if (!validerArticle(article, be)) {
			throw be; // Lève une exception si la validation échoue
		}
		articleDAO.update(article);
	}

	@Override
	public void delete(int id) {
		if (id <= 0) {
			throw new IllegalArgumentException("L'ID de l'article doit être supérieur à 0.");
		}
		articleDAO.delete(id);
	}

	@Override
	public List<Article> findByCategorie(int categorieId) {
		return articleDAO.findByCategorie(categorieId);
	}

	@Override
	public List<Article> findByUtilisateur(int utilisateurId) {
		return articleDAO.findByUtilisateur(utilisateurId);
	}

	/**
	 * Validation de l'objet Article.
	 * 
	 * @param article Objet à valider.
	 * @param be      BusinessException pour collecter les erreurs.
	 * @return true si l'objet est valide, false sinon.
	 */
	private boolean validerArticle(Article article, BusinessException be) {
		boolean isValid = true;

		if (article == null) {
			be.add(BusinessCode.VALIDATION_ARTICLE_NULL);
			return false;
		}

		isValid &= validerNom(article.getNom(), be);
		isValid &= validerDescription(article.getDescription(), be);
		isValid &= validerDateDebut(article.getDate_debut(), be);
		isValid &= validerPrix(article.getPrix_initial(), be);
		isValid &= validerAdresse(article.getAdresse(), be);
		isValid &= validerStatutEnchere(article.getStatut_enchere(), be);
//		isValid &= validerPathImage(article.getPath_image(), be);

		return isValid;
	}

	private boolean validerNom(String nom, BusinessException be) {
		if (nom == null) {
			be.add(BusinessCode.VALIDATION_ARTICLE_NOM_NULL);
			return false;
		}
		if (nom.isBlank()) {
			be.add(BusinessCode.VALIDATION_ARTICLE_NOM_BLANK);
			return false;
		}
		if (nom.length() > 30) {
			be.add(BusinessCode.VALIDATION_ARTICLE_NOM_LONGUEUR);
			return false;
		}
		return true;
	}

	private boolean validerDescription(String description, BusinessException be) {
		if (description == null) {
			be.add(BusinessCode.VALIDATION_ARTICLE_DESCRIPTION_NULL);
			return false;
		}
		if (description.isBlank()) {
			be.add(BusinessCode.VALIDATION_ARTICLE_DESCRIPTION_BLANK);
			return false;
		}
		if (description.length() > 300) {
			be.add(BusinessCode.VALIDATION_ARTICLE_DESCRIPTION_LONGUEUR);
			return false;
		}
		return true;
	}

	private boolean validerDateDebut(LocalDateTime dateDebut, BusinessException be) {
		if (dateDebut == null) {
			be.add(BusinessCode.VALIDATION_ARTICLE_DATE_DEBUT_NULL);
			return false;
		}
		return true;
	}

	private boolean validerPrix(int prix, BusinessException be) {
		if (prix <= 0) {
			be.add(BusinessCode.VALIDATION_ARTICLE_PRIX_NEGATIF);
			return false;
		}
		return true;
	}

	private boolean validerAdresse(Adresse adresse, BusinessException be) {
		if (adresse == null) {
			be.add(BusinessCode.VALIDATION_ARTICLE_ADRESSE_NULL);
			return false;
		}
		return true;
	}

	private boolean validerStatutEnchere(StatutEnchere statut, BusinessException be) {
		if (statut == null) {
			be.add(BusinessCode.VALIDATION_ARTICLE_STATUT_ENCHERE_NULL);
			return false;
		}
		return true;
	}

	private boolean validerPathImage(String pathImage, BusinessException be) {
		if (pathImage == null) {
			be.add(BusinessCode.VALIDATION_ARTICLE_PATH_IMAGE_NULL);
			return false;
		}
		if (pathImage.isBlank()) {
			be.add(BusinessCode.VALIDATION_ARTICLE_PATH_IMAGE_BLANK);
			return false;
		}
		if (pathImage.length() > 300) {
			be.add(BusinessCode.VALIDATION_ARTICLE_PATH_IMAGE_LONGUEUR);
			return false;
		}
		return true;
	}
}
