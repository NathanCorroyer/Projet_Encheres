package fr.eni.projet.bll.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.eni.projet.DAL.AdresseDAO;
import fr.eni.projet.DAL.ArticleDAO;
import fr.eni.projet.DAL.EnchereDAO;
import fr.eni.projet.DAL.UtilisateurDAO;
import fr.eni.projet.bll.UtilisateurService;
import fr.eni.projet.bo.Adresse;
import fr.eni.projet.bo.Article;
import fr.eni.projet.bo.Enchere;
import fr.eni.projet.bo.Utilisateur;
import fr.eni.projet.exceptions.BusinessCode;
import fr.eni.projet.exceptions.BusinessException;
import fr.eni.projet.service.SessionService;

@Service
public class UtilisateurServiceImpl implements UtilisateurService {

	@Autowired
	private UtilisateurDAO utilisateurDAO;

	@Autowired
	private AdresseDAO adresseDAO;
	
	@Autowired
	private EnchereDAO enchereDAO;

	@Autowired
	private ArticleDAO articleDAO;
	
	@Autowired
	private SessionService sessionService;
	

	@Override
	@Transactional
	public void create(Utilisateur utilisateur) {
		// Validation complète avant création
		BusinessException be = new BusinessException();
		if (!validerUtilisateur(utilisateur, be)) {
			throw be;
		}
		System.out.println("salut je passe ici");

		Adresse adresse = utilisateur.getAdresse();
		if (adresse == null) {
			throw new IllegalArgumentException("L'adresse est obligatoire pour créer un utilisateur.");
		}
		// Créer l'adresse et récupérer la clé générée
		int adresseKey = adresseDAO.create(adresse);
		utilisateur.getAdresse().setId(adresseKey);

		if (!be.isValid()) {
			throw be;
		}
		
		// Hachage du mot de passe
		String password = PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(utilisateur.getPassword());

		utilisateur.setPassword(password);

		// Crédit initial
		utilisateur.setCredit(10);

		utilisateurDAO.create(utilisateur);
	}

	@Override
	public Utilisateur findById(int id) {

		if (id <= 0) {
			throw new IllegalArgumentException("L'ID doit être supérieur à 0.");
		}
		Utilisateur user = utilisateurDAO.findById(id);
		Adresse adresse = adresseDAO.findById(user.getAdresse().getId());
		user.setAdresse(adresse);
		return user;
	}

	@Override
	public Optional<Utilisateur> findByEmail(String email) {
		if (email == null || email.isBlank()) {
			throw new IllegalArgumentException("L'Email ne peut pas être vide.");
		}
		return utilisateurDAO.findByEmail(email);
	}

	@Override
	public Optional<Utilisateur> findByPseudo(String pseudo) {
		if (pseudo == null || pseudo.isBlank()) {
			throw new IllegalArgumentException("Le pseudo ne peut pas être vide.");
		}

		Optional<Utilisateur> user = utilisateurDAO.findByPseudo(pseudo);
		if (user.isEmpty()) { // Vérifie si l'Optional est vide
			return Optional.empty(); // Retourne un Optional vide si aucun utilisateur n'est trouvé
		}

		// Récupère l'utilisateur contenu dans l'Optional
		Utilisateur utilisateur = user.get();

		// Récupère l'adresse associée à l'utilisateur
		Adresse adresse = adresseDAO.findById(utilisateur.getAdresse().getId());

		// Assigne l'adresse à l'utilisateur
		utilisateur.setAdresse(adresse);

		// Retourne l'utilisateur avec son adresse
		return Optional.of(utilisateur);
	}

	@Override
	public List<Utilisateur> findAll() {
		return utilisateurDAO.findAll();
	}

	@Override
	public String findPassword(String pseudo) {
		return utilisateurDAO.findPassword(pseudo);
	}

	@Override
	public void update(Utilisateur utilisateur, boolean isEmailModifie) {
		// Validation complète avant mise à jour
		BusinessException be = new BusinessException();
		validerUtilisateurUpdate(utilisateur, isEmailModifie, be);

		if (!be.isValid()) {
			throw be;
		}

		adresseDAO.update(utilisateur.getAdresse());
		// Mise à jour de l'utilisateur
		utilisateurDAO.update(utilisateur);
	}

	@Override
	public void updatePassword(Utilisateur utilisateur, String currentPassword, String newPassword,
			String confirmPassword) {
		// Validation complète avant mise à jour
		BusinessException be = new BusinessException();
		utilisateur.setPassword(findPassword(utilisateur.getPseudo()));
		validerPassword(newPassword, be);
		validerConfirmPassword(newPassword, confirmPassword, be);
		isSameAsCurrentPassword(currentPassword, utilisateur.getPassword(), be);

		if (!be.isValid()) {
			throw be;
		}
		newPassword = PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(newPassword);
		utilisateur.setPassword(newPassword);
		utilisateurDAO.updatePassword(utilisateur);
	}

	private boolean validerConfirmPassword(String newPassword, String confirmPassword, BusinessException be) {
		boolean isValid = true;

		if (!newPassword.equals(confirmPassword)) {
			be.add(BusinessCode.VALIDATION_UTILISATEUR_PASSWORD_NON_IDENTIQUES);
			isValid = false;
		}
		return isValid;
	}

	private boolean isSameAsCurrentPassword(String password, String currentPassword, BusinessException be) {
		boolean isValid = PasswordEncoderFactories.createDelegatingPasswordEncoder().matches(password, currentPassword);
		if (!isValid) {
			be.add(BusinessCode.VALIDATION_UTILISATEUR_PASSWORD_INCORRECT);
		}
		return isValid;
	}

	@Override
	public boolean modifierActivation(Utilisateur utilisateur) {
		if (utilisateur == null || utilisateur.getId() <= 0) {
			throw new IllegalArgumentException("L'utilisateur est invalide ou non identifié.");
		}
		
		//Si l'utilisateur va être désactivé, supprimer tous les articles créés par cet utilisateur ainsi que toutes ses encheres
		if(utilisateur.isActif()) {
			gererCascadeDesactivation(utilisateur);
		}
		return utilisateurDAO.modifierActivation(utilisateur); // Modification de l'état d'activation
	}

	@Transactional
	@Override
	public boolean supprimerUser(int id) {
		//On récupère tous les articles de l'utilisateur à supprimer
		List<Article> articles = articleDAO.findByUtilisateur(id);
		//Pour chacun de ces articles, on rend le crédit à l'utilisateur ayant la plus grosse enchère
		rendreCredit(articles);
		//Et on supprime toutes les enchères de l'utilisateur (pas besoin de lui rendre ses crédits car compte supprimé?)
		enchereDAO.deleteEncheresFromUser(id);
		return utilisateurDAO.delete(id) ;
	}
	
	private void gererCascadeDesactivation(Utilisateur utilisateur) {
		//On récupère tous les articles de l'utilisateur à supprimer
		List<Article> articles = articleDAO.findByUtilisateur(utilisateur.getId());
		//Pour chacun de ces articles, on rend les crédits au plus gros enchérisseur
		rendreCredit(articles);
		/*
		 * Et on appelle les DAO pour gérer les suppressions en cascade
		 * Sachant que la cascade est également gérée côté SQL, donc ce n'est pas forcément nécessaire ici, mais mesure de sécurité?
		 */
		articleDAO.deleteFromUser(utilisateur.getId());
		enchereDAO.deleteEncheresFromUser(utilisateur.getId());
	}
	
	private void rendreCredit(List<Article> articles) {
		//Récupération de l'utilisateur en session
		Utilisateur userConnecte = sessionService.getUserSessionAttribute();
		//Pour chacun des articles de la liste passée en paramètre
		articles.forEach( article-> {
			//On récupère l'enchère la plus haute, gérée avec un Optional
			Optional<Enchere> enchere = enchereDAO.findBiggestEnchereFromArticle(article.getId());
			//Si l'enchère existe, on rend les crédits à l'enchérisseur
			if(!enchere.isEmpty()) {
				if(userConnecte.getId() == enchere.get().getAcheteur().getId()) {
					//Directement à l'utilisateur connecté si toutefois c'est lui
					userConnecte.setCredit(userConnecte.getCredit() + enchere.get().getMontant());
					utilisateurDAO.updateCredit(userConnecte);
				}else {					
					Utilisateur user = utilisateurDAO.findById(enchere.get().getAcheteur().getId());
					user.setCredit(user.getCredit() + enchere.get().getMontant());
					utilisateurDAO.updateCredit(user);
				}
			}
		});
	}

	@Override
	public void updateCredit(Utilisateur utilisateur) {
		utilisateurDAO.updateCredit(utilisateur);
	}

	/**
	 * Valide un objet Utilisateur selon les règles métier définies.
	 *
	 * @param utilisateur L'utilisateur à valider.
	 * @param be          BusinessException pour collecter les erreurs.
	 * @return true si l'utilisateur est valide, false sinon.
	 */
	private boolean validerUtilisateur(Utilisateur utilisateur, BusinessException be) {
		boolean isValid = true;

		if (utilisateur == null) {
			be.add(BusinessCode.VALIDATION_UTILISATEUR_NULL);
			return false;
		}

		isValid &= validerPseudo(utilisateur.getPseudo(), be);
		isValid &= validerPseudoUnique(utilisateur.getPseudo(), be);
		isValid &= validerPassword(utilisateur.getPassword(), be);
		isValid &= validerEmail(utilisateur.getEmail(), be);
		isValid &= validerEmailUnique(utilisateur.getEmail(), be);
		isValid &= validerCredit(utilisateur.getCredit(), be);
		isValid &= validerAdresse(utilisateur.getAdresse(), be);

		return isValid;
	}

	private boolean validerUtilisateurUpdate(Utilisateur utilisateur, boolean isEmailModifie, BusinessException be) {
		boolean isValid = true;

		isValid &= validerEmail(utilisateur.getEmail(), be);
		if (isEmailModifie) {
			isValid &= validerEmailUnique(utilisateur.getPseudo(), be);
		}
		isValid &= validerCredit(utilisateur.getCredit(), be);
		isValid &= validerAdresse(utilisateur.getAdresse(), be);
		return isValid;
	}

	public boolean validerPseudo(String pseudo, BusinessException be) {
		boolean isValid = true;
		if (pseudo == null || pseudo.isBlank()) {
			be.add(BusinessCode.VALIDATION_UTILISATEUR_PSEUDO_BLANK);
			isValid = false;
		} else if (!pseudo.matches("^[a-zA-Z0-9_]+$")) {
			be.add(BusinessCode.VALIDATION_UTILISATEUR_PSEUDO_FORMAT);
			isValid = false;
		} else if (pseudo.length() > 30) {
			be.add(BusinessCode.VALIDATION_UTILISATEUR_PSEUDO_LONGUEUR);
			isValid = false;
		}

		return isValid;
	}

	public boolean validerPseudoUnique(String pseudo, BusinessException be) {
		boolean isValid = true;
		Optional<Utilisateur> existingUserByPseudo = utilisateurDAO.findByPseudo(pseudo);
		if (existingUserByPseudo.isPresent()) {
			isValid = false;
			be.add(BusinessCode.VALIDATION_UTILISATEUR_PSEUDO_EXISTANT);
		}
		return isValid;
	}

	private boolean validerPassword(String password, BusinessException be) {
		boolean isValid = true;
		if (password == null || password.isBlank()) {
			be.add(BusinessCode.VALIDATION_UTILISATEUR_PASSWORD_BLANK);
			isValid = false;
		} else if (!password.matches("^(?=.*[A-Z])(?=.*[\\d])(?=.*[\\W])[A-Za-z\\d\\W]{8,20}$")) {
			// Validation du format du mot de passe
			be.add(BusinessCode.VALIDATION_UTILISATEUR_PASSWORD_FORMAT);
			isValid = false;
		}
		return isValid;
	}

	private boolean validerEmail(String email, BusinessException be) {
		boolean isValid = true;
		if (email == null || email.isBlank()) {
			be.add(BusinessCode.VALIDATION_UTILISATEUR_EMAIL_BLANK);
			isValid = false;
		} else if (!email.matches("^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
			be.add(BusinessCode.VALIDATION_UTILISATEUR_EMAIL_FORMAT);
			isValid = false;
		}
		return isValid;
	}

	private boolean validerEmailUnique(String email, BusinessException be) {
		boolean isValid = true;
		Optional<Utilisateur> existingUserByEmail = utilisateurDAO.findByEmail(email);
		if (!existingUserByEmail.isEmpty()) {
			isValid = false;
			be.add(BusinessCode.VALIDATION_UTILISATEUR_EMAIL_EXISTANT);
		}
		return isValid;
	}

	private boolean validerAdresse(Adresse adresse, BusinessException be) {
		boolean isValid = true;
		if (adresse == null) {
			be.add(BusinessCode.VALIDATION_UTILISATEUR_ADRESSE_NULL);
			isValid = false;
		}
		return isValid;
	}

	private boolean validerCredit(int credit, BusinessException be) {
		boolean isValid = true;
		if (credit < 0) {
			be.add(BusinessCode.VALIDATION_UTILISATEUR_CREDIT_NEGATIF);
			isValid = false;
		}
		return isValid;
	}

	
	
}