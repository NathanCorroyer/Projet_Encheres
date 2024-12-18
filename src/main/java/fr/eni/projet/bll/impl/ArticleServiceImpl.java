package fr.eni.projet.bll.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import fr.eni.projet.service.SessionService;

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
	

	@Autowired
	private MessageSource message;

	@Autowired
	private SessionService sessionService;

	private Locale locale;

	public ArticleServiceImpl() {
		this.locale = LocaleContextHolder.getLocale();
	}

	public void marquerCommeLivre(int id) {
		// Récupérer l'article
		Article article = articleDAO.findArticleById(id);
		if (article == null) {
			throw new IllegalArgumentException("Article non trouvé.");
		}

		// Vérifier que le statut actuel est CLOTURE
		if (article.getStatut_enchere() != StatutEnchere.CLOTUREE) {
			throw new IllegalStateException("L'article doit être clôturé avant de passer à LIVREE.");
		}

		Utilisateur vendeur = sessionService.getUserSessionAttribute();
		int creditsGagnes = article.getPrix_vente();

		// Mettre à jour le statut
		article.setStatut_enchere(StatutEnchere.LIVREE);

		// Créditer le vendeur
		vendeur.setCredit(vendeur.getCredit() + creditsGagnes);
		userDAO.updateCredit(vendeur);

		// Sauvegarder les modifications
		articleDAO.updateStatutEnchere(article, StatutEnchere.LIVREE);

	}

	public String titre(Article article, boolean isEncherisseur, String pseudoWinner, boolean hasEnchere) {

		String titre = message.getMessage("detail-article.h1", null, locale);

		if (article.getStatut_enchere().getValue() >= 2) {
			if (hasEnchere) {
				if (isEncherisseur) {
					titre = message.getMessage("detail-article-winner.h1", null, locale);
				} else {
					titre = message.getMessage("detail-article-looser.h1", new Object[] { pseudoWinner }, locale);
				}
			} else {
				titre = message.getMessage("detail-article-none.h1", null, locale);
			}

		}
		return titre;

	}

	public boolean canShowRetraitButton(Article article) {
		// Vérifie si l'article est CLOTUREE et s'il a des enchères
		return article.getStatut_enchere() == StatutEnchere.CLOTUREE && articleDAO.hasEncheres(article.getId());
	}

	@Scheduled(cron = "0 30 9 * * ?") // Expression cron : Tous les jours à 9h30
	@Transactional
	@Override
	public void activerEncheresDuJour() {
		LocalDate today = LocalDate.now();
		LocalDateTime startOfDay = today.atStartOfDay();

		List<Article> articlesADemarrer = articleDAO.findByDateDebutAndStatutEnchere(startOfDay,
				StatutEnchere.PAS_COMMENCEE.ordinal());

		for (Article article : articlesADemarrer) {
			article.setStatut_enchere(StatutEnchere.EN_COURS);
			articleDAO.updateStatutEnchere(article, StatutEnchere.EN_COURS);
		}

		System.out.println(articlesADemarrer.size() + " enchères activées pour la date : " + today);

	}

	@Scheduled(cron = "0 30 9 * * ?") // Exécution quotidienne
	@Transactional
	@Override
	public void cloturerEncheresDuJour() {
		LocalDate today = LocalDate.now(); // Date actuelle sans l'heure
		LocalDateTime startOfDay = today.atStartOfDay();
		List<Article> articlesACloturer = articleDAO.findByDateFinBeforeAndStatutEnchere(startOfDay,
				StatutEnchere.EN_COURS.ordinal());

		for (Article article : articlesACloturer) {
			// Récupérer l'enchère la plus élevée pour cet article
			Optional<Enchere> enchereMaxOpt = enchereDAO.findBiggestEnchereFromArticle(article.getId());
			Enchere enchereMax = enchereMaxOpt.get();
			article.setPrix_vente(enchereMax.getMontant());

			article.setStatut_enchere(StatutEnchere.CLOTUREE); // Changer le statut à CLOTUREE
			articleDAO.updateStatutEnchere(article, StatutEnchere.CLOTUREE);
		}

		System.out.println(articlesACloturer.size() + " enchères clôturées pour la date : " + today);
	}

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
			Categorie categorie = categorieDAO.findById(article.getCategorie().getId());
			article.setCategorie(categorie);
			Utilisateur utilisateur = userDAO.findById(article.getProprietaire().getId());
			article.setProprietaire(utilisateur);
			Adresse adresse = adresseDAO.findById(article.getAdresse().getId());
			article.setAdresse(adresse);
		}

		return article;
	}

	@Override
	public List<Article> findAll() {
		List<Article> articles = articleDAO.findAll();
		alimenterArticles(articles);
		return articles;
	}

	public List<Article> findAllWithEncheres(int userId){
		var articles = articleDAO.findAllActiveWithEncheres(userId);
		alimenterArticles(articles);
		return articles;
	}
	
	@Override
	public List<Article> findAllWithEncheresFinies(int userId) {
		var articles = articleDAO.findAllFiniesWithEncheres(userId);
		alimenterArticles(articles);
		articles = articles.stream().filter(article -> {
			Enchere enchere = enchereDAO.findBiggestEnchereFromArticle(article.getId()).get();
			return userId == enchere.getAcheteur().getId();
		}).collect(Collectors.toList());
		return articles;
	}
	
	public List<Categorie> findAllCategories() {
		return categorieDAO.findAll();
	}

	@Override
	public List<Article> findAllActive() {
		List<Article> articles = articleDAO.findAllActive();
		articles.forEach(article -> {
			Utilisateur user = userDAO.findById(article.getProprietaire().getId());
			article.setProprietaire(user);
			Optional<Enchere> enchere = enchereDAO.findBiggestEnchereFromArticle(article.getId());
			if (!enchere.isEmpty()) {
				article.setPrix_vente(enchere.get().getMontant());
			}
		});
		return articles;
	}

	
	@Override
	public List<Article> findEnCoursFromVendeur(int id) {
		var articles = articleDAO.findEnCoursFromVendeur(id);
		alimenterArticles(articles);
		return articles;
	}

	@Override
	public List<Article> findNonCommenceeFromVendeur(int id) {
		var articles = articleDAO.findNonCommenceeFromVendeur(id);
		alimenterArticles(articles);
		return articles;
	}

	@Override
	public List<Article> findFiniesFromVendeur(int id) {
		var articles = articleDAO.findFiniesFromVendeur(id);
		alimenterArticles(articles);
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
		if (categorieId <= 0) {
			return articleDAO.findByCategorie(categorieId);
		} else {
			return articleDAO.findAll();
		}
	}

	@Override
	public List<Article> findByNom(String nom) {
		return articleDAO.findByNom(nom);
	}

	@Override
	public List<Article> findByUtilisateur(int utilisateurId) {
		return articleDAO.findByUtilisateur(utilisateurId);
	}


	public List<Article> filterByCategorieAndNom(List<Article> articles, Long categorieId, String nom) {
		return articles.stream()
				.filter(article -> (categorieId == null || article.getCategorie().getId() == categorieId.intValue())
						&& (nom == null || article.getNom().toLowerCase().contains(nom.toLowerCase())))
				.collect(Collectors.toList());
	}

	@Override
	public void uploadImage(String fileName, int idArticle) {
		articleDAO.uploadImage(fileName, idArticle);
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


	private void alimenterArticles(List<Article> articles) {
		articles.forEach(article -> {
			Utilisateur user = userDAO.findById(article.getProprietaire().getId());
			article.setProprietaire(user);
			Optional<Enchere> enchere = enchereDAO.findBiggestEnchereFromArticle(article.getId());
			if (!enchere.isEmpty()) {
				article.setPrix_vente(enchere.get().getMontant());
			}
		});
	}


}
