package fr.eni.projet.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.eni.projet.bll.EnchereService;
import fr.eni.projet.bo.Enchere;

// First test of logout home page, useless for now

@Controller
@RequestMapping("/encheres")
public class EnchereController {
	private EnchereService enchereService;

	public EnchereController(EnchereService enchereService) {
		this.enchereService = enchereService;
	}
	
	@GetMapping
	public String afficherEncheres(Model model) {
		List<Enchere> lstEncheres = enchereService.getEncheres();
		model.addAttribute("encheres", lstEncheres);
		
		return null;
	}
}
