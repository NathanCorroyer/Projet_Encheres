package fr.eni.projet.DAL;

import java.util.List;
import java.util.Optional;

import fr.eni.projet.bo.Categorie;

public interface CategorieDAO {

	public void create(Categorie categorie);

	public Categorie findById(int id);
	public Categorie findByLabel(String libelle);
	public List<Categorie> findAll();

	public void update(Categorie categorie);
	public boolean delete(int id);

}
