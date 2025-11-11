package com.example.edugo.service;

import com.example.edugo.dto.ConversionRequest;
import com.example.edugo.dto.ConversionResponse;
import com.example.edugo.dto.OptionsConversionResponse;
import com.example.edugo.entity.Principales.ConversionEleve;
import com.example.edugo.entity.Principales.Eleve;
import com.example.edugo.entity.Principales.OptionsConversion;
import com.example.edugo.exception.ResourceNotFoundException;
import com.example.edugo.repository.ConversionEleveRepository;
import com.example.edugo.repository.EleveRepository;
import com.example.edugo.repository.OptionsConversionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConversionService {

    private final OptionsConversionRepository optionsConversionRepository;
    private final ConversionEleveRepository conversionEleveRepository;
    private final EleveRepository eleveRepository;

    // ====== MAPPING ENTITE <-> DTO ======
    private OptionsConversionResponse toOptionsConversionResponse(OptionsConversion option) {
        if (option == null) return null;
        OptionsConversionResponse response = new OptionsConversionResponse();
        response.setId(option.getId());
        response.setLibelle(option.getLibelle());
        response.setEtat(option.getEtat());
        response.setNbrePoint(option.getNbrePoint());
        response.setDateAjout(option.getDateAjout());
        return response;
    }

    private ConversionResponse toConversionResponse(ConversionEleve conversion, Integer pointsRestants) {
        if (conversion == null) return null;
        ConversionResponse response = new ConversionResponse();
        response.setId(conversion.getId());
        if (conversion.getOption() != null) {
            response.setLibelleOption(conversion.getOption().getLibelle());
            response.setPointsUtilises(conversion.getOption().getNbrePoint());
        }
        response.setDateConversion(conversion.getDateConversion());
        response.setPointsRestants(pointsRestants);
        return response;
    }

    // ==================== GESTION OPTIONS DE CONVERSION ====================
    
    public List<OptionsConversionResponse> getAllOptions() {
        return optionsConversionRepository.findAll().stream()
                .map(this::toOptionsConversionResponse)
                .collect(Collectors.toList());
    }

    public List<OptionsConversionResponse> getOptionsActives() {
        return optionsConversionRepository.findByEtatTrue().stream()
                .map(this::toOptionsConversionResponse)
                .collect(Collectors.toList());
    }

    public OptionsConversionResponse getOptionById(Long id) {
        OptionsConversion option = optionsConversionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Option de conversion", id));
        return toOptionsConversionResponse(option);
    }

    // ==================== CONVERSION DE POINTS ====================
    
    @Transactional
    public ConversionResponse convertirPoints(Long eleveId, ConversionRequest request) {
        // Récupérer l'élève
        Eleve eleve = eleveRepository.findById(eleveId)
                .orElseThrow(() -> new ResourceNotFoundException("Élève", eleveId));

        // Récupérer l'option de conversion
        OptionsConversion option = optionsConversionRepository.findById(request.getOptionId())
                .orElseThrow(() -> new ResourceNotFoundException("Option de conversion", request.getOptionId()));

        // Vérifier que l'option est active
        if (option.getEtat() == null || !option.getEtat()) {
            throw new RuntimeException("Cette option de conversion n'est pas disponible");
        }

        // Vérifier que l'élève a assez de points
        if (eleve.getPointAccumule() == null || eleve.getPointAccumule() < option.getNbrePoint()) {
            throw new RuntimeException("Points insuffisants. Vous avez " + 
                    (eleve.getPointAccumule() != null ? eleve.getPointAccumule() : 0) + 
                    " points, mais " + option.getNbrePoint() + " points sont requis.");
        }

        // Déduire les points
        eleve.setPointAccumule(eleve.getPointAccumule() - option.getNbrePoint());
        eleveRepository.save(eleve);

        // Créer l'enregistrement de conversion
        ConversionEleve conversion = new ConversionEleve(option, eleve);
        conversion.setDateConversion(LocalDateTime.now());
        conversion = conversionEleveRepository.save(conversion);

        // Retourner la réponse
        return toConversionResponse(conversion, eleve.getPointAccumule());
    }

    // ==================== HISTORIQUE DES CONVERSIONS ====================
    
    public List<ConversionResponse> getHistoriqueConversions(Long eleveId) {
        Eleve eleve = eleveRepository.findById(eleveId)
                .orElseThrow(() -> new ResourceNotFoundException("Élève", eleveId));

        List<ConversionEleve> conversions = conversionEleveRepository.findHistoriqueByEleveId(eleveId);
        
        return conversions.stream()
                .map(conv -> toConversionResponse(conv, eleve.getPointAccumule()))
                .collect(Collectors.toList());
    }
}

