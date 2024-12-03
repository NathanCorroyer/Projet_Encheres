package fr.eni.projet.service;

//@Service
//public class UserAnonymisationService {
//
//	@Autowired
//	private UtilisateurService utilisateurService;
//
//	@Autowired
//	private ArticleService articleService;

//	@Transactional
//	public void anonymiserUtilisateur(Long userId) throws BusinessException {
//		Utilisateur utilisateur = utilisateurService.findById()
//				.orElseThrow(() -> new BusinessException("Utilisateur non trouvé"));
//
//		List<Article> articlesEnCours = articleService.findByProprietaireAndStatutEnchere(utilisateur,
//				StatutEnchere.EN_COURS);
//
//		if (!articlesEnCours.isEmpty()) {
//			throw new BusinessException("L'utilisateur ne peut pas être anonymisé car il a des enchères en cours.");
//		}
//
//		utilisateur.setPseudo("Anonyme_" + utilisateur.getId());
//		utilisateur.setEmail("anonyme_" + utilisateur.getId() + "@example.com");
//		utilisateur.setPassword(null);
//
//		utilisateurService.save(utilisateur);
//	}
//
//}
