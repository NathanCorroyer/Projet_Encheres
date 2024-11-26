package fr.eni.projet.bo;

import java.util.Objects;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class Adresse {

	private int id;

	@NotNull
	@NotBlank
	@Size(max = 100)
	private String rue;

	@NotNull
	@NotBlank
	@Size(min = 5, max = 5)
	private String code_postal;

	@NotNull
	@NotBlank
	@Size(max = 50)
	private String ville;

	@NotNull
	private Boolean adresse_eni;

	// ----------------------- CONSTRUCTEURS -----------------------------------

	public Adresse() {
	}

	public Adresse(int id, String rue, String code_postal, String ville, boolean adresse_eni) {
		this.id = id;
		this.rue = rue;
		this.code_postal = code_postal;
		this.ville = ville;
		this.adresse_eni = adresse_eni;
	}

	// ----------------------- GETTERS / SETTERS -----------------------------------

	public int getId_article() {
		return id;
	}

	public void setId_article(int id_article) {
		this.id = id_article;
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

	public Boolean getAdresseEni() {
		return adresse_eni;
	}

	public void setAdresseEni(Boolean adresseEni) {
		this.adresse_eni = adresseEni;
	}

	// ----------------------- METHODES -----------------------------------

	@Override
	public int hashCode() {
		return Objects.hash(code_postal, id, rue, ville, adresse_eni);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Adresse other = (Adresse) obj;
		return Objects.equals(code_postal, other.code_postal) && id == other.id && Objects.equals(rue, other.rue)
				&& Objects.equals(ville, other.ville) && adresse_eni == other.adresse_eni;
	}

	@Override
	public String toString() {
		return "Adresse [id_article=" + id + ", rue=" + rue + ", code_postal=" + code_postal + ", ville=" + ville
				+ ", adresse_eni=" + adresse_eni + "]";
	}

}
