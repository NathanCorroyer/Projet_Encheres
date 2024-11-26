package fr.eni.projet.bo;

public class Adresse {

	private int id;
	private String rue;
	private String code_postal;
	private String ville;
	private boolean adresse_eni;

	// ----------------------- CONSTRUCTEURS -----------------------------------

	public Adresse() {
	}

	
	
	public Adresse(int id) {
		this.id = id;
	}



	public Adresse(int id, String rue, String code_postal, String ville, boolean adresse_eni) {
		this.id = id;
		this.rue = rue;
		this.code_postal = code_postal;
		this.ville = ville;
		this.adresse_eni = adresse_eni;
	}

	// ----------------------- GETTERS / SETTERS -----------------------------------

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRue() {
		return rue;
	}

	public void setRue(String rue) {
		this.rue = rue;
	}

	public String getCode_postal() {
		return code_postal;
	}

	public void setCode_postal(String code_postal) {
		this.code_postal = code_postal;
	}

	public String getVille() {
		return ville;
	}

	public void setVille(String ville) {
		this.ville = ville;
	}

	
	public boolean isAdresse_eni() {
		return adresse_eni;
	}
	
	public void setAdresse_eni(boolean adresse_eni) {
		this.adresse_eni = adresse_eni;
	}
	
	
	// ----------------------- METHODES -----------------------------------


	@Override
	public String toString() {
		return "Adresse [id_article=" + id + ", rue=" + rue + ", code_postal=" + code_postal + ", ville=" + ville + "]";
	}

}
