package fr.eni.projet.bll.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.eni.projet.DAL.ArticleDAO;
import fr.eni.projet.DAL.CategorieDAO;
import fr.eni.projet.bll.CategorieService;
import fr.eni.projet.bo.Article;
import fr.eni.projet.bo.Categorie;

@Service
public class CategorieServiceImpl implements CategorieService {

	@Autowired
	CategorieDAO categorieDAO;
	@Autowired
	ArticleDAO articleDAO;

	@Override
	public List<Categorie> findAll() {
		return categorieDAO.findAll();
	}

	@Override
	public Categorie findById(int id) {
		return categorieDAO.findById(id);
	}

	@Override
	public void create(Categorie categorie) {
		categorieDAO.create(categorie);
		
	}

	@Override
	public Categorie findByLibelle(String libelle) {
		return categorieDAO.findByLabel(libelle);
	}

	@Override
	public void update(Categorie categorie) {
		categorieDAO.update(categorie);
	}

	@Override
	public boolean delete(int id) {
		List<Article> articles = articleDAO.findByCategorie(id);
		if(articles.isEmpty()) {			
			return categorieDAO.delete(id);
		}
		return false;
	}

		
}
