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
import org.springframework.transaction.annotation.Transactional;

import fr.eni.projet.DAL.AdresseDAO;
import fr.eni.projet.bo.Adresse;
import fr.eni.projet.bo.Utilisateur;

@Repository
public class AdresseDAOJdbcImpl implements AdresseDAO {

	private final static String INSERT = "INSERT INTO ADRESSES(rue, code_postal, ville, adresse_eni) VALUES (:rue, :code_postal, :ville, :adresse_eni)";
	private final static String FIND_BY_ID = "SELECT no_adresse, rue, code_postal, ville, adresse_eni FROM ADRESSES WHERE no_adresse = :id";
	private final static String FIND_ALL = "SELECT no_adresse, rue, code_postal, ville, adresse_eni FROM ADRESSES";
	private final static String UPDATE = "UPDATE ADRESSES SET rue = :rue, code_postal = :code_postal, ville = :ville, adresse_eni = :adresse_eni WHERE no_adresse = :id";
	private final static String DELETE = "DELETE FROM ADRESSES WHERE no_adresse = :id";
	private final static String FIND_BY_USER = "SELECT a.no_adresse, a.rue, a.code_postal, a.ville, a.adresse_eni "
			+ "FROM ADRESSES a " + "INNER JOIN UTILISATEURS u ON u.no_adresse = a.no_adresse "
			+ "WHERE u.no_utilisateur = :utilisateur_id";
	private final static String FIND_BY_ENI = "SELECT * FROM ADRESSES WHERE adresse_eni = :adresse_eni";

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Transactional
	@Override
	public int create(Adresse adresse) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		KeyHolder keyHolder = new GeneratedKeyHolder();

		namedParameters.addValue("rue", adresse.getRue()).addValue("code_postal", adresse.getCode_postal())
				.addValue("ville", adresse.getVille()).addValue("adresse_eni", adresse.isAdresse_eni());

		int rowsAffected = namedParameterJdbcTemplate.update(INSERT, namedParameters, keyHolder);

		// Log de vérification
		if (keyHolder.getKey() != null) {
			adresse.setId(keyHolder.getKey().intValue());
			return keyHolder.getKey().intValue();
		} else {

		}
		return 0;
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

		namedParameters.addValue("id", adresse.getId()).addValue("rue", adresse.getRue())
				.addValue("code_postal", adresse.getCode_postal()).addValue("ville", adresse.getVille())
				.addValue("adresse_eni", adresse.isAdresse_eni());

		namedParameterJdbcTemplate.update(UPDATE, namedParameters);
	}

	@Override
	public void delete(int id) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("id", id);

		namedParameterJdbcTemplate.update(DELETE, namedParameters);
	}

	@Override
	public List<Adresse> findByUtilisateur(Utilisateur utilisateur) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("utilisateur_id", utilisateur.getId());

		return namedParameterJdbcTemplate.query(FIND_BY_USER, namedParameters, new AdresseRowMapper());
	}

	@Override
	public List<Adresse> findByAdresseENI(boolean isENI) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("adresse_eni", isENI);

		return namedParameterJdbcTemplate.query(FIND_BY_ENI, namedParameters, new AdresseRowMapper());
	}

	class AdresseRowMapper implements RowMapper<Adresse> {
		@Override
		public Adresse mapRow(ResultSet rs, int rowNum) throws SQLException {
			Adresse adresse = new Adresse();
			adresse.setId(rs.getInt("no_adresse"));
			adresse.setRue(rs.getString("rue"));
			adresse.setCode_postal(rs.getString("code_postal"));
			adresse.setVille(rs.getString("ville"));
			adresse.setAdresse_eni(rs.getBoolean("adresse_eni"));

			return adresse;
		}
	}

}