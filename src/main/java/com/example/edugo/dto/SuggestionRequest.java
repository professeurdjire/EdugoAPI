package com.example.edugo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SuggestionRequest {

    @NotBlank(message = "Le contenu de la suggestion est obligatoire")
    @Size(min = 10, max = 1000, message = "La suggestion doit contenir entre 10 et 1000 caractères")
    private String contenu;

    private Long eleveId; // Optionnel, sera généralement récupéré du contexte de sécurité
}