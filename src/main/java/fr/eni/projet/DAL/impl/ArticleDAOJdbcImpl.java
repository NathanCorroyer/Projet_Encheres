package fr.eni.projet.DAL.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

	private final static String INSERT = "INSERT INTO ARTICLES(nom_article, description, date_debut_encheres, date_fin_encheres, prix_initial,  no_utilisateur, no_categorie, statut_enchere, no_adresse_retrait) VALUES (:nom_article, :description, :date_debut_encheres, :date_fin_encheres, :prix_initial, :no_utilisateur, :no_categorie, :statut_enchere, :no_adresse_retrait)";
	private final static String FIND_BY_ID = "SELECT no_article, nom_article, description, date_debut_encheres, date_fin_encheres, prix_initial, prix_vente, no_utilisateur, no_categorie, statut_enchere, no_adresse_retrait, path_image FROM ARTICLES WHERE no_article = :no_article";
	private final static String FIND_ALL = "SELECT no_article, nom_article, description, date_debut_encheres, date_fin_encheres, prix_initial, prix_vente, no_utilisateur, no_categorie, statut_enchere, no_adresse_retrait, path_image FROM ARTICLES";
	private final static String FIND_EN_COURS_VENDEUR = "SELECT no_article, nom_article, description, date_debut_encheres, date_fin_encheres, prix_initial, prix_vente, no_utilisateur, no_categorie, statut_enchere, no_adresse_retrait, path_image FROM ARTICLES WHERE no_utilisateur = :no_utilisateur AND statut_enchere=1";
	private final static String FIND_NON_COMMENCEES_VENDEUR = "SELECT no_article, nom_article, description, date_debut_encheres, date_fin_encheres, prix_initial, prix_vente, no_utilisateur, no_categorie, statut_enchere, no_adresse_retrait, path_image FROM ARTICLES WHERE no_utilisateur = :no_utilisateur AND statut_enchere=0";
	private final static String FIND_FINIES_VENDEUR = "SELECT no_article, nom_article, description, date_debut_encheres, date_fin_encheres, prix_initial, prix_vente, no_utilisateur, no_categorie, statut_enchere, no_adresse_retrait, path_image FROM ARTICLES WHERE no_utilisateur = :no_utilisateur AND statut_enchere>1";
	private final static String FIND_ALL_WITH_ENCHERES_FROM_USER = "SELECT DISTINCT a.* FROM ARTICLES a JOIN ENCHERES e ON a.no_article = e.no_article WHERE e.no_utilisateur = :no_utilisateur AND a.statut_enchere = 1";
	private final static String FIND_ALL_FINIES_WITH_ENCHERES_FROM_USER = "SELECT DISTINCT a.* FROM ARTICLES a JOIN ENCHERES e ON a.no_article = e.no_article WHERE e.no_utilisateur = :no_utilisateur AND a.statut_enchere > 1";
	private final static String UPDATE = "UPDATE ARTICLES SET nom_article = :nom_article, description = :description, date_debut_encheres = :date_debut_encheres, date_fin_encheres = :date_fin_encheres, prix_initial = :prix_initial, no_categorie = :no_categorie, no_adresse_retrait = :no_adresse_retrait WHERE no_article = :no_article";
	private final static String DELETE = "DELETE FROM ARTICLES WHERE no_article = :no_article";
	private final static String DELETE_FROM_USER = "DELETE FROM ARTICLES WHERE no_utilisateur = :no_utilisateur";
	private final static String FIND_BY_CATEGORIE = "SELECT no_article, nom_article, description, date_debut_encheres, date_fin_encheres, prix_initial, prix_vente, no_utilisateur, no_categorie, statut_enchere, no_adresse_retrait, path_image FROM ARTICLES WHERE no_categorie = :no_categorie";
	private final static String FIND_BY_NOM = "SELECT no_article, nom_article, description, date_debut_encheres, date_fin_encheres, prix_initial, prix_vente, no_utilisateur, no_categorie, statut_enchere, no_adresse_retrait, path_image FROM ARTICLES WHERE nom_article = :nom";
	private final static String FIND_BY_UTILISATEUR = "SELECT * FROM ARTICLES WHERE no_utilisateur = :no_utilisateur";
	private final static String FIND_BY_ACHETEUR = "SELECT DISTINCT a.* FROM ARTICLES a JOIN ENCHERES e ON a.no_article = e.no_article WHERE e.no_utilisateur = :no_utilisateur";

	private final static String FIND_ALL_ACTIVE = "SELECT a.* FROM ARTICLES a WHERE a.statut_enchere = 1";
	private final static String FIND_ACTIVE_BY_CATEGORIE = "SELECT no_article, nom_article, description, date_debut_encheres, date_fin_encheres, prix_initial, prix_vente, no_utilisateur, no_categorie, statut_enchere, no_adresse_retrait, path_image FROM ARTICLES WHERE no_categorie = :no_categorie AND statut_enchere = 1";
	// private final static String FIND_ALL_ACTIVE ="SELECT a.no_article,
	// a.nom_article, a.description, a.date_debut_encheres, a.date_fin_encheres,
	// a.prix_initial, a.prix_vente, a.no_utilisateur, a.no_categorie,
	// a.statut_enchere, a.no_adresse_retrait, a.path_image, u.pseudo AS
	// pseudo_proprietaire FROM ARTICLES a JOIN UTILISATEURS u ON a.no_utilisateur =
	// u.no_utilisateur" + " WHERE a.statut_enchere = 1";
	private static final String FIND_BY_DATE_AND_STATUT = "SELECT * FROM ARTICLES WHERE CAST(date_debut_encheres AS DATE) = :date_debut_encheres "
			+ "AND statut_enchere = :statut_enchere";
	private static final String UPDATE_STATUT = "UPDATE ARTICLES SET statut_enchere = :statut_enchere WHERE no_article = :no_article";
	private final static String UPLOAD_IMAGE = "UPDATE ARTICLES SET path_image = :path_image WHERE no_article = :no_article";
	private static final String FIND_BY_DATE_FIN_AND_STATUT = "SELECT * FROM ARTICLES WHERE CAST(date_fin_encheres AS DATE) < :date_fin_encheres "
			+ "AND statut_enchere = :statut_enchere";
	private final static String HAS_ENCHERES = "SELECT COUNT(*) FROM ENCHERES WHERE no_article = :no_article";
	// private final static String FIND_ALL_ACTIVE ="SELECT a.no_article,
	// a.nom_article, a.description, a.date_debut_encheres, a.date_fin_encheres,
	// a.prix_initial, a.prix_vente, a.no_utilisateur, a.no_categorie,
	// a.statut_enchere, a.no_adresse_retrait, a.path_image, u.pseudo AS
	// pseudo_proprietaire FROM ARTICLES a JOIN UTILISATEURS u ON a.no_utilisateur =
	// u.no_utilisateur" + " WHERE a.statut_enchere = 1";

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
	public List<Article> findAllActive() {
		return namedParameterJdbcTemplate.query(FIND_ALL_ACTIVE, new ArticleRowMapper());
	}
	
	@Override
	public List<Article> findAllActiveWithEncheres(int userId) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("no_utilisateur", userId);
		return namedParameterJdbcTemplate.query(FIND_ALL_WITH_ENCHERES_FROM_USER,namedParameters,  new ArticleRowMapper());
	}

	// Filter on the Server
//	@Override
//	public List<Article> findAllActive(String nom, Integer categorieId, String sortBy, String sortOrder) {
//		StringBuilder sql = new StringBuilder(FIND_ALL_ACTIVE);
//		sql.append(" JOIN categorie c ON a.no_catgeorie = c.no_categorie");
//		
//		// Array list to get all the request's parameters
//		Map<String, Object> params = new HashMap<>();
//		
//		// Conditional filters
//		boolean firstCondition = true;
//		
//		// Filter by name
//		if (nom != null && !nom.isEmpty()) {
//			if (firstCondition) {
//				sql.append(" WHERE ");
//				firstCondition = false;
//			} else {
//				sql.append(" AND ");
//			}
//			sql.append("e.nom LIKE :nom");
//			params.put("nom", "%" + nom + "%");
//		}
//		
//		// Filter by categorie
//		if (categorieId != null) {
//			if (firstCondition) {
//				sql.append(" WHERE ");
//				firstCondition = false;
//			} else {
//				sql.append(" AND ");
//			}
//			sql.append("e.catgeorie_id = :categorieId");
//			params.put("categorieId", categorieId);
//		}
//		
//		//Dynamic filters
//		if (sortBy!= null && sortBy.isEmpty()) {
//			sql.append(" ORDER BY e.").append(sortBy).append(" ").append(sortOrder != null ? sortOrder : "ASC");
//		}
//		
//		return namedParameterJdbcTemplate.query(sql.toString(), params, new ArticleRowMapper());
//	}

	@Override
	public boolean hasEncheres(int articleId) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("no_article", articleId);

		Integer count = namedParameterJdbcTemplate.queryForObject(HAS_ENCHERES, namedParameters, Integer.class);
		return count != null && count > 0;
	}

	@Override
	public int create(Article article) {
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
		// Récupérer l'ID généré pour l'adresse
		if (keyHolder.getKey() != null) {
			article.setId(keyHolder.getKey().intValue());
			return keyHolder.getKey().intValue();
		}
		return 0;
	}

	@Override
	public void updateStatutEnchere(Article article, StatutEnchere statutEnchere) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("statut_enchere", statutEnchere.ordinal());
		namedParameters.addValue("no_article", article.getId());
		namedParameterJdbcTemplate.update(UPDATE_STATUT, namedParameters);
	}

	@Override
	public void update(Article article) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();

		namedParameters.addValue("nom_article", article.getNom()).addValue("description", article.getDescription())
				.addValue("date_debut_encheres", article.getDate_debut())
				.addValue("date_fin_encheres", article.getDate_fin())
				.addValue("prix_initial", article.getPrix_initial())
//				.addValue("no_utilisateur", article.getProprietaire().getId())
				.addValue("no_categorie", article.getCategorie().getId())
//				.addValue("statut_enchere", article.getStatut_enchere().ordinal())
				.addValue("no_adresse_retrait", article.getAdresse().getId()).addValue("no_article", article.getId());

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
	public List<Article> findAllByCategorie(int categorieId) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("no_categorie", categorieId);
		return namedParameterJdbcTemplate.query(FIND_ACTIVE_BY_CATEGORIE, namedParameters, new ArticleRowMapper());
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
	
	@Override
	public List<Article> findByEncherisseur(int utilisateurId){
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("no_utilisateur", utilisateurId);
		return namedParameterJdbcTemplate.query(FIND_BY_ACHETEUR, namedParameters, new ArticleRowMapper());
	}


	@Override
	public List<Article> findByDateDebutAndStatutEnchere(LocalDateTime today, int statut) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		// Ne comparer que la date, pas l'heure
		LocalDate date = today.toLocalDate();
		namedParameters.addValue("date_debut_encheres", date);
		namedParameters.addValue("statut_enchere", statut);

		return namedParameterJdbcTemplate.query(FIND_BY_DATE_AND_STATUT, namedParameters, (rs, rowNum) -> {
			Article article = new Article();
			article.setId(rs.getInt("no_article"));
			article.setDate_debut(rs.getTimestamp("date_debut_encheres").toLocalDateTime());
			article.setStatut_enchere(StatutEnchere.fromValue(rs.getInt("statut_enchere")));
			return article;
		});
	}

	@Override
	public List<Article> findByDateFinBeforeAndStatutEnchere(LocalDateTime today, int statut) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		// Ne comparer que la date, pas l'heure
		LocalDate date = today.toLocalDate();
		namedParameters.addValue("date_fin_encheres", date);
		namedParameters.addValue("statut_enchere", statut);

		return namedParameterJdbcTemplate.query(FIND_BY_DATE_FIN_AND_STATUT, namedParameters, (rs, rowNum) -> {
			Article article = new Article();
			article.setId(rs.getInt("no_article"));
			article.setDate_debut(rs.getTimestamp("date_fin_encheres").toLocalDateTime());
			article.setStatut_enchere(StatutEnchere.fromValue(rs.getInt("statut_enchere")));
			return article;
		});
	}

	@Override
	public void uploadImage(String fileName, int idArticle) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("no_article", idArticle);
		namedParameters.addValue("path_image", fileName);
		namedParameterJdbcTemplate.update(UPLOAD_IMAGE, namedParameters);
	}

	@Override
	public List<Article> findAllFiniesWithEncheres(int userId) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("no_utilisateur", userId);
		return namedParameterJdbcTemplate.query(FIND_ALL_FINIES_WITH_ENCHERES_FROM_USER, namedParameters, new ArticleRowMapper());
	}


	@Override
	public List<Article> findEnCoursFromVendeur(int id) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("no_utilisateur", id);
		return namedParameterJdbcTemplate.query(FIND_EN_COURS_VENDEUR, namedParameters, new ArticleRowMapper());
	}


	@Override
	public List<Article> findNonCommenceeFromVendeur(int id) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("no_utilisateur", id);
		return namedParameterJdbcTemplate.query(FIND_NON_COMMENCEES_VENDEUR, namedParameters, new ArticleRowMapper());
	}


	@Override
	public List<Article> findFiniesFromVendeur(int id) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("no_utilisateur", id);
		return namedParameterJdbcTemplate.query(FIND_FINIES_VENDEUR, namedParameters, new ArticleRowMapper());
	
	@Override
	public void deleteFromUser(int userId) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("no_utilisateur", userId);
		namedParameterJdbcTemplate.update(DELETE_FROM_USER, namedParameters);
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
			Categorie categorie = new Categorie();
			categorie.setId(rs.getInt("no_categorie"));
			article.setCategorie(categorie);
			article.setStatut_enchere(StatutEnchere.fromValue(rs.getInt("statut_enchere")));
			article.setPath_image(rs.getString("path_image"));

			// Relations
			Utilisateur proprietaire = new Utilisateur();
			proprietaire.setId(rs.getInt("no_utilisateur"));
			article.setProprietaire(proprietaire);

			Adresse adresse = new Adresse();
			adresse.setId(rs.getInt("no_adresse_retrait"));
			article.setAdresse(adresse);

			return article;
		}
	}

}