package fr.eni.projet.DAL;

import java.util.List;

import fr.eni.projet.bo.Categorie;

public interface CategorieDAO {

	public void create(Categorie categorie);
	
	public List<Categorie> findAll();
	
	
}
