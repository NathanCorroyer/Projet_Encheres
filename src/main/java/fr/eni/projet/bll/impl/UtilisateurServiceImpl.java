package fr.eni.projet.bll.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.eni.projet.DAL.AdresseDAO;
import fr.eni.projet.DAL.UtilisateurDAO;
import fr.eni.projet.bll.UtilisateurService;
import fr.eni.projet.bo.Adresse;
import fr.eni.projet.bo.Utilisateur;

@Service
public class UtilisateurServiceImpl implements UtilisateurService {

	@Autowired
	UtilisateurDAO utilisateurDAO;

	@Autowired
	private AdresseDAO adresseDAO;

	@Override
	@Transactional
	public void create(Utilisateur utilisateur) {
		Adresse adresse = utilisateur.getAdresse();
		if (adresse == null) {
			throw new IllegalArgumentException("L'adresse est obligatoire pour créer un utilisateur.");
		}
		int adresseKey = 0;
		if (adresse != null) {
			adresseKey = adresseDAO.create(adresse);
		}
		utilisateur.getAdresse().setId(adresseKey);
		utilisateurDAO.create(utilisateur);
	}

	@Override
	public Utilisateur findById(int id) {
		Utilisateur user = utilisateurDAO.findById(id);
		Adresse adresse = adresseDAO.findById(user.getAdresse().getId());
		user.setAdresse(adresse);
		return user;
	}

	@Override
	public Utilisateur findByPseudo(String pseudo) {
		Utilisateur user =  utilisateurDAO.findByPseudo(pseudo);
		Adresse adresse = adresseDAO.findById(user.getAdresse().getId());
		user.setAdresse(adresse);
		return user;
	}

	@Override
	public List<Utilisateur> findAll() {
		return utilisateurDAO.findAll();
	}

	@Override
	public void update(Utilisateur utilisateur) {
		utilisateurDAO.update(utilisateur);
	}

	@Override
	public void modifierActivation(Utilisateur utilisateur) {
		utilisateurDAO.modifierActivation(utilisateur);
	}

}
