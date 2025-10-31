package com.example.edugo.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DefiRequest {
    private String titre;
    private String ennonce;
    private Integer pointDefi;
    private Long classeId;
    private String typeDefi;
    private String reponseDefi;
}
