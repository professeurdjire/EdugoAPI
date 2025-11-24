package com.example.edugo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EleveDefiResponse {
    private Long id;
    private Long eleveId;
    private String nom;
    private String prenom;
    private Long defiId;
    private String defiTitre;
    private LocalDateTime dateEnvoie;
    private String statut;
}
