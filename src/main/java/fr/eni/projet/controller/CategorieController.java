package fr.eni.projet.controller;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import fr.eni.projet.bll.CategorieService;
import fr.eni.projet.bo.Categorie;
import fr.eni.projet.exceptions.BusinessException;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/categories")
public class CategorieController {

	@Autowired
	private CategorieService categorieService;
	@Autowired
	private MessageSource messageSource;
	private Locale locale;
	
	public CategorieController() {
		this.locale = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()) != null ? ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()) 
                .getRequest()
                .getLocale() : Locale.FRANCE;
		
	}
	@GetMapping("/creer")
	public String creerCategorieGet(@ModelAttribute("categorie") Categorie categorie) {
		return "/categories/view-creer-categorie";
	}

	@PostMapping("/creer")
	public String creerCategorie(@Valid @ModelAttribute("categorie") Categorie categorie, BindingResult bindingResult) {
		try {
			categorieService.create(categorie);
			return "redirect:/admin/categories";			
		} catch (BusinessException be) {
			// Ajoute les messages d'erreur métier au BindingResult
			be.getClefsExternalisations().forEach(key -> {
				ObjectError error = new ObjectError("globalError", key);
				bindingResult.addError(error);
			});
			return "/categories/view-creer-categorie";
		}
	}
	
	
	@GetMapping("/modifier/{id}")
	public String modifierCategorieGet(@PathVariable("id") int id, Model model) {
		Categorie categorie = categorieService.findById(id); 
		if(categorie != null) {
			model.addAttribute("categorie", categorie);			
			return "/categories/view-modifier-categorie";
		}
		return "redirect:/admin/categories";
	}

	@PostMapping("/modifier/{id}")
	public String modifierCategorie(@Valid @ModelAttribute("categorie") Categorie categorie, BindingResult bindingResult) {
		try {
			categorieService.update(categorie);
			return "redirect:/admin/categories";			
		} catch (BusinessException be) {
			// Ajoute les messages d'erreur métier au BindingResult
			be.getClefsExternalisations().forEach(key -> {
				ObjectError error = new ObjectError("globalError", key);
				bindingResult.addError(error);
			});
			return "categories/view-modifier-categorie";
		}
	}
	
	
	@PostMapping("/delete/{id}")
    @ResponseBody  // Indique que le résultat de la méthode doit être écrit dans la réponse HTTP
    public ResponseEntity<?> delete(@PathVariable("id") int id) {
		locale = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()) != null ? ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()) 
                .getRequest()
                .getLocale() : Locale.FRANCE;
		System.out.println(locale);
		boolean isCategorieDeleted = categorieService.delete(id);
		if(isCategorieDeleted) {
			return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body("{\"message\": \"" + messageSource.getMessage("categorie.suppr.ok", null ,locale) +"\"}");
		}else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON) // Code HTTP 400
                    .body("{\"message\": \"" + messageSource.getMessage("categorie.suppr.error", null ,locale) +"\"}");
		}
    }
	
	
}
