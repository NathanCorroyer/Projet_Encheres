package fr.eni.projet.bo;

public class Utilisateur {
	private int id;
	private String pseudo;
	private String nom;
	private String prenom;
	private String email;
	private String telephone;
	private Adresse adresse;
	private String password;
	private int credit = 0;
	private boolean actif = true;
	private int code_role;
	
	
	//----------------------- CONSTRUCTEURS ----------------------------------- 
	public Utilisateur() {
	}
	
	
	public Utilisateur(int id, String pseudo, String nom, String prenom, String email, String telephone, Adresse adresse, String password, int credit, boolean actif, int code_role) {
		super();
		this.id = id;
		this.pseudo = pseudo;
		this.nom = nom;
		this.prenom = prenom;
		this.email = email;
		this.telephone = telephone;
		this.adresse=adresse;
		this.password = password;
		this.credit = credit;
		this.actif = actif;
		this.code_role = code_role;
	}
	
	//----------------------- GETTERS / SETTERS -----------------------------------
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



	
	//----------------------- METHODES -----------------------------------


	@Override
	public String toString() {
		return "Utilisateur [id=" + id + ", pseudo=" + pseudo + ", nom=" + nom + ", prenom=" + prenom + ", email=" + email + ", telephone=" + telephone + ", adresse =  " + adresse  + " password=" + password + ", credit=" + credit + ", actif=" + actif + ", code_role=" + code_role + "]";
	}

	
	
}
