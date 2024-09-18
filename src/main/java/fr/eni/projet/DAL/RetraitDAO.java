package fr.eni.projet.DAL;

import fr.eni.projet.bo.Retrait;

public interface RetraitDAO {
	
	public void create(Retrait retrait);
	public Retrait readByIdArticle(int id_article);
}
