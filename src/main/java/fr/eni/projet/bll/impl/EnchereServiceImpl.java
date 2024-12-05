package fr.eni.projet.bll.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.eni.projet.DAL.ArticleDAO;
import fr.eni.projet.DAL.EnchereDAO;
import fr.eni.projet.DAL.UtilisateurDAO;
import fr.eni.projet.bll.EnchereService;
import fr.eni.projet.bo.Enchere;
import fr.eni.projet.bo.Utilisateur;
import fr.eni.projet.exceptions.BusinessCode;
import fr.eni.projet.exceptions.BusinessException;
import fr.eni.projet.service.SessionService;

@Service
public class EnchereServiceImpl implements EnchereService {
	private EnchereDAO enchereDAO;
	private UtilisateurDAO utilisateurDAO;
	private ArticleDAO articleDAO;
	private SessionService sessionService;

	public EnchereServiceImpl(EnchereDAO enchereDAO, UtilisateurDAO utilisateurDAO, ArticleDAO articleDAO,
			SessionService sessionService) {
		this.enchereDAO = enchereDAO;
		this.utilisateurDAO = utilisateurDAO;
		this.articleDAO = articleDAO;
		this.sessionService = sessionService;

	}

	@Override
	public List<Enchere> getEncheres() {
		return enchereDAO.findAll();
	}

	@Override
	public Optional<Enchere> findBiggestEnchereFromArticle(int id_article) {
		return enchereDAO.findBiggestEnchereFromArticle(id_article);
	}

	@Transactional
	@Override
	public void create(Enchere enchere) {
		BusinessException be = new BusinessException();
//		Utilisateur acheteur = utilisateurDAO.findById(enchere.getAcheteur().getId());
		Utilisateur acheteur = sessionService.getUserSessionAttribute();
		Utilisateur ancienAcheteur = null;
		Optional<Enchere> lastEnchere = enchereDAO.findBiggestEnchereFromArticle(enchere.getArticle().getId());
		int ancienMontant = -1;
		int prixInitial = articleDAO.findArticleById(enchere.getArticle().getId()).getPrix_initial();
		if (!lastEnchere.isEmpty()) {
			ancienMontant = lastEnchere.get().getMontant();
			ancienAcheteur = utilisateurDAO.findById(lastEnchere.get().getAcheteur().getId());
		}

		validerEnchere(enchere, ancienMontant, prixInitial, acheteur, be);
		if (be.isValid()) {
			throw be;
		}

		enchereDAO.create(enchere);
		// Si création fonctionne, retirer les crédits de l'acheteur, rendre les crédits
		// du précédent acheteur s'il existe
		acheteur.setCredit(acheteur.getCredit() - enchere.getMontant());
		utilisateurDAO.updateCredit(acheteur);
		if (ancienAcheteur != null) {
			ancienAcheteur.setCredit(ancienAcheteur.getCredit() + lastEnchere.get().getMontant());
			utilisateurDAO.updateCredit(ancienAcheteur);
		}

	}

	protected boolean validerEnchere(Enchere enchere, int ancienMontant, int prixInitial, Utilisateur acheteur,
			BusinessException be) {
		boolean isValid = true;
		isValid &= validerMontant(enchere.getMontant(), ancienMontant, prixInitial, acheteur, be);
		return isValid;
	}

	protected boolean validerMontant(int nouveauMontant, int ancienMontant, int prixInitial, Utilisateur acheteur,
			BusinessException be) {
		boolean isValid = true;
		if (ancienMontant == -1) {
			if (!(nouveauMontant > prixInitial)) {
				isValid = false;
				be.add(BusinessCode.BLL_ENCHERE_MONTANT_FAIBLE);
			}
		} else {
			if (!(isValid &= nouveauMontant > ancienMontant)) {
				isValid = false;
				be.add(BusinessCode.BLL_ENCHERE_MONTANT_FAIBLE);
			}
		}
		if (nouveauMontant < acheteur.getCredit()) {
			isValid = false;
			be.add(BusinessCode.BLL_ENCHERE_CREDITS_INSUFFISANTS);
		}

		return isValid;
	}
}
