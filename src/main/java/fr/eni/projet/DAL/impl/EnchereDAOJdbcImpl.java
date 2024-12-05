package fr.eni.projet.DAL.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import fr.eni.projet.DAL.EnchereDAO;
import fr.eni.projet.bo.Article;
import fr.eni.projet.bo.Enchere;
import fr.eni.projet.bo.Utilisateur;

@Repository
public class EnchereDAOJdbcImpl implements EnchereDAO {


	private static final String FIND_ALL = "SELECT e.date_enchere, e.montant_enchere, e.no_utilisateur, "
			+ "a.no_article AS no_article, a.nom_article AS nom_article, a.no_utilisateur AS no_vendeur_id, "
			+ "u.pseudo AS nom_vendeur "
			+ "FROM ENCHERES e "
			+ "LEFT JOIN ARTICLES a ON e.no_article = a.no_article "
			+ "LEFT JOIN UTILISATEURS u ON a.no_utilisateur = u.no_utilisateur ";
	private final static String INSERT = "INSERT INTO ENCHERES(date_enchere, montant_enchere, no_utilisateur, no_article) VALUES (:date_enchere, :montant_enchere, :no_utilisateur, :no_article) ";
//	private final static String FIND_ALL_FROM_ARTICLE = "";
	private final static String FIND_BIGGEST_FROM_ARTICLE = "SELECT TOP 1 date_enchere, montant_enchere, no_utilisateur, no_article FROM ENCHERES WHERE no_article = :id_article ORDER BY montant_enchere DESC";
	private final static String DELETE_FROM_USER = "DELETE FROM ENCHERES WHERE no_utilisateur = :no_utilisateur";
		
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	
	public EnchereDAOJdbcImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}
	
	@Override
	public List<Enchere> findAll() {
		return namedParameterJdbcTemplate.query(FIND_ALL, new EnchereRowMapper() );
	}
	
	@Override
	public void create(Enchere enchere) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("date_enchere", enchere.getDate());
		namedParameters.addValue("montant_enchere", enchere.getMontant());
		namedParameters.addValue("no_utilisateur", enchere.getAcheteur().getId());
		namedParameters.addValue("no_article", enchere.getArticle().getId());
		
		namedParameterJdbcTemplate.update(INSERT, namedParameters);
	}

	@Override
	public List<Enchere> findAllEncheresFromArticle(int id_article) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<Enchere> findBiggestEnchereFromArticle(int id_article) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("id_article", id_article); 
		try {
	        Enchere enchere = namedParameterJdbcTemplate.queryForObject(
	            FIND_BIGGEST_FROM_ARTICLE,
	            namedParameters,
	            new EnchereRowMapper()
	        );
	        return Optional.ofNullable(enchere);
	    } catch (EmptyResultDataAccessException e) {
	        return Optional.empty();
	    }
	}

	
	class EnchereRowMapper implements RowMapper<Enchere>{

		@Override
		public Enchere mapRow(ResultSet rs, int rowNum) throws SQLException {
			Enchere enchere = new Enchere();
			enchere.setDate(rs.getObject("date_enchere", LocalDateTime.class));
			enchere.setMontant(rs.getInt("montant_enchere"));
			
			//Relations 
			Utilisateur acheteur = new Utilisateur();
			acheteur.setId(rs.getInt("no_utilisateur"));
			enchere.setAcheteur(acheteur);
			
			
			Article articleVendu = new Article();
			articleVendu.setId(rs.getInt("no_article"));
			enchere.setArticle(articleVendu);
			
			return enchere;
		}
		
	}


	@Override
	public boolean deleteEncheresFromUser(int id) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("no_utilisateur", id);
		return namedParameterJdbcTemplate.update(DELETE_FROM_USER, namedParameters) > 0;
	}
}
