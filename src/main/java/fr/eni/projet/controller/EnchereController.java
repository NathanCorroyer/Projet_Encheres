package fr.eni.projet.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.eni.projet.bll.EnchereService;
import fr.eni.projet.bll.UtilisateurService;
import fr.eni.projet.bo.Enchere;
import fr.eni.projet.bo.Utilisateur;
import fr.eni.projet.exceptions.BusinessException;

// First test of logout home page, useless for now

@Controller
@RequestMapping("/encheres")
public class EnchereController {
	private EnchereService enchereService;
	private UtilisateurService userService;
	@Autowired
	private MessageSource messageSource;
	private Locale locale;
	public EnchereController(EnchereService enchereService, UtilisateurService userService) {
		this.enchereService = enchereService;
		this.userService = userService;
		this.locale = LocaleContextHolder.getLocale();
	}
	
	@GetMapping
	public String afficherEncheres(Model model) {
		List<Enchere> lstEncheres = enchereService.getEncheres();
		model.addAttribute("encheres", lstEncheres);
		
		return null;
	}
	
	@PostMapping("/creer")
	public String creerEnchere(@ModelAttribute Enchere enchere, BindingResult bindingResult, Authentication auth) {
		Utilisateur user = userService.findByPseudo(auth.getName()).get();
		enchere.setAcheteur(user);
		enchere.setDate(LocalDateTime.now());
		try {
			enchereService.create(enchere);
		} catch (BusinessException be) {
			List<String> errorMessages = new ArrayList<>();
			be.getClefsExternalisations().forEach(key -> {
			    String errorMessage = messageSource.getMessage(key, null, locale);
			    errorMessages.add(errorMessage);
			});
		}
		return "redirect:/articles/details/" + enchere.getArticle().getId();
	}
}
