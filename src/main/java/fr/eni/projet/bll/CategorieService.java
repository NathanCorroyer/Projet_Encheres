package fr.eni.projet.bll;

import java.util.List;
import java.util.Optional;

import fr.eni.projet.bo.Categorie;

public interface CategorieService {

	List<Categorie> findAll();

	public Optional<Categorie> findById(int id);

}
