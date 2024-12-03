package fr.eni.projet.bll;

import java.util.List;
import java.util.Optional;

import fr.eni.projet.bo.Categorie;

public interface CategorieService {

	public void create(Categorie categorie);
	public Categorie findById(int id);
	public Categorie findByLibelle(String libelle);
	public List<Categorie> findAll();
	public void update(Categorie categorie);

}
