package fr.eni.projet.service;

import java.io.File;
import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileUploadService {
	private final String uploadDirectory = System.getProperty("user.home") + "/uploads/";
	
	public String uploadFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalStateException("Le fichier est vide");
        }

        // Créer le dossier d'upload s'il n'existe pas
        File uploadDir = new File(uploadDirectory);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // Sauvegarder le fichier
        String filePath = uploadDirectory + file.getOriginalFilename();
        file.transferTo(new File(filePath));

        return file.getOriginalFilename();
    }

	
	public void showDirectory() {
		System.out.println("uploadDirectory : " + uploadDirectory);
	}
}
