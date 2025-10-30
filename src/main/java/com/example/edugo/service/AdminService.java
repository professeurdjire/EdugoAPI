package com.example.edugo.service;

import com.example.edugo.entity.Principales.*;
import com.example.edugo.entity.User;
import com.example.edugo.exception.ResourceNotFoundException;
import com.example.edugo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final NiveauRepository niveauRepository;
    private final ClasseRepository classeRepository;
    private final MatiereRepository matiereRepository;
    private final UserRepository userRepository;
    private final EleveRepository eleveRepository;
    private final LivreRepository livreRepository;
    private final ExerciceRepository exerciceRepository;
    private final ChallengeRepository challengeRepository;
    private final DefiRepository defiRepository;
    private final BadgeRepository badgeRepository;
    private final PasswordEncoder passwordEncoder;

    // ==================== GESTION UTILISATEURS ====================
    
    @Transactional
    public User createUser(User user, Long classeId) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email déjà utilisé");
        }
        
        user.setMotDePasse(passwordEncoder.encode(user.getMotDePasse()));
        user = userRepository.save(user);
        
        if (user instanceof Eleve && classeId != null) {
            Eleve eleve = (Eleve) user;
            eleve.setClasse(classeRepository.findById(classeId)
                    .orElseThrow(() -> new ResourceNotFoundException("Classe", classeId)));
            eleve.setPointAccumule(0);
            return userRepository.save(eleve);
        }
        
        return user;
    }

    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", id));
        
        user.setNom(userDetails.getNom());
        user.setPrenom(userDetails.getPrenom());
        user.setEmail(userDetails.getEmail());
        user.setEstActive(userDetails.getEstActive());
        user.setPhotoProfil(userDetails.getPhotoProfil());
        
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", id));
        userRepository.delete(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", id));
    }

    // ==================== GESTION NIVEAUX ====================
    
    @Transactional
    public Niveau createNiveau(Niveau niveau) {
        if (niveauRepository.existsByNom(niveau.getNom())) {
            throw new RuntimeException("Ce niveau existe déjà");
        }
        return niveauRepository.save(niveau);
    }

    public Niveau updateNiveau(Long id, Niveau niveauDetails) {
        Niveau niveau = niveauRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Niveau", id));
        niveau.setNom(niveauDetails.getNom());
        return niveauRepository.save(niveau);
    }

    public void deleteNiveau(Long id) {
        Niveau niveau = niveauRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Niveau", id));
        niveauRepository.delete(niveau);
    }

    public List<Niveau> getAllNiveaux() {
        return niveauRepository.findAll();
    }

    public Niveau getNiveauById(Long id) {
        return niveauRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Niveau", id));
    }

    // ==================== GESTION CLASSES ====================
    
    @Transactional
    public Classe createClasse(Classe classe) {
        return classeRepository.save(classe);
    }

    public Classe updateClasse(Long id, Classe classeDetails) {
        Classe classe = classeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Classe", id));
        classe.setNom(classeDetails.getNom());
        classe.setNiveau(classeDetails.getNiveau());
        return classeRepository.save(classe);
    }

    public void deleteClasse(Long id) {
        Classe classe = classeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Classe", id));
        classeRepository.delete(classe);
    }

    public List<Classe> getAllClasses() {
        return classeRepository.findAll();
    }

    public Classe getClasseById(Long id) {
        return classeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Classe", id));
    }

    // ==================== GESTION MATIERES ====================
    
    @Transactional
    public Matiere createMatiere(Matiere matiere) {
        if (matiereRepository.existsByNom(matiere.getNom())) {
            throw new RuntimeException("Cette matière existe déjà");
        }
        return matiereRepository.save(matiere);
    }

    public Matiere updateMatiere(Long id, Matiere matiereDetails) {
        Matiere matiere = matiereRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Matière", id));
        matiere.setNom(matiereDetails.getNom());
        return matiereRepository.save(matiere);
    }

    public void deleteMatiere(Long id) {
        Matiere matiere = matiereRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Matière", id));
        matiereRepository.delete(matiere);
    }

    public List<Matiere> getAllMatieres() {
        return matiereRepository.findAll();
    }

    public Matiere getMatiereById(Long id) {
        return matiereRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Matière", id));
    }

    // ==================== GESTION LIVRES ====================
    
    @Transactional
    public Livre createLivre(Livre livre) {
        return livreRepository.save(livre);
    }

    public Livre updateLivre(Long id, Livre livreDetails) {
        Livre livre = livreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Livre", id));
        
        livre.setTitre(livreDetails.getTitre());
        livre.setAuteur(livreDetails.getAuteur());
        livre.setDescription(livreDetails.getDescription());
        livre.setTotalPages(livreDetails.getTotalPages());
        livre.setNiveau(livreDetails.getNiveau());
        livre.setClasse(livreDetails.getClasse());
        livre.setMatiere(livreDetails.getMatiere());
        
        return livreRepository.save(livre);
    }

    public void deleteLivre(Long id) {
        Livre livre = livreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Livre", id));
        livreRepository.delete(livre);
    }

    public List<Livre> getAllLivres() {
        return livreRepository.findAll();
    }

    public Livre getLivreById(Long id) {
        return livreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Livre", id));
    }

    // ==================== GESTION EXERCICES ====================
    
    @Transactional
    public Exercice createExercice(Exercice exercice) {
        return exerciceRepository.save(exercice);
    }

    public Exercice updateExercice(Long id, Exercice exerciceDetails) {
        Exercice exercice = exerciceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exercice", id));
        
        exercice.setTitre(exerciceDetails.getTitre());
        exercice.setDescription(exerciceDetails.getDescription());
        exercice.setNiveauDifficulte(exerciceDetails.getNiveauDifficulte());
        exercice.setTempsAlloue(exerciceDetails.getTempsAlloue());
        exercice.setActive(exerciceDetails.getActive());
        
        return exerciceRepository.save(exercice);
    }

    public void deleteExercice(Long id) {
        Exercice exercice = exerciceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exercice", id));
        exerciceRepository.delete(exercice);
    }

    public List<Exercice> getAllExercices() {
        return exerciceRepository.findAll();
    }

    public Exercice getExerciceById(Long id) {
        return exerciceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exercice", id));
    }

    // ==================== GESTION DEFIS ====================
    
    @Transactional
    public Defi createDefi(Defi defi) {
        return defiRepository.save(defi);
    }

    public Defi updateDefi(Long id, Defi defiDetails) {
        Defi defi = defiRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Défi", id));
        
        defi.setTitre(defiDetails.getTitre());
        defi.setEnnonce(defiDetails.getEnnonce());
        defi.setPointDefi(defiDetails.getPointDefi());
        defi.setTypeDefi(defiDetails.getTypeDefi());
        
        return defiRepository.save(defi);
    }

    public void deleteDefi(Long id) {
        Defi defi = defiRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Défi", id));
        defiRepository.delete(defi);
    }

    public List<Defi> getAllDefis() {
        return defiRepository.findAll();
    }

    public Defi getDefiById(Long id) {
        return defiRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Défi", id));
    }

    // ==================== GESTION CHALLENGES ====================
    
    @Transactional
    public Challenge createChallenge(Challenge challenge) {
        return challengeRepository.save(challenge);
    }

    public Challenge updateChallenge(Long id, Challenge challengeDetails) {
        Challenge challenge = challengeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Challenge", id));
        
        challenge.setTitre(challengeDetails.getTitre());
        challenge.setDescription(challengeDetails.getDescription());
        challenge.setDateDebut(challengeDetails.getDateDebut());
        challenge.setDateFin(challengeDetails.getDateFin());
        challenge.setRewardMode(challengeDetails.getRewardMode());
        challenge.setTypeChallenge(challengeDetails.getTypeChallenge());
        
        return challengeRepository.save(challenge);
    }

    public void deleteChallenge(Long id) {
        Challenge challenge = challengeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Challenge", id));
        challengeRepository.delete(challenge);
    }

    public List<Challenge> getAllChallenges() {
        return challengeRepository.findAll();
    }

    public Challenge getChallengeById(Long id) {
        return challengeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Challenge", id));
    }

    // ==================== GESTION BADGES ====================
    
    @Transactional
    public Badge createBadge(Badge badge) {
        if (badgeRepository.existsByNom(badge.getNom())) {
            throw new RuntimeException("Ce badge existe déjà");
        }
        return badgeRepository.save(badge);
    }

    public Badge updateBadge(Long id, Badge badgeDetails) {
        Badge badge = badgeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Badge", id));
        
        badge.setNom(badgeDetails.getNom());
        badge.setDescription(badgeDetails.getDescription());
        badge.setType(badgeDetails.getType());
        badge.setIcone(badgeDetails.getIcone());
        
        return badgeRepository.save(badge);
    }

    public void deleteBadge(Long id) {
        Badge badge = badgeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Badge", id));
        badgeRepository.delete(badge);
    }

    public List<Badge> getAllBadges() {
        return badgeRepository.findAll();
    }

    public Badge getBadgeById(Long id) {
        return badgeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Badge", id));
    }
}

