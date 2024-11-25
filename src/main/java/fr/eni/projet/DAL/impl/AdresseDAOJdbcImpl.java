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

import fr.eni.projet.DAL.AdresseDAO;
import fr.eni.projet.bo.Adresse;

@Repository
public class AdresseDAOJdbcImpl implements AdresseDAO {

	private final static String INSERT = "INSERT INTO ADRESSES(rue, code_postal, ville) VALUES (:rue, :code_postal, :ville)";
	private final static String FIND_BY_ID = "SELECT id, rue, code_postal, ville FROM ADRESSES WHERE id = :id";
	private final static String FIND_ALL = "SELECT id, rue, code_postal, ville FROM ADRESSES";
	private final static String UPDATE = "UPDATE ADRESSES SET rue = :rue, code_postal = :code_postal, ville = :ville WHERE id = :id";
	private final static String DELETE = "DELETE FROM ADRESSES WHERE id = :id";

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Override
	public void create(Adresse adresse) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		KeyHolder keyHolder = new GeneratedKeyHolder();

		namedParameters.addValue("rue", adresse.getRue()).addValue("code_postal", adresse.getCode_postal())
				.addValue("ville", adresse.getVille());

		namedParameterJdbcTemplate.update(INSERT, namedParameters, keyHolder);

		// Récupérer l'ID généré pour l'adresse
		if (keyHolder.getKey() != null) {
			adresse.setId_article(keyHolder.getKey().intValue());
		}
	}

	@Override
	public Adresse findById(int id) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("id", id);

		return namedParameterJdbcTemplate.queryForObject(FIND_BY_ID, namedParameters, new AdresseRowMapper());
	}

	@Override
	public List<Adresse> findAll() {
		return namedParameterJdbcTemplate.query(FIND_ALL, new AdresseRowMapper());
	}

	@Override
	public void update(Adresse adresse) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();

		namedParameters.addValue("id", adresse.getId_article()).addValue("rue", adresse.getRue())
				.addValue("code_postal", adresse.getCode_postal()).addValue("ville", adresse.getVille());

		namedParameterJdbcTemplate.update(UPDATE, namedParameters);
	}

	@Override
	public void delete(int id) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("id", id);

		namedParameterJdbcTemplate.update(DELETE, namedParameters);
	}

	class AdresseRowMapper implements RowMapper<Adresse> {
		@Override
		public Adresse mapRow(ResultSet rs, int rowNum) throws SQLException {
			Adresse adresse = new Adresse();
			adresse.setId_article(rs.getInt("id"));
			adresse.setRue(rs.getString("rue"));
			adresse.setCode_postal(rs.getString("code_postal"));
			adresse.setVille(rs.getString("ville"));
			return adresse;
		}
	}

	@Override
	public Adresse readByIdArticle(int id) {
		// TODO Auto-generated method stub
		return null;
	}
}