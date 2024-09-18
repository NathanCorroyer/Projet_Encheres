package fr.eni.projet.bo;

import java.time.LocalDateTime;

public class Enchere {
	private int id;
	
	private LocalDateTime date;
	
	private int montant;
	
	private Article article;
	
	private Utilisateur acheteur;

	
	//----------------------- CONSTRUCTEURS -----------------------------------
	
	public Enchere() {
		super();
	}
	
	public Enchere(int id, LocalDateTime date, int montant, Article article, Utilisateur acheteur) {
		super();
		this.id = id;
		this.date = date;
		this.montant = montant;
		this.article = article;
		this.acheteur = acheteur;
	}

	
	//----------------------- GETTERS / SETTERS -----------------------------------

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
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
		return "Enchere [id=" + id + ", date=" + date + ", montant=" + montant + ", article=" + article + ", acheteur=" + acheteur + "]";
	}
	
}
