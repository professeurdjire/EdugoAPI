package com.example.edugo.entity;



import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "question")
@Data
@NoArgsConstructor
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String enonce;

    @Column(nullable = false)
    private Integer ordre;

    @Column(nullable = false)
    private Integer points;

    @Column(nullable = false)
    private LocalDateTime dateCreation;

    @Column(nullable = false)
    private LocalDateTime dateModification;

    @ManyToOne
    @JoinColumn(name = "id_quiz", nullable = false)
    private Quiz quiz;

    // Constructeur
    public Question(String enonce, Integer ordre, Integer points, Quiz quiz) {
        this.enonce = enonce;
        this.ordre = ordre;
        this.points = points;
        this.quiz = quiz;
        this.dateCreation = LocalDateTime.now();
        this.dateModification = LocalDateTime.now();
    }
}
