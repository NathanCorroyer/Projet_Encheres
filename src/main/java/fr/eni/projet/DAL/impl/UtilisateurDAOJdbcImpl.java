package fr.eni.projet.DAL.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import fr.eni.projet.DAL.UtilisateurDAO;
import fr.eni.projet.bo.Adresse;
import fr.eni.projet.bo.Utilisateur;

@Repository
public class UtilisateurDAOJdbcImpl implements UtilisateurDAO {

	private final static String INSERT = "INSERT INTO UTILISATEURS(pseudo, nom, prenom, email, telephone, no_adresse, mot_de_passe, credit, code_role) VALUES (:pseudo, :nom, :prenom, :email, :telephone, :no_adresse, :mot_de_passe, :credit, :code_role)";
	private final static String FIND_BY_ID = "SELECT no_utilisateur, pseudo, nom, prenom, email, telephone, no_adresse, credit, actif, code_role FROM UTILISATEURS WHERE no_utilisateur = :no_utilisateur";
	private final static String FIND_BY_PSEUDO = "SELECT no_utilisateur, pseudo, nom, prenom, email, telephone, no_adresse, credit, actif, code_role FROM UTILISATEURS WHERE pseudo = :pseudo";
	private final static String FIND_ALL = "SELECT no_utilisateur, pseudo, nom, prenom, email, telephone, no_adresse, credit, actif, code_role FROM UTILISATEURS";
	private final static String UPDATE = "UPDATE UTILISATEURS SET nom = :nom, prenom = :prenom, email = :email, telephone = :telephone WHERE no_utilisateur = :no_utilisateur";
	private final static String UPDATE_PASSWORD = "UPDATE UTILISATEURS SET password = :password WHERE no_utilisateur = :no_utilisateur";
	private final static String MODIFIER_ACTIVATION = "UPDATE UTILISATEURS SET actif = :actif WHERE no_utilisateur = :no_utilisateur";
	private final static String FIND_BY_EMAIL = "SELECT no_utilisateur, pseudo, nom, prenom, email, telephone, no_adresse, credit, actif, code_role FROM UTILISATEURS WHERE email = :email";
	
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Override
	public void create(Utilisateur utilisateur) {

		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		KeyHolder keyHolder = new GeneratedKeyHolder();

		namedParameters.addValue("pseudo", utilisateur.getPseudo()).addValue("nom", utilisateur.getNom())
				.addValue("prenom", utilisateur.getPrenom()).addValue("email", utilisateur.getEmail())
				.addValue("telephone", utilisateur.getTelephone())
				.addValue("no_adresse", utilisateur.getAdresse().getId())
				.addValue("mot_de_passe", utilisateur.getPassword()).addValue("credit", utilisateur.getCredit())
				.addValue("code_role", 1);

		namedParameterJdbcTemplate.update(INSERT, namedParameters, keyHolder);

		if (keyHolder != null && keyHolder.getKey() != null) {
			utilisateur.setId(keyHolder.getKey().intValue());
		}
	}

	@Override
	public Utilisateur findById(int id) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("no_utilisateur", id);
		return namedParameterJdbcTemplate.queryForObject(FIND_BY_ID, namedParameters, new UtilisateurRowMapper());
	}

	@Override
	public Optional<Utilisateur> findByPseudo(String pseudo) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("pseudo", pseudo);

		List<Utilisateur> results = namedParameterJdbcTemplate.query(FIND_BY_PSEUDO, namedParameters,
				new UtilisateurRowMapper());
		if (results.isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(results.get(0));
	}

	@Override
	public Optional<Utilisateur> findByEmail(String email) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("email", email);
		List<Utilisateur> results = namedParameterJdbcTemplate.query(FIND_BY_EMAIL, namedParameters,
				new UtilisateurRowMapper());
		if (results.isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(results.get(0));
	}

	@Override
	public List<Utilisateur> findAll() {
		return namedParameterJdbcTemplate.query(FIND_ALL, new UtilisateurRowMapper());
	}

	@Override
	public void update(Utilisateur utilisateur) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("nom", utilisateur.getNom())
				.addValue("prenom", utilisateur.getPrenom()).addValue("email", utilisateur.getEmail())
				.addValue("telephone", utilisateur.getTelephone())
				.addValue("no_utilisateur", utilisateur.getId());

		namedParameterJdbcTemplate.update(UPDATE, namedParameters);
	}

	
	@Override
	public void modifierActivation(Utilisateur utilisateur) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("no_utilisateur", utilisateur.getId()).addValue("actif", !utilisateur.isActif());

		namedParameterJdbcTemplate.update(MODIFIER_ACTIVATION, namedParameters);
	}

	class UtilisateurRowMapper implements RowMapper<Utilisateur> {

		@Override
		public Utilisateur mapRow(ResultSet rs, int rowNum) throws SQLException {
			Utilisateur user = new Utilisateur();

			user.setId(rs.getInt("no_utilisateur"));
			user.setPseudo(rs.getString("pseudo"));
			user.setNom(rs.getString("nom"));
			user.setPrenom(rs.getString("prenom"));
			user.setEmail(rs.getString("email"));
			user.setTelephone(rs.getString("telephone"));

			Adresse adresse = new Adresse();
			adresse.setId(rs.getInt("no_adresse"));
			user.setAdresse(adresse);

			user.setCredit(rs.getInt("credit"));
			user.setActif(rs.getBoolean("actif"));
			user.setCode_role(rs.getInt("code_role"));

			return user;
		}
	}

	@Override
	public void updatePassword(Utilisateur utilisateur) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("password", utilisateur.getPassword())
				.addValue("no_utilisateur", utilisateur.getId());

		namedParameterJdbcTemplate.update(UPDATE_PASSWORD, namedParameters);
		
	}

}