package fr.eni.projet.bll.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.eni.projet.DAL.AdresseDAO;
import fr.eni.projet.bll.AdresseService;
import fr.eni.projet.bo.Adresse;
import fr.eni.projet.bo.Utilisateur;
import fr.eni.projet.exceptions.BusinessCode;
import fr.eni.projet.exceptions.BusinessException;

@Service
public class AdresseServiceImpl implements AdresseService {

	@Autowired
	private AdresseDAO adresseDAO;

	@Override
	public void create(Adresse adresse) {
		// Validation avant création
		BusinessException be = new BusinessException();
		if (!validerAdresse(adresse, be)) {
			throw be;
		}
		adresseDAO.create(adresse);
	}

	@Override
	public Adresse findById(int id) {
		if (id <= 0) {
			throw new IllegalArgumentException("L'ID doit être supérieur à 0.");
		}
		return adresseDAO.findById(id);
	}

	@Override
	public List<Adresse> findAll() {
		return adresseDAO.findAll();
	}

	@Override
	public void update(Adresse adresse) {
		// Validation avant mise à jour
		BusinessException be = new BusinessException();
		if (!validerAdresse(adresse, be)) {
			throw be;
		}
		adresseDAO.update(adresse);
	}

	@Override
	public void delete(int id) {
		if (id <= 0) {
			throw new IllegalArgumentException("L'ID doit être supérieur à 0.");
		}
		adresseDAO.delete(id);
	}

	/**
	 * Validation de l'objet Adresse.
	 * 
	 * @param adresse Objet à valider.
	 * @param be      BusinessException pour collecter les erreurs.
	 * @return true si l'objet est valide, false sinon.
	 */
	private boolean validerAdresse(Adresse adresse, BusinessException be) {
		boolean isValid = true;

		if (adresse == null) {
			be.add(BusinessCode.VALIDATION_ADRESSE_NULL);
			return false;
		}

		isValid &= validerRue(adresse.getRue(), be);
		isValid &= validerCodePostal(adresse.getCode_postal(), be);
		isValid &= validerVille(adresse.getVille(), be);

		return isValid;
	}

	private boolean validerRue(String rue, BusinessException be) {
		if (rue == null || rue.isBlank()) {
			be.add(BusinessCode.VALIDATION_ADRESSE_RUE_BLANK);
			return false;
		}
		if (rue.length() > 100) {
			be.add(BusinessCode.VALIDATION_ADRESSE_RUE_LONGUEUR);
			return false;
		}
		return true;
	}

	private boolean validerCodePostal(String codePostal, BusinessException be) {
		if (codePostal == null || codePostal.isBlank()) {
			be.add(BusinessCode.VALIDATION_ADRESSE_CODEPOSTAL_BLANK);
			return false;
		}
		if (!codePostal.matches("\\d{5}")) { // Code postal à 5 chiffres
			be.add(BusinessCode.VALIDATION_ADRESSE_CODEPOSTAL_FORMAT);
			return false;
		}
		return true;
	}

	private boolean validerVille(String ville, BusinessException be) {
		if (ville == null || ville.isBlank()) {
			be.add(BusinessCode.VALIDATION_ADRESSE_VILLE_BLANK);
			return false;
		}
		if (ville.length() > 50) {
			be.add(BusinessCode.VALIDATION_ADRESSE_VILLE_LONGUEUR);
			return false;
		}
		return true;
	}

	@Override
	public List<Adresse> findByUtilisateur(Optional<Utilisateur> utilisateur) {
		if (utilisateur.isPresent()) {
			return adresseDAO.findByUtilisateur(utilisateur.get());
		} else {

			return Collections.emptyList();
		}
	}

	@Override
	public List<Adresse> findByAdresseENI(boolean isENI) {
		return adresseDAO.findByAdresseENI(isENI);
	}

}
