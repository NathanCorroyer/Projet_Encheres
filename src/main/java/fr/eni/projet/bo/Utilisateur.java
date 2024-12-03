package fr.eni.projet.bo;

import java.util.Objects;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class Utilisateur {

	private int id;
	@NotBlank
	@NotNull
	@Size(max = 30)
	@Pattern(regexp = "^[a-zA-Z0-9_]+$")
	private String pseudo;

	@NotBlank
	@NotNull
	@Size(max = 30)
	private String nom;

	@NotBlank
	@NotNull
	@Size(max = 30)
	private String prenom;

	@Email
	@NotBlank
	@NotNull
	@Size(max = 50)
	private String email;

	@NotBlank
	@NotNull
	@Size(min = 10, max = 15)
	private String telephone;

	@Valid
	@NotNull
	private Adresse adresse;

	@NotBlank
	@NotNull
	@Size(min = 8, max = 20, message = "{validation.utilisateur.password.format}")
	@Pattern(regexp = "^(?=.[a-z])(?=.[A-Z])(?=.\\d)(?=.[@$!%?&])[A-Za-z\\d@$!%?&]{8,20}$", message = "{validation.utilisateur.password.format}" )
	private String password;

	@NotNull
	@Min(value = 0)
	private int credit = 0;

	private boolean actif = true;

	@NotNull
	private int code_role;

	// ----------------------- CONSTRUCTEURS -----------------------------------
	public Utilisateur() {
	}
	
	public Utilisateur(int id) {
		this.id = id;
	}

	public Utilisateur(int id, String pseudo, String nom, String prenom, String email, String telephone,
			Adresse adresse, String password, int credit, boolean actif, int code_role) {
		super();
		this.id = id;
		this.pseudo = pseudo;
		this.nom = nom;
		this.prenom = prenom;
		this.email = email;
		this.telephone = telephone;
		this.adresse = adresse;
		this.password = password;
		this.credit = credit;
		this.actif = actif;
		this.code_role = code_role;
	}

	// ----------------------- GETTERS / SETTERS -----------------------------------
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPseudo() {
		return pseudo;
	}

	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public Adresse getAdresse() {
		return adresse;
	}

	public void setAdresse(Adresse adresse) {
		this.adresse = adresse;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getCredit() {
		return credit;
	}

	public void setCredit(int credit) {
		this.credit = credit;
	}

	public boolean isActif() {
		return actif;
	}

	public void setActif(boolean actif) {
		this.actif = actif;
	}

	public int getCode_role() {
		return code_role;
	}

	public void setCode_role(int code_role) {
		this.code_role = code_role;
	}

	// ----------------------- METHODES -----------------------------------

	@Override
	public int hashCode() {
		return Objects.hash(actif, adresse, code_role, credit, email, id, nom, password, prenom, pseudo, telephone);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Utilisateur other = (Utilisateur) obj;
		return actif == other.actif && Objects.equals(adresse, other.adresse) && code_role == other.code_role
				&& credit == other.credit && Objects.equals(email, other.email) && id == other.id
				&& Objects.equals(nom, other.nom) && Objects.equals(password, other.password)
				&& Objects.equals(prenom, other.prenom) && Objects.equals(pseudo, other.pseudo)
				&& Objects.equals(telephone, other.telephone);
	}

	@Override
	public String toString() {
		return "Utilisateur [id=" + id + ", pseudo=" + pseudo + ", nom=" + nom + ", prenom=" + prenom + ", email="
				+ email + ", telephone=" + telephone + ", adresse =  " + adresse + " password=" + password + ", credit="
				+ credit + ", actif=" + actif + ", code_role=" + code_role + "]";
	}

		
	

}
