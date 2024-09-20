package fr.eni.projet.bo;

import java.time.LocalDateTime;

public class Enchere {
	
	private LocalDateTime date;
	
	private int montant;
	
	private Article article;
	
	private Utilisateur acheteur;

	
	//----------------------- CONSTRUCTEURS -----------------------------------
	
	public Enchere() {
	}
	
	public Enchere(LocalDateTime date, int montant, Article article, Utilisateur acheteur) {
		this.date = date;
		this.montant = montant;
		this.article = article;
		this.acheteur = acheteur;
	}

	
	//----------------------- GETTERS / SETTERS -----------------------------------

	
	
	public LocalDateTime getDate() {
		return date;
	}
	
	public void setDate(LocalDateTime date) {
		this.date = date;
	}
	
	public int getMontant() {
		return montant;
	}
	
	public void setMontant(int montant) {
		this.montant = montant;
	}
	
	public Article getArticle() {
		return article;
	}
	
	public void setArticle(Article article) {
		this.article = article;
	}
	
	public Utilisateur getAcheteur() {
		return acheteur;
	}
	
	public void setAcheteur(Utilisateur acheteur) {
		this.acheteur = acheteur;
	}

	
	//----------------------- METHODES -----------------------------------
	
	@Override
	public String toString() {
		return "Enchere [date=" + date + ", montant=" + montant + ", article=" + article + ", acheteur=" + acheteur + "]";
	}
	
}
