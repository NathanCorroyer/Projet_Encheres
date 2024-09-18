package fr.eni.projet.DAL.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import fr.eni.projet.DAL.EnchereDAO;
import fr.eni.projet.bo.Enchere;

@Repository
public class EnchereDAOJdbcImpl implements EnchereDAO {
	private final static String INSERT = "";
	private final static String FIND_ALL_FROM_ARTICLE = "";
	private final static String FIND_BIGGEST_FROM_ARTICLE = "";
	
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Override
	public void create(Enchere enchere) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Enchere> findAllEncheresFromArticle(int id_article) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Enchere findBiggestEnchereFromArticle(int id_article) {
		// TODO Auto-generated method stub
		return null;
	}

	
	class EnchereRowMapper implements RowMapper<Enchere>{

		@Override
		public Enchere mapRow(ResultSet rs, int rowNum) throws SQLException {
			Enchere enchere = new Enchere();
			enchere.setId(rs.getInt("no_enchere"));
			enchere.setDate(rs.getObject("date_enchere", LocalDateTime.class));
			enchere.setMontant(rs.getInt("montant_enchere"));
			
			//Relations 
			enchere.getAcheteur().setId(rs.getInt("no_utilisateur"));
			enchere.getArticle().setId(rs.getInt("no_article"));
			return enchere;
		}
		
	}
}
