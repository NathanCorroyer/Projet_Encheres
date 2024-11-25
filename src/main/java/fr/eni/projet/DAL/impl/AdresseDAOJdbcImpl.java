package fr.eni.projet.DAL.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import fr.eni.projet.DAL.AdresseDAO;
import fr.eni.projet.bo.Adresse;


@Repository
public class AdresseDAOJdbcImpl implements AdresseDAO {
	private final static String INSERT = "";
	private final static String FIND_BY_ID = "";
	
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	
	@Override
	public void create(Adresse adresse) {
		// TODO Auto-generated method stub

	}

	@Override
	public Adresse readByIdArticle(int id_article) {
		// TODO Auto-generated method stub
		return null;
	}

	
	class AdresseRowMapper implements RowMapper<Adresse>{

		@Override
		public Adresse mapRow(ResultSet rs, int rowNum) throws SQLException {
			Adresse adresse = new Adresse();
			adresse.setId_article(rs.getInt("no_article"));
			adresse.setRue(rs.getString("rue"));
			adresse.setCode_postal(rs.getString("code_postal"));
			adresse.setVille(rs.getString("ville"));
			return adresse;
		}
		
	}
}
