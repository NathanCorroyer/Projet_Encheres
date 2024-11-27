package fr.eni.projet.bll.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import fr.eni.projet.DAL.ArticleDAO;
import fr.eni.projet.DAL.EnchereDAO;
import fr.eni.projet.DAL.UtilisateurDAO;
import fr.eni.projet.bll.EnchereService;
import fr.eni.projet.bo.Enchere;

@Service
public class EnchereServiceImpl implements EnchereService {
	private EnchereDAO enchereDAO;
	private UtilisateurDAO acheteurDAO;
	private ArticleDAO articleVenduDAO;
	
	public EnchereServiceImpl(EnchereDAO enchereDAO, UtilisateurDAO acheteurDAO, ArticleDAO articleVenduDAO) {
		this.enchereDAO = enchereDAO;
		this.acheteurDAO = acheteurDAO;
		this.articleVenduDAO = articleVenduDAO;
	}

	@Override
	public List<Enchere> getEncheres() {
		return enchereDAO.findAll();
	}

}
