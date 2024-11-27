package fr.eni.projet.bll.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.eni.projet.DAL.AdresseDAO;
import fr.eni.projet.DAL.UtilisateurDAO;
import fr.eni.projet.bll.UtilisateurService;
import fr.eni.projet.bo.Adresse;
import fr.eni.projet.bo.Utilisateur;
import fr.eni.projet.exceptions.BusinessCode;
import fr.eni.projet.exceptions.BusinessException;

@Service
public class UtilisateurServiceImpl implements UtilisateurService {

	@Autowired
	UtilisateurDAO utilisateurDAO;

	@Autowired
	private AdresseDAO adresseDAO;

	@Override
	@Transactional
	public void create(Utilisateur utilisateur) {
		// Validation complète avant création
		BusinessException be = new BusinessException();
		if (!validerUtilisateur(utilisateur, be)) {
			throw be;
		}

		Adresse adresse = utilisateur.getAdresse();
		if (adresse == null) {
			throw new IllegalArgumentException("L'adresse est obligatoire pour créer un utilisateur.");
		}

		int adresseKey = adresseDAO.create(adresse); // Création de l'adresse
		utilisateur.getAdresse().setId(adresseKey);

		// Vérification des contraintes de pseudo et email uniques
		if (utilisateurDAO.findByPseudo(utilisateur.getPseudo()) != null) {
			be.add(BusinessCode.VALIDATION_UTILISATEUR_PSEUDO_EXISTANT);
		}
		if (utilisateurDAO.findByEmail(utilisateur.getEmail()) != null) {
			be.add(BusinessCode.VALIDATION_UTILISATEUR_EMAIL_EXISTANT);
		}

		if (be.isValid()) {
			throw be;
		}
		utilisateur.setCredit(10);
		utilisateurDAO.create(utilisateur); // Création de l'utilisateur
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
	public void update(Utilisateur utilisateur) {
		// Validation complète avant mise à jour
		BusinessException be = new BusinessException();
		if (!validerUtilisateur(utilisateur, be)) {
			throw be;
		}

		// Vérification des contraintes pour pseudo et email uniques
		Optional<Utilisateur> existingUserByPseudo = utilisateurDAO.findByPseudo(utilisateur.getPseudo());
		if (existingUserByPseudo.isPresent() && existingUserByPseudo.get().getId() != utilisateur.getId()) {
			be.add(BusinessCode.VALIDATION_UTILISATEUR_PSEUDO_EXISTANT);
		}

		Optional<Utilisateur> existingUserByEmail = utilisateurDAO.findByEmail(utilisateur.getEmail());
		if (existingUserByEmail != null && existingUserByEmail.get().getId() != utilisateur.getId()) {
			be.add(BusinessCode.VALIDATION_UTILISATEUR_EMAIL_EXISTANT);
		}

		// Si des erreurs sont trouvées, les lancer
		if (!be.isValid()) {
			throw be;
		}

		// Mise à jour de l'utilisateur
		utilisateurDAO.update(utilisateur);
	}

	@Override
	public void modifierActivation(Utilisateur utilisateur) {
		if (utilisateur == null || utilisateur.getId() <= 0) {
			throw new IllegalArgumentException("L'utilisateur est invalide ou non identifié.");
		}
		utilisateurDAO.modifierActivation(utilisateur); // Modification de l'état d'activation
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

		// Validation du pseudo
		if (utilisateur.getPseudo() == null || utilisateur.getPseudo().isBlank()) {
			be.add(BusinessCode.VALIDATION_UTILISATEUR_PSEUDO_BLANK);
			isValid = false;
		} else if (!utilisateur.getPseudo().matches("^[a-zA-Z0-9_]+$")) {
			be.add(BusinessCode.VALIDATION_UTILISATEUR_PSEUDO_FORMAT);
			isValid = false;
		} else if (utilisateur.getPseudo().length() > 30) {
			be.add(BusinessCode.VALIDATION_UTILISATEUR_PSEUDO_LONGUEUR);
			isValid = false;
		}

		// Validation du mot de passe
		if (utilisateur.getPassword() == null || utilisateur.getPassword().isBlank()) {
			be.add(BusinessCode.VALIDATION_UTILISATEUR_PASSWORD_BLANK);
			isValid = false;
		} else if (utilisateur.getPassword().startsWith("{bcrypt}")) {
			// Si c'est déjà un hash bcrypt, on ignore la validation regex.
			System.out.println("Mot de passe déjà haché avec bcrypt, ignorer la validation regex.");
		} else if (!utilisateur.getPassword()
				.matches("^(?=.*[A-Z])(?=.*[\\d])(?=.*[@$!%?&])[A-Za-z\\d@$!%?&]{8,20}$")) {
			// Validation du format du mot de passe
			be.add(BusinessCode.VALIDATION_UTILISATEUR_PASSWORD_FORMAT);
			System.out.println("Erreur regex : " + utilisateur.getPassword());
			isValid = false;
		}

		// Validation de l'email
		if (utilisateur.getEmail() == null || utilisateur.getEmail().isBlank()) {
			be.add(BusinessCode.VALIDATION_UTILISATEUR_EMAIL_BLANK);
			isValid = false;
		} else if (!utilisateur.getEmail().matches("^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
			be.add(BusinessCode.VALIDATION_UTILISATEUR_EMAIL_FORMAT);
			isValid = false;
		}

		// Validation du crédit
		if (utilisateur.getCredit() < 0) {
			be.add(BusinessCode.VALIDATION_UTILISATEUR_CREDIT_NEGATIF);
			isValid = false;
		}

		// Validation de l'adresse
		if (utilisateur.getAdresse() == null) {
			be.add(BusinessCode.VALIDATION_UTILISATEUR_ADRESSE_NULL);
			isValid = false;
		}

		return isValid;
	}
}