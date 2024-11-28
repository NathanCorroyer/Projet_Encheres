package fr.eni.projet.exceptions;

public class BusinessCode {

	// Clefs de validation des utilisateurs
	public static final String VALIDATION_UTILISATEUR_NULL = "validation.utilisateur.null";
	public static final String VALIDATION_UTILISATEUR_PSEUDO_BLANK = "validation.utilisateur.pseudo.blank";
	public static final String VALIDATION_UTILISATEUR_PSEUDO_FORMAT = "validation.utilisateur.pseudo.format";
	public static final String VALIDATION_UTILISATEUR_PSEUDO_LONGUEUR = "validation.utilisateur.pseudo.longueur";
	public static final String VALIDATION_UTILISATEUR_PSEUDO_EXISTANT = "validation.utilisateur.pseudo.existant";
	public static final String VALIDATION_UTILISATEUR_PASSWORD_BLANK = "validation.utilisateur.password.blank";
	public static final String VALIDATION_UTILISATEUR_PASSWORD_FORMAT = "validation.utilisateur.password.format";
	public static final String VALIDATION_UTILISATEUR_PASSWORD_NON_IDENTIQUES = "validation.utilisateur.password.non.identiques";
	public static final String VALIDATION_UTILISATEUR_PASSWORD_INCORRECT = "validation.utilisateur.password.incorrect";
	public static final String VALIDATION_UTILISATEUR_EMAIL_BLANK = "validation.utilisateur.email.blank";
	public static final String VALIDATION_UTILISATEUR_EMAIL_FORMAT = "validation.utilisateur.email.format";
	public static final String VALIDATION_UTILISATEUR_EMAIL_EXISTANT = "validation.utilisateur.email.existant";
	public static final String VALIDATION_UTILISATEUR_CREDIT_NEGATIF = "validation.utilisateur.credit.negative";
	public static final String VALIDATION_UTILISATEUR_ADRESSE_NULL = "validation.utilisateur.adresse.null";

	// Clefs de validation des articles
	public static final String VALIDATION_ARTICLE_NULL = "validation.article.null";
	public static final String VALIDATION_ARTICLE_NOM_NULL = "validation.article.nom.null";
	public static final String VALIDATION_ARTICLE_NOM_BLANK = "validation.article.nom.blank";
	public static final String VALIDATION_ARTICLE_NOM_LONGUEUR = "validation.article.nom.longueur";
	public static final String VALIDATION_ARTICLE_DESCRIPTION_NULL = "validation.article.description.null";
	public static final String VALIDATION_ARTICLE_DESCRIPTION_BLANK = "validation.article.description.blank";
	public static final String VALIDATION_ARTICLE_DESCRIPTION_LONGUEUR = "validation.article.description.longueur";
	public static final String VALIDATION_ARTICLE_DATE_DEBUT_NULL = "validation.article.date.debut.null";
	public static final String VALIDATION_ARTICLE_PRIX_NEGATIF = "validation.article.prix.negatif";
	public static final String VALIDATION_ARTICLE_ADRESSE_NULL = "validation.article.adresse.null";
	public static final String VALIDATION_ARTICLE_STATUT_ENCHERE_NULL = "validation.article.statut.enchere.null";
	public static final String VALIDATION_ARTICLE_PATH_IMAGE_NULL = "validation.article.path.image.null";
	public static final String VALIDATION_ARTICLE_PATH_IMAGE_BLANK = "validation.article.path.image.blank";
	public static final String VALIDATION_ARTICLE_PATH_IMAGE_LONGUEUR = "validation.article.path.image.longueur";

	// Clefs de validation des adresses
	public static final String VALIDATION_ADRESSE_NULL = "validation.adresse.null";
	public static final String VALIDATION_ADRESSE_RUE_BLANK = "validation.adresse.rue.blank";
	public static final String VALIDATION_ADRESSE_RUE_LONGUEUR = "validation.adresse.rue.longueur";
	public static final String VALIDATION_ADRESSE_CODEPOSTAL_BLANK = "validation.adresse.codepostal.blank";
	public static final String VALIDATION_ADRESSE_CODEPOSTAL_FORMAT = "validation.adresse.codepostal.format";
	public static final String VALIDATION_ADRESSE_VILLE_BLANK = "validation.adresse.ville.blank";
	public static final String VALIDATION_ADRESSE_VILLE_LONGUEUR = "validation.adresse.ville.longueur";
	public static final String VALIDATION_ADRESSE_ENI_NULL = "validation.adresse.eni.null";

	// Clefs de validation des encheres
	public static final String BLL_UTILISATEUR_CREATE_ERREUR = "bll.utilisateur.create.erreur";
	public static final String BLL_AVIS_CREATE_ERREUR = "bll.avis.create.erreur";
}
