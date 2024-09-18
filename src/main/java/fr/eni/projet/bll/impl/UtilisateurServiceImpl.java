package fr.eni.projet.bll.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.eni.projet.DAL.UtilisateurDAO;
import fr.eni.projet.bll.UtilisateurService;
import fr.eni.projet.bo.Utilisateur;

@Service
public class UtilisateurServiceImpl implements UtilisateurService {

	@Autowired
	UtilisateurDAO utilisateurDAO;
	
	@Override
	public void create(Utilisateur utilisateur) {
		utilisateurDAO.create(utilisateur);
	}

	@Override
	public Utilisateur findById(int id) {
		return utilisateurDAO.findById(id);
	}

	@Override
	public Utilisateur findByPseudo(String pseudo) {
		return utilisateurDAO.findByPseudo(pseudo);
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
