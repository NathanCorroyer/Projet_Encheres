package fr.eni.projet.DAL.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import fr.eni.projet.DAL.ArticleDAO;
import fr.eni.projet.bo.Article;

@Repository
public class ArticleDAOJdbcImpl implements ArticleDAO{

	private final static String INSERT = "SELECT ";
	private final static String FIND_BY_ID = "";
	private final static String FIND_ALL = "";
	private final static String DELETE = "";
	private final static String UPDATE = "";
	
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Override
	public Article findArticleById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Article> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void create(Article article) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Article article) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Article article) {
		// TODO Auto-generated method stub
		
	}

	
	class ArticleRowMapper implements RowMapper<Article>{

		@Override
		public Article mapRow(ResultSet rs, int rowNum) throws SQLException {
			Article article = new Article();
			article.setId(rs.getInt("no_article"));
			article.setNom(rs.getString("nom_article"));
			article.setDescription(rs.getString("description"));
			article.setDate_debut(rs.getObject("date_debut_encheres", LocalDateTime.class));
			article.setDate_debut(rs.getObject("date_fin_encheres", LocalDateTime.class));
			article.setPrix_initial(rs.getInt("prix_initial"));
			article.setPrix_vente(rs.getInt("prix_vente"));
			
			//Relations
			article.getProprietaire().setId(rs.getInt("no_utilisateur"));;
			article.getCategorie().setId(rs.getInt("id_categorie"));
			return article;
		}
		
	}
}
