package fr.eni.projet.DAL;

import java.util.List;

import fr.eni.projet.bo.Enchere;

public interface EnchereDAO {
	
	public void create(Enchere enchere);
	
	public List<Enchere> findAllEncheresFromArticle(int id_article);
	
	public Enchere findBiggestEnchereFromArticle(int id_article);
}
