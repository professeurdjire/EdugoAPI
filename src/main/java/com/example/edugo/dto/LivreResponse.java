package com.example.edugo.dto;

import lombok.Data;

@Data
public class LivreResponse {
    private Long id;
    private String titre;
    private String isbn;
    private String auteur;
    private String imageCouverture;
    private Integer totalPages;
}
