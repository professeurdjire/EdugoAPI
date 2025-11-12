package com.example.edugo.service;

import com.example.edugo.entity.FichierLivre;
import com.example.edugo.entity.FichierLivre.TypeFichier;
import com.example.edugo.entity.Principales.Livre;
import com.example.edugo.exception.ResourceNotFoundException;
import com.example.edugo.repository.FichierLivreRepository;
import com.example.edugo.repository.LivreRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDDocument;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class ServiceFichierLivre {

    private final FichierLivreRepository fichierLivreRepository;
    private final LivreRepository livreRepository;
    private final FileUploadService fileUploadService;

    @Value("${app.file.upload-dir:./uploads}")
    private String uploadDir;

    public ServiceFichierLivre(FichierLivreRepository fichierLivreRepository,
                               LivreRepository livreRepository,
                               FileUploadService fileUploadService) {
        this.fichierLivreRepository = fichierLivreRepository;
        this.livreRepository = livreRepository;
        this.fileUploadService = fileUploadService;
    }

    @Transactional
    public FichierLivre upload(Long livreId, MultipartFile file) throws IOException {
        Livre livre = livreRepository.findById(livreId)
                .orElseThrow(() -> new ResourceNotFoundException("Livre", livreId));

        String storedPath = fileUploadService.uploadDocument(file); // returns like "/uploads/documents/<uuid>.<ext>"

        FichierLivre entity = new FichierLivre();
        entity.setLivre(livre);
        entity.setNom(StringUtils.cleanPath(file.getOriginalFilename() != null ? file.getOriginalFilename() : "fichier"));
        entity.setCheminFichier(storedPath);
        entity.setTaille(file.getSize());
        String original = file.getOriginalFilename();
        String ext = (original != null && original.contains(".")) ? original.substring(original.lastIndexOf('.') + 1) : null;
        entity.setFormat(ext);
        entity.setType(guessType(ext));
        FichierLivre saved = fichierLivreRepository.save(entity);

        // Auto-calc total pages for PDF if not set
        if (saved.getType() == TypeFichier.PDF && (livre.getTotalPages() == null || livre.getTotalPages() == 0)) {
            try (PDDocument doc = PDDocument.load(file.getInputStream())) {
                int pages = doc.getNumberOfPages();
                livre.setTotalPages(pages);
                livreRepository.save(livre);
            } catch (Exception ignored) {
                // Do not fail upload if page count fails
            }
        }
        return saved;
    }

    @Transactional(readOnly = true)
    public List<FichierLivre> listByLivre(Long livreId) {
        return fichierLivreRepository.findByLivreId(livreId);
    }

    @Transactional(readOnly = true)
    public FichierLivre getById(Long fichierId) {
        return fichierLivreRepository.findById(fichierId)
                .orElseThrow(() -> new ResourceNotFoundException("FichierLivre", fichierId));
    }

    @Transactional(readOnly = true)
    public Resource download(Long fichierId) throws MalformedURLException {
        FichierLivre f = fichierLivreRepository.findById(fichierId)
                .orElseThrow(() -> new ResourceNotFoundException("FichierLivre", fichierId));
        Path path = resolveStoredPath(f.getCheminFichier());
        Resource resource = new UrlResource(path.toUri());
        if (!resource.exists()) {
            throw new MalformedURLException("Fichier introuvable: " + path);
        }
        return resource;
    }

    @Transactional
    public void delete(Long fichierId) {
        FichierLivre f = fichierLivreRepository.findById(fichierId)
                .orElseThrow(() -> new ResourceNotFoundException("FichierLivre", fichierId));
        // Optionnel: supprimer le fichier physique si nécessaire
        // On laisse la suppression disque pour plus tard pour éviter les incohérences entre chemins relatifs/absolus
        fichierLivreRepository.delete(f);
    }

    private Path resolveStoredPath(String stored) {
        // stored expected like "/uploads/documents/<filename>"
        if (stored == null) {
            return Paths.get(uploadDir);
        }
        String normalized = stored.replace("\\", "/");
        if (normalized.startsWith("/uploads/")) {
            normalized = normalized.substring("/uploads/".length());
        } else if (normalized.startsWith("uploads/")) {
            normalized = normalized.substring("uploads/".length());
        }
        return Paths.get(uploadDir).resolve(normalized).normalize();
    }

    private TypeFichier guessType(String ext) {
        if (ext == null) return TypeFichier.PDF;
        String e = ext.toLowerCase();
        if (e.equals("pdf")) return TypeFichier.PDF;
        if (e.equals("epub")) return TypeFichier.EPUB;
        if (e.equals("jpg") || e.equals("jpeg") || e.equals("png") || e.equals("gif")) return TypeFichier.IMAGE;
        if (e.equals("mp4") || e.equals("webm") || e.equals("mkv")) return TypeFichier.VIDEO;
        if (e.equals("mp3") || e.equals("wav") || e.equals("m4a")) return TypeFichier.AUDIO;
        return TypeFichier.PDF;
    }
}
