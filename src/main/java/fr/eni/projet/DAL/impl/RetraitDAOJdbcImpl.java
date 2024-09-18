package fr.eni.projet.DAL.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import fr.eni.projet.DAL.RetraitDAO;
import fr.eni.projet.bo.Retrait;


@Repository
public class RetraitDAOJdbcImpl implements RetraitDAO {
	private final static String INSERT = "";
	private final static String FIND_BY_ID = "";
	
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	
	@Override
	public void create(Retrait retrait) {
		// TODO Auto-generated method stub

	}

	@Override
	public Retrait readByIdArticle(int id_article) {
		// TODO Auto-generated method stub
		return null;
	}

	
	class RetraitRowMapper implements RowMapper<Retrait>{

		@Override
		public Retrait mapRow(ResultSet rs, int rowNum) throws SQLException {
			Retrait retrait = new Retrait();
			retrait.setId_article(rs.getInt("no_article"));
			retrait.setRue(rs.getString("rue"));
			retrait.setCode_postal(rs.getString("code_postal"));
			retrait.setVille(rs.getString("ville"));
			return retrait;
		}
		
	}
}
