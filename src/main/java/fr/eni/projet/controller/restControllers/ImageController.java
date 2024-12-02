package fr.eni.projet.controller.restControllers;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Path;
import java.nio.file.Paths;

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
            	
                return ResponseEntity.ok().body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
