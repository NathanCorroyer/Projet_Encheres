package fr.eni.projet.bo;

public class Categorie {
	
	private int id;
	private String libelle;
	
	
	//----------------------- CONSTRUCTEURS -----------------------------------
	
	public Categorie() {
		super();
	}

	public Categorie(String libelle) {
		this.libelle = libelle;
	}

	//----------------------- GETTERS / SETTERS -----------------------------------
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	//----------------------- Méthodes -----------------------------------
	
	
	@Override
	public String toString() {
		return "Categorie [id=" + id + ", libelle=" + libelle + "]";
	}
	
	
}
