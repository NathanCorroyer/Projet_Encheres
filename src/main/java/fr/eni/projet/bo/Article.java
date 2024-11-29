package fr.eni.projet.bo;

import java.time.LocalDateTime;
import java.util.Objects;

import fr.eni.projet.enums.StatutEnchere;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class Article {
	private int id;

	@NotNull
	@NotBlank
	@Size(max = 30)
	private String nom;

	@NotNull
	@NotBlank
	@Size(max = 300)
	private String description;

	@NotNull
	private LocalDateTime date_debut;
	private LocalDateTime date_fin;
	private int prix_initial;
	private int prix_vente;

	@NotNull
	private Utilisateur proprietaire;

	@NotNull
	private Categorie categorie;

	@NotNull
	private Adresse adresse;

	@NotNull
	private StatutEnchere statut_enchere;

	@Size(max = 300)
	private String path_image;
	// ----------------------- CONSTRUCTEURS -----------------------------------

	public Article() {
	}

	public Article(int id, String nom, String description, LocalDateTime date_debut, LocalDateTime date_fin,
			int prix_initial, int prix_vente, Utilisateur proprietaire, Categorie categorie, Adresse adresse,
			StatutEnchere statut_enchere, String path_image) {
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
		this.adresse = adresse;
		this.statut_enchere = statut_enchere;
		this.path_image = path_image;

	}

	// ----------------------- GETTERS / SETTERS -----------------------------------

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

	public Adresse getAdresse() {
		return adresse;
	}

	public void setAdresse(Adresse adresse) {
		this.adresse = adresse;
	}

	public Categorie getCategorie() {
		return categorie;
	}

	public void setCategorie(Categorie categorie) {
		this.categorie = categorie;
	}

	public StatutEnchere getStatut_enchere() {
		return statut_enchere;
	}

	public void setStatut_enchere(StatutEnchere statut_enchere) {
		this.statut_enchere = statut_enchere;
	}

	public String getPath_image() {
		return path_image;
	}

	public void setPath_image(String path_image) {
		this.path_image = path_image;
	}

	// ------------ Méthodes spéciales --------------

	@Override
	public int hashCode() {
		return Objects.hash(adresse, categorie, date_debut, date_fin, description, id, nom, path_image, prix_initial,
				prix_vente, proprietaire, statut_enchere);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Article other = (Article) obj;
		return Objects.equals(adresse, other.adresse) && Objects.equals(categorie, other.categorie)
				&& Objects.equals(date_debut, other.date_debut) && Objects.equals(date_fin, other.date_fin)
				&& Objects.equals(description, other.description) && id == other.id && Objects.equals(nom, other.nom)
				&& Objects.equals(path_image, other.path_image) && prix_initial == other.prix_initial
				&& prix_vente == other.prix_vente && Objects.equals(proprietaire, other.proprietaire)
				&& statut_enchere == other.statut_enchere;
	}

	@Override
	public String toString() {
		return "Article [id=" + id + ", nom=" + nom + ", description=" + description + ", date_debut=" + date_debut
				+ ", date_fin=" + date_fin + ", prix_initial=" + prix_initial + ", prix_vente=" + prix_vente
				+ ", proprietaire=" + proprietaire + ", categorie=" + categorie + "]";
	}
}
