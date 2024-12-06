package fr.eni.projet.DAL;

import java.util.List;

import fr.eni.projet.bo.Adresse;
import fr.eni.projet.bo.Utilisateur;

public interface AdresseDAO {

	public int create(Adresse adresse);

	public Adresse findById(int id);

	public List<Adresse> findAll();

	public void update(Adresse adresse);

	public void delete(int id);

	List<Adresse> findByUtilisateur(Utilisateur utilisateur);

	List<Adresse> findByAdresseENI(boolean isENI);
}
