package fr.eni.projet.bo;

public class Adresse {
	
	private int id_article;
	private String rue;
	private String code_postal;
	private String ville;
	
	//----------------------- CONSTRUCTEURS -----------------------------------
	
	public Adresse() {
	}
	public Adresse(int id_article, String rue, String code_postal, String ville) {
		this.id_article = id_article;
		this.rue = rue;
		this.code_postal = code_postal;
		this.ville = ville;
	}
	
	
	//----------------------- GETTERS / SETTERS -----------------------------------
	
	public int getId_article() {
		return id_article;
	}
	public void setId_article(int id_article) {
		this.id_article = id_article;
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

	//----------------------- METHODES -----------------------------------
	
	@Override
	public String toString() {
		return "Adresse [id_article=" + id_article + ", rue=" + rue + ", code_postal=" + code_postal + ", ville=" + ville + "]";
	}
	
	
}
