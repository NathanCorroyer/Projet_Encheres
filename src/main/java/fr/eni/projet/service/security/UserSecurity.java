package fr.eni.projet.service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import fr.eni.projet.bll.ArticleService;
import fr.eni.projet.bo.Utilisateur;
import fr.eni.projet.service.SessionService;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class UserSecurity {

	@Autowired
	private ArticleService articleService;
	@Autowired
	private static SessionService sessionService;
	
	public UserSecurity(SessionService sessionService) {
		UserSecurity.sessionService = sessionService;
	}

	public static AuthorizationDecision hasAccessToUser(Authentication auth, HttpServletRequest request) {
		String requestedPseudo = request.getParameter("pseudo");
		String loggedInUser = auth.getName(); // Retrieve the logged-in username
		return new AuthorizationDecision(loggedInUser.equals(requestedPseudo));
	}

	public AuthorizationDecision hasAccessToArticle(Authentication auth, HttpServletRequest request) {
		String uri = request.getRequestURI();
		String[] uriParts = uri.split("/");
		String articleIdStr = uriParts[uriParts.length - 1];
		if (articleIdStr == null) {
			return new AuthorizationDecision(false); // Aucun ID d'article trouvé
		}

		try {
			int articleId = Integer.parseInt(articleIdStr);
			String loggedInUser = auth.getName();

			// Récupérer l'ID de l'utilisateur propriétaire de l'article
			var article = articleService.findById(articleId);

			// Vérifier que l'utilisateur connecté est le propriétaire de l'article
			String articleOwner = article.getProprietaire().getPseudo();

			return new AuthorizationDecision(loggedInUser.equals(articleOwner));

		} catch (NumberFormatException e) {
			return new AuthorizationDecision(false); // L'ID de l'article n'est pas valide
		}
	}

	public static AuthorizationDecision isUserActif() {
		Utilisateur userConnecte = sessionService.getUserSessionAttribute();
		return new AuthorizationDecision(userConnecte.isActif());
	} 
}