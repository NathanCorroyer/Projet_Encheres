package fr.eni.projet.DAL.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import fr.eni.projet.DAL.ArticleDAO;
import fr.eni.projet.bo.Adresse;
import fr.eni.projet.bo.Article;
import fr.eni.projet.bo.Categorie;
import fr.eni.projet.bo.Utilisateur;
import fr.eni.projet.enums.StatutEnchere;

@Repository
public class ArticleDAOJdbcImpl implements ArticleDAO {

	private final static String INSERT = "INSERT INTO ARTICLES(nom_article, description, date_debut_encheres, date_fin_encheres, prix_initial, prix_vente, no_utilisateur, no_categorie, statut_enchere, no_adresse_retrait, path_image) VALUES (:nom_article, :description, :date_debut_encheres, :date_fin_encheres, :prix_initial, :prix_vente, :no_utilisateur, :no_categorie, :statut_enchere, :no_adresse_retrait, :path_image)";
	private final static String FIND_BY_ID = "SELECT no_article, nom_article, description, date_debut_encheres, date_fin_encheres, prix_initial, prix_vente, no_utilisateur, no_categorie, statut_enchere, no_adresse_retrait, path_image FROM ARTICLES WHERE no_article = :no_article";
	private final static String FIND_ALL = "SELECT no_article, nom_article, description, date_debut_encheres, date_fin_encheres, prix_initial, prix_vente, no_utilisateur, no_categorie, statut_enchere, no_adresse_retrait, path_image FROM ARTICLES";
	private final static String UPDATE = "UPDATE ARTICLES SET nom_article = :nom_article, description = :description, date_debut_encheres = :date_debut_encheres, date_fin_encheres = :date_fin_encheres, prix_initial = :prix_initial, prix_vente = :prix_vente, no_utilisateur = :no_utilisateur, no_categorie = :no_categorie, statut_enchere = :statut_enchere, no_adresse_retrait = :no_adresse_retrait, path_image = :path_image WHERE no_article = :no_article";
	private final static String DELETE = "DELETE FROM ARTICLES WHERE no_article = :no_article";
	private final static String FIND_BY_CATEGORIE = "SELECT no_article, nom_article, description, date_debut_encheres, date_fin_encheres, prix_initial, prix_vente, no_utilisateur, no_categorie, statut_enchere, no_adresse_retrait, path_image FROM ARTICLES WHERE no_categorie = :no_categorie";
	private final static String FIND_BY_NOM = "SELECT no_article, nom_article, description, date_debut_encheres, date_fin_encheres, prix_initial, prix_vente, no_utilisateur, no_categorie, statut_enchere, no_adresse_retrait, path_image FROM ARTICLES WHERE nom_article = :nom";
	private final static String FIND_BY_UTILISATEUR = "SELECT no_article, nom_article, description, date_debut_encheres, date_fin_encheres, prix_initial, prix_vente, no_utilisateur, no_categorie, statut_enchere, no_adresse_retrait, path_image FROM ARTICLES WHERE no_utilisateur = :no_utilisateur";
	private final static String FIND_ALL_ACTIVE = "SELECT no_article, nom_article, description, date_debut_encheres, date_fin_encheres, prix_initial, prix_vente, no_utilisateur, no_categorie, statut_enchere, no_adresse_retrait, path_image FROM ARTICLES WHERE statut_enchere = 1";
	//	private final static String FIND_ALL_ACTIVE ="SELECT a.no_article, a.nom_article, a.description, a.date_debut_encheres, a.date_fin_encheres, a.prix_initial, a.prix_vente, a.no_utilisateur, a.no_categorie, a.statut_enchere, a.no_adresse_retrait, a.path_image, u.pseudo AS pseudo_proprietaire FROM ARTICLES a JOIN UTILISATEURS u ON a.no_utilisateur = u.no_utilisateur" + " WHERE a.statut_enchere = 1";
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Override
	public Article findArticleById(int id) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("no_article", id);
		return namedParameterJdbcTemplate.queryForObject(FIND_BY_ID, namedParameters, new ArticleRowMapper());
	}

	@Override
	public List<Article> findAll() {
		return namedParameterJdbcTemplate.query(FIND_ALL, new ArticleRowMapper());
	}
	
	@Override
	public List<Article> findAllActive(int categorieId, String nom) {
		
		MapSqlParameterSource namedParameterMap = new MapSqlParameterSource();
		
		StringBuilder sql = new StringBuilder(FIND_ALL_ACTIVE);
		
		if(categorieId >= 0) {
			sql.append(" AND no_categorie = :no_categorie");
			namedParameterMap.addValue("no_catgeorie", categorieId);
		}
		
		if(nom != null && !nom.isEmpty()) {
			sql.append(" AND nom = :nom");
			namedParameterMap.addValue("nom", nom);
		}
				
		return namedParameterJdbcTemplate.query(sql.toString(), namedParameterMap, new ArticleRowMapper());
	}

	@Override
	public void create(Article article) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		KeyHolder keyHolder = new GeneratedKeyHolder();

		namedParameters.addValue("nom_article", article.getNom()).addValue("description", article.getDescription())
				.addValue("date_debut_encheres", article.getDate_debut())
				.addValue("date_fin_encheres", article.getDate_fin())
				.addValue("prix_initial", article.getPrix_initial()).addValue("prix_vente", article.getPrix_vente())
				.addValue("no_utilisateur", article.getProprietaire().getId())
				.addValue("no_categorie", article.getCategorie().getId())
				.addValue("statut_enchere", article.getStatut_enchere().ordinal())
				.addValue("no_adresse_retrait", article.getAdresse().getId())
				.addValue("path_image", article.getPath_image());

		namedParameterJdbcTemplate.update(INSERT, namedParameters, keyHolder);
		if (keyHolder != null && keyHolder.getKey() != null) {
			article.setId(keyHolder.getKey().intValue());
		}
	}

	@Override
	public void update(Article article) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();

		namedParameters.addValue("nom_article", article.getNom()).addValue("description", article.getDescription())
				.addValue("date_debut_encheres", article.getDate_debut())
				.addValue("date_fin_encheres", article.getDate_fin())
				.addValue("prix_initial", article.getPrix_initial()).addValue("prix_vente", article.getPrix_vente())
				.addValue("no_utilisateur", article.getProprietaire().getId())
				.addValue("no_categorie", article.getCategorie().getId())
				.addValue("statut_enchere", article.getStatut_enchere().ordinal())
				.addValue("no_adresse_retrait", article.getAdresse().getId())
				.addValue("path_image", article.getPath_image()).addValue("no_article", article.getId());

		namedParameterJdbcTemplate.update(UPDATE, namedParameters);
	}

	@Override
	public void delete(int id) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("no_article", id);
		namedParameterJdbcTemplate.update(DELETE, namedParameters);
	}

	@Override
	public List<Article> findByCategorie(int categorieId) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("no_categorie", categorieId);
		return namedParameterJdbcTemplate.query(FIND_BY_CATEGORIE, namedParameters, new ArticleRowMapper());
	}
	
	@Override
	public List<Article> findByNom(String nom) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("nom", nom);
		return namedParameterJdbcTemplate.query(FIND_BY_NOM, namedParameters, new ArticleRowMapper());
	}

	@Override
	public List<Article> findByUtilisateur(int utilisateurId) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("no_utilisateur", utilisateurId);
		return namedParameterJdbcTemplate.query(FIND_BY_UTILISATEUR, namedParameters, new ArticleRowMapper());
	}
	
	// RowMapper to map SQL result to Article object
	class ArticleRowMapper implements RowMapper<Article> {
		
		@Override
		public Article mapRow(ResultSet rs, int rowNum) throws SQLException {
			Article article = new Article();
			
			article.setId(rs.getInt("no_article"));
			article.setNom(rs.getString("nom_article"));
			article.setDescription(rs.getString("description"));
			article.setDate_debut(rs.getTimestamp("date_debut_encheres").toLocalDateTime());
			article.setDate_fin(rs.getTimestamp("date_fin_encheres").toLocalDateTime());
			article.setPrix_initial(rs.getInt("prix_initial"));
			article.setPrix_vente(rs.getInt("prix_vente"));
			article.setStatut_enchere(StatutEnchere.fromValue(rs.getInt("statut_enchere")));
			article.setPath_image(rs.getString("path_image"));
			
			//Relations
			Utilisateur proprietaire = new Utilisateur();
			proprietaire.setId(rs.getInt("no_utilisateur"));
			article.setProprietaire(proprietaire);
			
			Categorie categorie = new Categorie();
			categorie.setId(rs.getInt("no_categorie"));
			article.setCategorie(categorie);
			
			Adresse adresse = new Adresse();
			adresse.setId(rs.getInt("no_adresse_retrait"));
			article.setAdresse(adresse);
			
			return article;
		}
	}
}