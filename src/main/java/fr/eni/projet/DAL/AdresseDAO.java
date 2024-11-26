package fr.eni.projet.DAL;

import java.util.List;

import fr.eni.projet.bo.Adresse;

public interface AdresseDAO {

	public int create(Adresse adresse);

	public Adresse readByIdArticle(int id);

	public Adresse findById(int id);

	public List<Adresse> findAll();

	public void update(Adresse adresse);

	public void delete(int id);
}
