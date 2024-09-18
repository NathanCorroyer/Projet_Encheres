package fr.eni.projet.bo;

import java.time.LocalDateTime;

public class Article {
	private int id;
	private String nom;
	private String description;
	private LocalDateTime date_debut;
	private LocalDateTime date_fin;
	private int prix_initial;
	private int prix_vente;
	private Utilisateur proprietaire;
	private Categorie categorie;
	
	

	//----------------------- CONSTRUCTEURS -----------------------------------
	
	public Article() {
	}
	
	public Article(int id, String nom, String description, LocalDateTime date_debut, LocalDateTime date_fin, int prix_initial, int prix_vente, Utilisateur proprietaire, Categorie categorie) {
		super();
		this.id = id;
		this.nom = nom;
		this.description = description;
		this.date_debut = date_debut;
		this.date_fin = date_fin;
		this.prix_initial = prix_initial;
		this.prix_vente = prix_vente;
		this.proprietaire = proprietaire;
		this.categorie = categorie;
	}
	
	//----------------------- GETTERS / SETTERS -----------------------------------
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public LocalDateTime getDate_debut() {
		return date_debut;
	}
	public void setDate_debut(LocalDateTime date_debut) {
		this.date_debut = date_debut;
	}
	public LocalDateTime getDate_fin() {
		return date_fin;
	}
	public void setDate_fin(LocalDateTime date_fin) {
		this.date_fin = date_fin;
	}
	public int getPrix_initial() {
		return prix_initial;
	}
	public void setPrix_initial(int prix_initial) {
		this.prix_initial = prix_initial;
	}
	public int getPrix_vente() {
		return prix_vente;
	}
	public void setPrix_vente(int prix_vente) {
		this.prix_vente = prix_vente;
	}
	public Utilisateur getProprietaire() {
		return proprietaire;
	}
	public void setProprietaire(Utilisateur proprietaire) {
		this.proprietaire = proprietaire;
	}
	public Categorie getCategorie() {
		return categorie;
	}
	public void setCategorie(Categorie categorie) {
		this.categorie = categorie;
	}

	
	
	//------------ Méthodes spéciales --------------
	

	@Override
	public String toString() {
		return "Article [id=" + id + ", nom=" + nom + ", description=" + description + ", date_debut=" + date_debut + ", date_fin=" + date_fin + ", prix_initial=" + prix_initial + ", prix_vente="
				+ prix_vente + ", proprietaire=" + proprietaire + ", categorie=" + categorie + "]";
	}
}
