package fr.eni.projet.exceptions;

public class BusinessCode {

//Clefs de validation des utilisateurs
//Clefs de validation des Catégories
//Clefs de validation des articles
	public static final String VALIDATION_ARTICLE_NULL = "L'article ne peut pas être null.";
	public static final String VALIDATION_ARTICLE_NOM_NULL = "Le nom de l'article ne peut pas être null.";
	public static final String VALIDATION_ARTICLE_NOM_BLANK = "Le nom de l'article ne peut pas être vide.";
	public static final String VALIDATION_ARTICLE_NOM_LONGUEUR = "Le nom de l'article ne peut pas dépasser 30 caractères.";
	public static final String VALIDATION_ARTICLE_DESCRIPTION_NULL = "La description de l'article ne peut pas être null.";
	public static final String VALIDATION_ARTICLE_DESCRIPTION_BLANK = "La description de l'article ne peut pas être vide.";
	public static final String VALIDATION_ARTICLE_DESCRIPTION_LONGUEUR = "La description de l'article ne peut pas dépasser 300 caractères.";
	public static final String VALIDATION_ARTICLE_DATE_DEBUT_NULL = "La date de début de l'enchère ne peut pas être null.";
	public static final String VALIDATION_ARTICLE_PRIX_NEGATIF = "Le prix de l'article ne peut pas être négatif.";
	public static final String VALIDATION_ARTICLE_ADRESSE_NULL = "L'adresse de l'article ne peut pas être null.";
	public static final String VALIDATION_ARTICLE_STATUT_ENCHERE_NULL = "Le statut de l'enchère ne peut pas être null.";
	public static final String VALIDATION_ARTICLE_PATH_IMAGE_NULL = "Le chemin de l'image de l'article ne peut pas être null.";
	public static final String VALIDATION_ARTICLE_PATH_IMAGE_BLANK = "Le chemin de l'image de l'article ne peut pas être vide.";
	public static final String VALIDATION_ARTICLE_PATH_IMAGE_LONGUEUR = "Le chemin de l'image ne peut pas dépasser 300 caractères.";

//Clefs de validation des adresses
	public static final String VALIDATION_ADRESSE_NULL = "L'adresse ne peut pas être nulle.";
	public static final String VALIDATION_ADRESSE_RUE_BLANK = "Le champ rue ne peut pas être vide.";
	public static final String VALIDATION_ADRESSE_RUE_LONGUEUR = "La rue ne peut pas dépasser 100 caractères.";
	public static final String VALIDATION_ADRESSE_CODEPOSTAL_BLANK = "Le code postal ne peut pas être vide.";
	public static final String VALIDATION_ADRESSE_CODEPOSTAL_FORMAT = "Le code postal doit comporter exactement 5 chiffres.";
	public static final String VALIDATION_ADRESSE_VILLE_BLANK = "Le champ ville ne peut pas être vide.";
	public static final String VALIDATION_ADRESSE_VILLE_LONGUEUR = "La ville ne peut pas dépasser 50 caractères.";
	// Validation du champ adresse_eni
	public static final String VALIDATION_ADRESSE_ENI_NULL = "Le champ adresse_eni ne peut pas être nul.";

//Clefs de validation des encheres

	public static final String BLL_UTILISATEUR_CREATE_ERREUR = "bll.user.create.erreur";
	public static final String BLL_AVIS_CREATE_ERREUR = "bll.avis.create.erreur";;

//Clefs de validation d'un avis

}
