package fr.eni.projet.DAL;

import java.util.List;
import java.util.Optional;

import fr.eni.projet.bo.Categorie;

public interface CategorieDAO {

	public void create(Categorie categorie);

	public List<Categorie> findAll();

	public Optional<Categorie> findById(int id);

}
