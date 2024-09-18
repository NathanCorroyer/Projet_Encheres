package fr.eni.projet.bll;

import java.util.List;

import fr.eni.projet.bo.Utilisateur;

public interface UtilisateurService {
	public void create(Utilisateur utilisateur);
	public Utilisateur findById(int id);
	public Utilisateur findByPseudo(String pseudo);
	public List<Utilisateur> findAll();
	public void update(Utilisateur utilisateur);
	public void modifierActivation(Utilisateur utilisateur);
}
