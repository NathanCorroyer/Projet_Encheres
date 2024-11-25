package fr.eni.projet.bll;

import java.util.List;

import fr.eni.projet.bo.Adresse;

public interface AdresseService {
	public void create(Adresse adresse);

	public Adresse readByIdArticle(int id);

	public Adresse findById(int id);

	public List<Adresse> findAll();

	public void update(Adresse adresse);

	public void delete(int id);
}
