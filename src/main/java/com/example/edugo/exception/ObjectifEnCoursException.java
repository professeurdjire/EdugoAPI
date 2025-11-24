package com.example.edugo.exception;

public class ObjectifEnCoursException extends RuntimeException {
    
    public ObjectifEnCoursException(String message) {
        super(message);
    }
    
    public ObjectifEnCoursException(String typeObjectif, long joursRestants) {
        super(String.format(
            "Vous avez déjà un objectif %s en cours. Terminez-le avant de créer un nouvel objectif. Jours restants: %d",
            typeObjectif, joursRestants
        ));
    }
}

