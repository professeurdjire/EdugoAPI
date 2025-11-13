package com.example.edugo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class SuggestionRequest {

    @NotBlank(message = "Le contenu de la suggestion est obligatoire")
    @Size(min = 10, max = 1000, message = "La suggestion doit contenir entre 10 et 1000 caractères")
    private String contenu;

    private Long eleveId;

    public SuggestionRequest(String contenu, Long eleveId) {
        this.contenu = contenu;
        this.eleveId = eleveId;
    }

    public @NotBlank(message = "Le contenu de la suggestion est obligatoire") @Size(min = 10, max = 1000, message = "La suggestion doit contenir entre 10 et 1000 caractères") String getContenu() {
        return contenu;
    }

    public void setContenu(@NotBlank(message = "Le contenu de la suggestion est obligatoire") @Size(min = 10, max = 1000, message = "La suggestion doit contenir entre 10 et 1000 caractères") String contenu) {
        this.contenu = contenu;
    }

    public Long getEleveId() {return eleveId;}
    public void setEleveId(Long eleveId) {this.eleveId = eleveId;}
}