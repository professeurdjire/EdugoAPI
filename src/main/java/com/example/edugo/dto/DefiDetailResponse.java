package com.example.edugo.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class DefiDetailResponse {
    private Long id;
    private String titre;
    private String ennonce;
    private Integer pointDefi;
    private LocalDateTime dateAjout;
    private int nbreParticipations;
    private Long classeId;
    private String classeNom;
    private String typeDefi;
    private String reponseDefi;
    private List<EleveLiteResponse> participants;
    // Statistiques ou infos supplémentaires à ajouter ici selon besoins.
}
