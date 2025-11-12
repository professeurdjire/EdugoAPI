package com.example.edugo.controller;

import com.example.edugo.entity.FichierLivre;
import com.example.edugo.service.ServiceFichierLivre;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/livres")
@Tag(name = "Livres - Fichiers", description = "Gestion des fichiers liés aux livres (upload, liste, téléchargement, suppression)")
@SecurityRequirement(name = "bearerAuth")
public class LivreFichierController {

    private final ServiceFichierLivre service;

    public LivreFichierController(ServiceFichierLivre service) {
        this.service = service;
    }

    @PostMapping(path = "/{livreId}/fichiers", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Uploader un fichier pour un livre")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FichierLivreDto> uploadFichier(@Parameter(description = "ID du livre") @PathVariable Long livreId,
                                                         @RequestPart("file") MultipartFile file) throws Exception {
        FichierLivre saved = service.upload(livreId, file);
        return ResponseEntity.ok(toDto(saved));
    }

    @GetMapping(path = "/{livreId}/fichiers")
    @Operation(summary = "Lister les fichiers d'un livre")
    @PreAuthorize("hasAnyRole('ELEVE','ADMIN')")
    public ResponseEntity<List<FichierLivreDto>> listFichiers(@Parameter(description = "ID du livre") @PathVariable Long livreId) {
        List<FichierLivreDto> list = service.listByLivre(livreId).stream().map(this::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping(path = "/fichiers/{fichierId}/download")
    @Operation(summary = "Télécharger un fichier de livre")
    @PreAuthorize("hasAnyRole('ELEVE','ADMIN')")
    public ResponseEntity<Resource> download(@Parameter(description = "ID du fichier") @PathVariable Long fichierId) throws Exception {
        FichierLivre meta = service.getById(fichierId);
        Resource resource = service.download(fichierId);
        String filename = meta.getNom() != null ? meta.getNom() : ("fichier-" + fichierId);
        String encoded = URLEncoder.encode(filename, StandardCharsets.UTF_8);
        MediaType mediaType = resolveMediaType(meta.getFormat());
        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encoded)
                .body(resource);
    }

    @DeleteMapping(path = "/fichiers/{fichierId}")
    @Operation(summary = "Supprimer un fichier de livre")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@Parameter(description = "ID du fichier") @PathVariable Long fichierId) {
        service.delete(fichierId);
        return ResponseEntity.noContent().build();
    }

    private MediaType resolveMediaType(String ext) {
        if (ext == null) return MediaType.APPLICATION_OCTET_STREAM;
        String e = ext.toLowerCase();
        if (e.equals("pdf")) return MediaType.APPLICATION_PDF;
        if (e.equals("epub")) return MediaType.parseMediaType("application/epub+zip");
        if (e.equals("png")) return MediaType.IMAGE_PNG;
        if (e.equals("jpg") || e.equals("jpeg")) return MediaType.IMAGE_JPEG;
        if (e.equals("gif")) return MediaType.IMAGE_GIF;
        return MediaType.APPLICATION_OCTET_STREAM;
    }

    private FichierLivreDto toDto(FichierLivre f) {
        FichierLivreDto dto = new FichierLivreDto();
        dto.id = f.getId();
        dto.nom = f.getNom();
        dto.type = f.getType() != null ? f.getType().name() : null;
        dto.taille = f.getTaille();
        dto.format = f.getFormat();
        dto.chemin = f.getCheminFichier();
        return dto;
    }

    private static class FichierLivreDto {
        public Long id;
        public String nom;
        public String type;
        public Long taille;
        public String format;
        public String chemin;
    }
}
