package fr.eni.projet.service.security;

import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class UserSecurity {

    public static AuthorizationDecision hasAccessToUser(Authentication auth, HttpServletRequest request) {
        String requestedPseudo = request.getParameter("pseudo");
        String loggedInUser = auth.getName();  // Retrieve the logged-in username
        System.out.println(request);
        System.out.println(requestedPseudo);
        System.out.println(loggedInUser);
        return new AuthorizationDecision(loggedInUser.equals(requestedPseudo));
    }
}