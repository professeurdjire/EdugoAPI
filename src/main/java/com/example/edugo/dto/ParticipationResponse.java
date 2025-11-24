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
public class ParticipationResponse {
    private Long id;
    private Long utilisateurId;
    private String utilisateurNom;
    private Long activiteId;
    private String typeActivite;
    private String statut;
    private LocalDateTime dateParticipation;
}
