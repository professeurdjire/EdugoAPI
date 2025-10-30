package com.example.edugo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileUploadService {

    @Value("${app.file.upload-dir:./uploads}")
    private String uploadDir;

    public String uploadFile(MultipartFile file, String subdirectory) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("Le fichier est vide");
        }

        // Créer le dossier s'il n'existe pas
        Path uploadPath = Paths.get(uploadDir, subdirectory);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Générer un nom de fichier unique
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String filename = UUID.randomUUID().toString() + extension;

        // Sauvegarder le fichier
        Path filePath = uploadPath.resolve(filename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return String.format("/uploads/%s/%s", subdirectory, filename);
    }

    public void deleteFile(String filePath) throws IOException {
        Path path = Paths.get(uploadDir, filePath);
        if (Files.exists(path)) {
            Files.delete(path);
        }
    }

    public String uploadImage(MultipartFile file) throws IOException {
        return uploadFile(file, "images");
    }

    public String uploadDocument(MultipartFile file) throws IOException {
        return uploadFile(file, "documents");
    }
}

