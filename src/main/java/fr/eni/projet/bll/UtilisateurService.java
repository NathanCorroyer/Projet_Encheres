package fr.eni.projet.bll;

import java.util.List;
import java.util.Optional;

import fr.eni.projet.bo.Utilisateur;

public interface UtilisateurService {
	public void create(Utilisateur utilisateur);

	public Utilisateur findById(int id);

	public Optional<Utilisateur> findByPseudo(String pseudo);

	public List<Utilisateur> findAll();
	public String findPassword(String pseudo);
	public void update(Utilisateur utilisateur, boolean isEmail);
	public void updatePassword(Utilisateur utilisateur, String currentPassword, String newPassword, String confirmPassword);

	public void modifierActivation(Utilisateur utilisateur);

	public Optional<Utilisateur> findByEmail(String email);
}
