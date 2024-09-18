package fr.eni.projet.DAL;

import java.util.List;

import fr.eni.projet.bo.Utilisateur;

public interface UtilisateurDAO {
	
	
	public void create(Utilisateur utilisateur);
	public Utilisateur findById(int id);
	public Utilisateur findByPseudo(String pseudo);
	public List<Utilisateur> findAll();
	public void update(Utilisateur utilisateur);
	public void modifierActivation(Utilisateur utilisateur);
}
