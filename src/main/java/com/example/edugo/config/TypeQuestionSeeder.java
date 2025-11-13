package com.example.edugo.config;

import com.example.edugo.entity.Principales.TypeQuestion;
import com.example.edugo.repository.TypeQuestionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TypeQuestionSeeder implements CommandLineRunner {

    private final TypeQuestionRepository repo;

    public TypeQuestionSeeder(TypeQuestionRepository repo) {
        this.repo = repo;
    }

    @Override
    public void run(String... args) {
        List<String> defaults = List.of("QCU", "QCM", "VRAI_FAUX", "APPARIEMENT");
        for (String label : defaults) {
            repo.findByLibelleTypeIgnoreCase(label)
                .orElseGet(() -> repo.save(new TypeQuestion(label)));
        }
    }
}
