package fr.eni.projet.bll.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.eni.projet.DAL.CategorieDAO;
import fr.eni.projet.bll.CategorieService;
import fr.eni.projet.bo.Categorie;

@Service
public class CatégorieServiceImpl implements CategorieService {

	@Autowired
	CategorieDAO categorieDAO;

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
		// TODO Auto-generated method stub
		
	}

	@Override
	public Categorie findByLibelle(String libelle) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(Categorie categorie) {
		// TODO Auto-generated method stub
		
	}

		
}
