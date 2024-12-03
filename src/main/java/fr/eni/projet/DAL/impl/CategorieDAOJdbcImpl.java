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

import fr.eni.projet.DAL.CategorieDAO;
import fr.eni.projet.bo.Categorie;

@Repository
public class CategorieDAOJdbcImpl implements CategorieDAO {

	private final static String INSERT = "INSERT INTO CATEGORIES (libelle) VALUES (:libelle)";
	private final static String FIND_BY_ID = "SELECT no_categorie, libelle FROM CATEGORIES WHERE no_categorie = :no_categorie";
	private final static String FIND_BY_LIBELLE = "SELECT no_categorie, libelle FROM CATEGORIES WHERE libelle = :libelle";
	private final static String FIND_ALL = "SELECT no_categorie, libelle FROM CATEGORIES";
	private final static String UPDATE = "UPDATE CATEGORIES SET libelle = :libelle WHERE no_categorie = :no_categorie";
	private final static String DELETE = "DELETE FROM CATEGORIES WHERE no_categorie = :noCategorie";

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Override
	public void create(Categorie categorie) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		KeyHolder keyHolder = new GeneratedKeyHolder();

		namedParameters
		.addValue("libelle", categorie.getLibelle());

		namedParameterJdbcTemplate.update(INSERT, namedParameters, keyHolder);
		if (keyHolder != null && keyHolder.getKey() != null) {
			categorie.setId(keyHolder.getKey().intValue());
		}

	}

	@Override
	public List<Categorie> findAll() {
		return namedParameterJdbcTemplate.query(FIND_ALL, new CategorieRowMapper());
	}

	

	@Override
	public Categorie findById(int id) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("no_categorie", id);
		return namedParameterJdbcTemplate.queryForObject(FIND_BY_ID, namedParameters, new CategorieRowMapper());
	}
	
	@Override
	public Categorie findByLabel(String libelle) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("libelle", libelle);
		return namedParameterJdbcTemplate.queryForObject(FIND_BY_LIBELLE, namedParameters, new CategorieRowMapper());
	}
	
	@Override
	public void update(Categorie categorie) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();

		namedParameters
		.addValue("libelle", categorie.getLibelle());

		namedParameterJdbcTemplate.update(UPDATE, namedParameters);
	}
	
	@Override
	public void delete(int id) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();

		namedParameters
		.addValue("no_categorie", id);

		namedParameterJdbcTemplate.update(DELETE, namedParameters);
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
}
