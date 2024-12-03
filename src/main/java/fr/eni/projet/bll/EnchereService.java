package fr.eni.projet.bll;

import java.util.List;
import java.util.Optional;

import fr.eni.projet.bo.Enchere;

public interface EnchereService {
	public List<Enchere> getEncheres();
	public void create(Enchere enchere);
	public Optional<Enchere> findBiggestEnchereFromArticle(int id_article);
}
