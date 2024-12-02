package fr.eni.projet.DAL.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import fr.eni.projet.DAL.CategorieDAO;
import fr.eni.projet.bo.Categorie;

@Repository
public class CategorieDAOJdbcImpl implements CategorieDAO {

	private final static String FIND_ALL = "SELECT no_categorie, libelle FROM CATEGORIES";
	private final static String FIND_BY_ID = "SELECT no_categorie, libelle FROM CATEGORIES WHERE no_categorie = :id";

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Override
	public void create(Categorie categorie) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Categorie> findAll() {
		// TODO Auto-generated method stub
		return namedParameterJdbcTemplate.query(FIND_ALL, new CategorieRowMapper());
	}

	class CategorieRowMapper implements RowMapper<Categorie> {

		@Override
		public Categorie mapRow(ResultSet rs, int rowNum) throws SQLException {
			Categorie categorie = new Categorie();
			categorie.setId(rs.getInt("no_categorie"));
			categorie.setLibelle(rs.getString("libelle"));
			return categorie;
		}

	}

	@Override
	public Optional<Categorie> findById(int id) {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id", id);
		Categorie categorie = namedParameterJdbcTemplate.queryForObject(FIND_BY_ID, params, new CategorieRowMapper());
		return Optional.ofNullable(categorie);
	}
}
