package fr.eni.projet.DAL;

import fr.eni.projet.bo.Adresse;

public interface AdresseDAO {
	
	public void create(Adresse adresse);
	public Adresse readByIdArticle(int id_article);
}
