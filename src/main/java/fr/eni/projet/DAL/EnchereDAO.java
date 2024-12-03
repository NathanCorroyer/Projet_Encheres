package fr.eni.projet.DAL;

import java.util.List;
import java.util.Optional;

import fr.eni.projet.bo.Enchere;

public interface EnchereDAO {


	public List<Enchere> findAll();
	
	public void create(Enchere enchere);

	public List<Enchere> findAllEncheresFromArticle(int id_article);

	public Optional<Enchere> findBiggestEnchereFromArticle(int id_article);
}
