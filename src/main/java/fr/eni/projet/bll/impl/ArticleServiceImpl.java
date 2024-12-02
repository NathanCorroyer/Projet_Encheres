package fr.eni.projet.bll.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.eni.projet.DAL.AdresseDAO;
import fr.eni.projet.DAL.ArticleDAO;
import fr.eni.projet.DAL.CategorieDAO;
import fr.eni.projet.DAL.EnchereDAO;
import fr.eni.projet.DAL.UtilisateurDAO;
import fr.eni.projet.bll.ArticleService;
import fr.eni.projet.bo.Adresse;
import fr.eni.projet.bo.Article;
import fr.eni.projet.bo.Categorie;
import fr.eni.projet.bo.Enchere;
import fr.eni.projet.bo.Utilisateur;
import fr.eni.projet.enums.StatutEnchere;
import fr.eni.projet.exceptions.BusinessCode;
import fr.eni.projet.exceptions.BusinessException;

@Service
public class ArticleServiceImpl implements ArticleService {

	@Autowired
	private ArticleDAO articleDAO;
	@Autowired
	private UtilisateurDAO userDAO;
	@Autowired
	private EnchereDAO enchereDAO;

	@Autowired
	private CategorieDAO categorieDAO;

	@Autowired
	private AdresseDAO adresseDAO;

	@Override
	public int create(Article article) {
		BusinessException be = new BusinessException();

		if (!validerArticle(article, be)) {
			System.err.println("Erreur de validation pour l'article: " + article);
			throw be;
		}

		try {
			System.out.println("L'article est valide. Tentative de création dans la base de données.");
			System.out.println("L'article a été créé avec succès: " + article);
			return articleDAO.create(article);
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
		Article article = articleDAO.findArticleById(id);
		if (article != null) {
			Optional<Categorie> categorieOptional = categorieDAO.findById(article.getCategorie().getId());
			if (categorieOptional.isPresent()) {
				article.setCategorie(categorieOptional.get());
			}
			Utilisateur utilisateur = userDAO.findById(article.getProprietaire().getId());
			article.setProprietaire(utilisateur);
			Adresse adresse = adresseDAO.findById(article.getAdresse().getId());
			article.setAdresse(adresse);
		}

		return article;
	}

	@Override
	public List<Article> findAll() {
		return articleDAO.findAll();
	}

	@Override
	public List<Article> findAllActive() {
		List<Article> articles = articleDAO.findAllActive();
		articles.forEach(article -> {
			Utilisateur user = userDAO.findById(article.getProprietaire().getId());
			article.setProprietaire(user);
			Enchere enchere = enchereDAO.findBiggestEnchereFromArticle(article.getId());
			if (enchere != null) {
				article.setPrix_vente(enchere.getMontant());
			}
		});
		return articles;
	}

	@Override
	public void update(Article article) {
		BusinessException be = new BusinessException();
		if (!validerArticle(article, be)) {
			throw be;
		}
		articleDAO.update(article);
	}

	@Override
	public void delete(int id) {
		if (id <= 0) {
			throw new IllegalArgumentException("L'ID de l'article doit être supérieur à 0.");
		}

		// Récupération de l'article
		Article article = articleDAO.findArticleById(id);
		if (article == null) {
			throw new IllegalArgumentException("L'article avec cet ID n'existe pas.");
		}

		// Validation de la date de début
		BusinessException be = new BusinessException();
		if (!validerDateDebut(article.getDate_debut(), be)) {
			throw be; // Lancer une exception avec les messages de validation
		}

		// Suppression si toutes les validations passent
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

	
	@Override
	public void uploadImage(String fileName, int idArticle) {
		articleDAO.uploadImage(fileName,idArticle);
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
//		isValid &= validerStatutEnchere(article.getStatut_enchere(), be);
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
		if (dateDebut == null || dateDebut.isBefore(LocalDateTime.now())) {
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
