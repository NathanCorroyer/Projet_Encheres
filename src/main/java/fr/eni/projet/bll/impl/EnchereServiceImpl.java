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
	
	public EnchereServiceImpl(EnchereDAO enchereDAO) {
		this.enchereDAO = enchereDAO;
	}

	@Override
	public List<Enchere> getEncheres() {
		return enchereDAO.findAll();
	}

}
