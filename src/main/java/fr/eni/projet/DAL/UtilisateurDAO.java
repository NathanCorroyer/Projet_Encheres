package fr.eni.projet.DAL;

import java.util.List;
import java.util.Optional;

import fr.eni.projet.bo.Utilisateur;

public interface UtilisateurDAO {

	public void create(Utilisateur utilisateur);

	public Utilisateur findById(int id);

	public Optional<Utilisateur> findByPseudo(String pseudo);

	public List<Utilisateur> findAll();
	public String findPassword(String pseudo);

	public void update(Utilisateur utilisateur);
	public void updatePassword(Utilisateur utilisateur);

	public void modifierActivation(Utilisateur utilisateur);

	public Optional<Utilisateur> findByEmail(String email);
}
