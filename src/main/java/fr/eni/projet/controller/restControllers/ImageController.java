package fr.eni.projet.controller.restControllers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ImageController {

    private final String uploadDirectory = System.getProperty("user.home") + "/uploads/";

    // Endpoint pour servir les images des articles
    @GetMapping("/uploads/{filename}")
    public ResponseEntity<Resource> getImage(@PathVariable("filename") String filename) {
        try {
            // Construction du chemin absolu du fichier
            Path filePath = Paths.get(uploadDirectory).resolve(filename);
            Resource resource = new UrlResource(filePath.toUri());
            
            // Vérification si le fichier existe
            if (resource.exists() && resource.isReadable()) {
                // Retourner le fichier en tant que ressource
            	
            	 // Détection du type MIME
                String mimeType = Files.probeContentType(filePath);
                
                // Si le type MIME est indéfini, on peut le définir à image/jpeg par défaut
                if (mimeType == null) {
                    mimeType = "image/jpeg";
                }
                return ResponseEntity.ok() .contentType(MediaType.parseMediaType(mimeType)).body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
