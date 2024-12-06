package fr.eni.projet.bll;

import java.util.List;
import java.util.Optional;

import fr.eni.projet.bo.Adresse;
import fr.eni.projet.bo.Utilisateur;

public interface AdresseService {
	public void create(Adresse adresse);

	public Adresse findById(int id);

	public List<Adresse> findAll();

	public void update(Adresse adresse);

	public void delete(int id);

	public List<Adresse> findByUtilisateur(Optional<Utilisateur> utilisateur);

	public List<Adresse> findByAdresseENI(boolean isENI);

}
