package fr.eni.projet.bll.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.eni.projet.DAL.AdresseDAO;
import fr.eni.projet.bll.AdresseService;
import fr.eni.projet.bo.Adresse;

@Service
public class AdresseServiceImpl implements AdresseService {

	@Autowired
	private AdresseDAO adresseDAO;

	@Override
	public void create(Adresse adresse) {
		adresseDAO.create(adresse);
	}

	@Override
	public Adresse readByIdArticle(int id) {
		return adresseDAO.readByIdArticle(id);
	}

	@Override
	public Adresse findById(int id) {
		return adresseDAO.findById(id);
	}

	@Override
	public List<Adresse> findAll() {
		return adresseDAO.findAll();
	}

	@Override
	public void update(Adresse adresse) {
		adresseDAO.update(adresse);
	}

	@Override
	public void delete(int id) {
		adresseDAO.delete(id);
	}
}