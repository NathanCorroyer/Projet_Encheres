package fr.eni.projet.controller;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import fr.eni.projet.bll.CategorieService;
import fr.eni.projet.bll.UtilisateurService;
import fr.eni.projet.bo.Categorie;
import fr.eni.projet.bo.Utilisateur;

@Controller
@RequestMapping("/admin")
@SessionAttributes({"userSession"})
public class AdminController {

	@Autowired
	UtilisateurService utilisateurService;
	
	@Autowired
	CategorieService categorieService;
	
	@Autowired
	MessageSource messageSource;
	
	private Locale locale;
	public AdminController() {
		this.locale = LocaleContextHolder.getLocale();
	}

	@GetMapping("/dashboard")
	public String getDashbord() {
		return "/admin/view-admin-dashboard";
	}
	
	@GetMapping("/users")
	public String listerUtilisateurs(Model model) {
		List<Utilisateur> users = utilisateurService.findAll();
		model.addAttribute("users", users);
		return "/admin/view-admin-utilisateurs";
	}
	
	@GetMapping("/categories")
	public String listerCategories(Model model) {
		List<Categorie> categories = categorieService.findAll();
		model.addAttribute("categories", categories);
		return "/admin/view-admin-categories";
	}
	
	
	@PostMapping("/users/activation/{id}")
    @ResponseBody  // Indique que le résultat de la méthode doit être écrit dans la réponse HTTP
    public ResponseEntity<?> toggleActivation(@PathVariable("id") int id) {
		Utilisateur user = utilisateurService.findById(id);
        boolean isChangedActivation = utilisateurService.modifierActivation(user);
        if(isChangedActivation) {        	
        	user.setActif(!user.isActif());
        	return ResponseEntity.ok(user);
        }else {
        	return ResponseEntity.status(HttpStatus.BAD_REQUEST) // Code HTTP 400
                    .body(messageSource.getMessage("admin.toggle.activation.user.error", new Object[]{id}, locale));
        }
    }
	
	@PostMapping("/users/delete/{id}")
    @ResponseBody  // Indique que le résultat de la méthode doit être écrit dans la réponse HTTP
    public ResponseEntity<?> delete(@PathVariable("id") int id) {
		boolean isUserDeleted = utilisateurService.supprimerUser(id);
		if(isUserDeleted) {
			return ResponseEntity.ok("");
		}else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST) // Code HTTP 400
                    .body("Erreur lors de la suppression de l'utilisateur");
		}
    }
}
