package com.example.edugo.service;

import com.example.edugo.dto.*;
import com.example.edugo.entity.Principales.*;
import com.example.edugo.entity.User;
import com.example.edugo.exception.ResourceNotFoundException;
import com.example.edugo.exception.ResourceAlreadyExistsException;
import com.example.edugo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
    private final PartenaireRepository partenaireRepository;
    private final QuizRepository quizRepository;
    private final LangueRepository langueRepository;
    private final StatistiqueService statistiqueService;
    private final PasswordEncoder passwordEncoder;

    // ====== MAPPING ENTITE <-> DTO ======
    private NiveauResponse toNiveauResponse(Niveau niveau) {
        if (niveau == null) return null;
        return new NiveauResponse(niveau.getId(), niveau.getNom());
    }

    private Niveau toNiveauEntity(NiveauRequest dto) {
        if (dto == null) return null;
        Niveau niveau = new Niveau();
        niveau.setNom(dto.getNom());
        return niveau;
    }

    // ====== MAPPING PARTENAIRE ENTITE <-> DTO ======
    private PartenaireResponse toPartenaireResponse(Partenaire partenaire) {
        if (partenaire == null) return null;
        return new PartenaireResponse(
                partenaire.getId(),
                partenaire.getNom(),
                partenaire.getDescription(),
                partenaire.getLogoUrl(),
                partenaire.getSiteWeb(),
                partenaire.getDomaine(),
                partenaire.getType(),
                partenaire.getEmail(),
                partenaire.getTelephone(),
                partenaire.getPays(),
                deriveStatut(partenaire.getActif()),
                partenaire.getDateCreation() != null ? partenaire.getDateCreation().toString() : null,
                partenaire.getNewsletter(),
                partenaire.getDateCreation(),
                partenaire.getDateModification(),
                partenaire.getActif()
        );
    }

    private Partenaire toPartenaireEntity(PartenaireRequest dto) {
        if (dto == null) return null;
        Partenaire partenaire = new Partenaire();
        partenaire.setNom(dto.getNom());
        partenaire.setDescription(dto.getDescription());
        partenaire.setLogoUrl(dto.getLogoUrl());
        partenaire.setSiteWeb(dto.getSiteWeb());
        partenaire.setActif(dto.getActif());
        partenaire.setDomaine(dto.getDomaine());
        partenaire.setType(dto.getType());
        partenaire.setEmail(dto.getEmail());
        partenaire.setTelephone(dto.getTelephone());
        partenaire.setPays(dto.getPays());
        partenaire.setNewsletter(dto.getNewsletter());
        return partenaire;
    }

    // ====== MAPPING CLASSE ENTITE <-> DTO ======
    private ClasseResponse toClasseResponse(Classe classe) {
        if (classe == null) return null;
        ClasseResponse response = new ClasseResponse();
        response.setId(classe.getId());
        response.setNom(classe.getNom());
        if (classe.getNiveau() != null) {
            response.setNiveauId(classe.getNiveau().getId());
            response.setNiveauNom(classe.getNiveau().getNom());
        }
        return response;
    }

    // ====== MAPPING MATIERE ENTITE <-> DTO ======
    private MatiereResponse toMatiereResponse(Matiere matiere) {
        if (matiere == null) return null;
        return new MatiereResponse(
                matiere.getId(),
                matiere.getNom(),
                matiere.getDateCreation(),
                matiere.getDateModification()
        );
    }

    // ====== MAPPING LIVRE ENTITE <-> DTO ======
    private LivreResponse toLivreResponse(Livre livre) {
        if (livre == null) return null;
        LivreResponse response = new LivreResponse();
        response.setId(livre.getId());
        response.setTitre(livre.getTitre());
        response.setIsbn(livre.getIsbn());
        response.setAuteur(livre.getAuteur());
        response.setImageCouverture(livre.getImageCouverture());
        response.setTotalPages(livre.getTotalPages());
        return response;
    }

    // ====== MAPPING EXERCICE ENTITE <-> DTO ======
    private ExerciceResponse toExerciceResponse(Exercice exercice) {
        if (exercice == null) return null;
        ExerciceResponse response = new ExerciceResponse();
        response.setId(exercice.getId());
        response.setTitre(exercice.getTitre());
        response.setActive(exercice.getActive());
        response.setNiveauDifficulte(exercice.getNiveauDifficulte());
        response.setTempsAlloue(exercice.getTempsAlloue());
        if (exercice.getMatiere() != null) {
            response.setMatiereId(exercice.getMatiere().getId());
            response.setMatiereNom(exercice.getMatiere().getNom());
        }
        return response;
    }

    // ====== MAPPING DEFI ENTITE <-> DTO ======
    private DefiResponse toDefiResponse(Defi defi) {
        if (defi == null) return null;
        DefiResponse response = new DefiResponse();
        response.setId(defi.getId());
        response.setTitre(defi.getTitre());
        response.setPointDefi(defi.getPointDefi());
        response.setDateAjout(defi.getDateAjout());
        response.setTypeDefi(defi.getTypeDefi() != null ? defi.getTypeDefi().toString() : null);
        if (defi.getClasse() != null) {
            response.setClasseId(defi.getClasse().getId());
            response.setClasseNom(defi.getClasse().getNom());
        }
        // Compter les participations
        response.setNbreParticipations(defi.getEleveDefis() != null ? defi.getEleveDefis().size() : 0);
        return response;
    }

    // ====== MAPPING CHALLENGE ENTITE <-> DTO ======
    private ChallengeResponse toChallengeResponse(Challenge challenge) {
        if (challenge == null) return null;
        ChallengeResponse response = new ChallengeResponse();
        response.setId(challenge.getId());
        response.setTitre(challenge.getTitre());
        response.setDescription(challenge.getDescription());
        response.setDateDebut(challenge.getDateDebut());
        response.setDateFin(challenge.getDateFin());
        response.setPoints(challenge.getPoints());
        response.setTheme(challenge.getTypeChallenge() != null ? challenge.getTypeChallenge().toString() : null);
        return response;
    }

    // ====== MAPPING BADGE ENTITE <-> DTO ======
    private BadgeResponse toBadgeResponse(Badge badge) {
        if (badge == null) return null;
        return new BadgeResponse(
                badge.getId(),
                badge.getNom(),
                badge.getDescription(),
                badge.getType() != null ? badge.getType().toString() : null,
                badge.getIcone()
        );
    }

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

    // Changement de mot de passe pour un utilisateur (admin ou autre)
    @Transactional
    public void changeUserPassword(Long userId, String oldPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", userId));

        if (!passwordEncoder.matches(oldPassword, user.getMotDePasse())) {
            throw new RuntimeException("Ancien mot de passe incorrect");
        }

        user.setMotDePasse(passwordEncoder.encode(newPassword));
        userRepository.save(user);
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

    // ==================== GESTION NIVEAUX DTO ====================
    public List<NiveauResponse> getAllNiveauxDto() {
        return niveauRepository.findAll().stream().map(this::toNiveauResponse).collect(Collectors.toList());
    }

    public NiveauResponse getNiveauByIdDto(Long id) {
        Niveau niveau = getNiveauById(id);
        return toNiveauResponse(niveau);
    }

    @Transactional
    public NiveauResponse createNiveauDto(NiveauRequest dto) {
        if (niveauRepository.existsByNom(dto.getNom())) {
            throw new RuntimeException("Ce niveau existe déjà");
        }
        Niveau niveau = toNiveauEntity(dto);
        Niveau saved = niveauRepository.save(niveau);
        return toNiveauResponse(saved);
    }

    @Transactional
    public NiveauResponse updateNiveauDto(Long id, NiveauRequest dto) {
        Niveau niveau = niveauRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Niveau", id));
        niveau.setNom(dto.getNom());
        Niveau saved = niveauRepository.save(niveau);
        return toNiveauResponse(saved);
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

    // ==================== GESTION CLASSES DTO ====================
    public List<ClasseResponse> getAllClassesDto() {
        return classeRepository.findAll().stream().map(this::toClasseResponse).collect(Collectors.toList());
    }

    public ClasseResponse getClasseByIdDto(Long id) {
        Classe classe = getClasseById(id);
        return toClasseResponse(classe);
    }

    @Transactional
    public ClasseResponse createClasseDto(ClasseRequest dto) {
        Classe classe = new Classe();
        classe.setNom(dto.getNom());
        if (dto.getNiveauId() != null) {
            Niveau niveau = niveauRepository.findById(dto.getNiveauId())
                    .orElseThrow(() -> new ResourceNotFoundException("Niveau", dto.getNiveauId()));
            classe.setNiveau(niveau);
        }
        Classe saved = classeRepository.save(classe);
        return toClasseResponse(saved);
    }

    @Transactional
    public ClasseResponse updateClasseDto(Long id, ClasseRequest dto) {
        Classe classe = classeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Classe", id));
        classe.setNom(dto.getNom());
        if (dto.getNiveauId() != null) {
            Niveau niveau = niveauRepository.findById(dto.getNiveauId())
                    .orElseThrow(() -> new ResourceNotFoundException("Niveau", dto.getNiveauId()));
            classe.setNiveau(niveau);
        }
        Classe saved = classeRepository.save(classe);
        return toClasseResponse(saved);
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

    // ==================== GESTION MATIERES DTO ====================
    public List<MatiereResponse> getAllMatieresDto() {
        return matiereRepository.findAll().stream().map(this::toMatiereResponse).collect(Collectors.toList());
    }

    public MatiereResponse getMatiereByIdDto(Long id) {
        Matiere matiere = getMatiereById(id);
        return toMatiereResponse(matiere);
    }

    @Transactional
    public MatiereResponse createMatiereDto(MatiereRequest dto) {
        if (matiereRepository.existsByNom(dto.getNom())) {
            throw new RuntimeException("Cette matière existe déjà");
        }
        Matiere matiere = new Matiere();
        matiere.setNom(dto.getNom());
        Matiere saved = matiereRepository.save(matiere);
        return toMatiereResponse(saved);
    }

    @Transactional
    public MatiereResponse updateMatiereDto(Long id, MatiereRequest dto) {
        Matiere matiere = matiereRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Matière", id));
        matiere.setNom(dto.getNom());
        Matiere saved = matiereRepository.save(matiere);
        return toMatiereResponse(saved);
    }

    // ==================== GESTION LIVRES ====================
    
    @Transactional
    public Livre createLivre(Livre livre) {
        // Résoudre les entités liées pour éviter les instances transientes
        if (livre.getNiveau() != null) {
            Long id = livre.getNiveau().getId();
            livre.setNiveau(id != null ?
                    niveauRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Niveau", id)) : null);
        }
        if (livre.getClasse() != null) {
            Long id = livre.getClasse().getId();
            livre.setClasse(id != null ?
                    classeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Classe", id)) : null);
        }
        if (livre.getMatiere() != null) {
            Long id = livre.getMatiere().getId();
            livre.setMatiere(id != null ?
                    matiereRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Matière", id)) : null);
        }
        if (livre.getLangue() != null) {
            Long id = livre.getLangue().getId();
            livre.setLangue(id != null ?
                    langueRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Langue", id)) : null);
        }
        // Éviter de tenter de persister un Quiz non sauvegardé si envoyé dans la requête
        if (livre.getQuiz() != null && livre.getQuiz().getId() == null) {
            livre.setQuiz(null);
        }
        // Vérifier l'unicité de l'ISBN pour éviter une violation de contrainte SQL
        String isbn = livre.getIsbn();
        if (isbn != null && !isbn.isBlank() && livreRepository.existsByIsbn(isbn)) {
            throw new ResourceAlreadyExistsException("ISBN", isbn);
        }
        return livreRepository.save(livre);
    }

    public Livre updateLivre(Long id, Livre livreDetails) {
        Livre livre = livreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Livre", id));
        
        // Champs simples
        livre.setTitre(livreDetails.getTitre());
        livre.setIsbn(livreDetails.getIsbn());
        livre.setAuteur(livreDetails.getAuteur());
        livre.setDescription(livreDetails.getDescription());
        livre.setAnneePublication(livreDetails.getAnneePublication());
        livre.setEditeur(livreDetails.getEditeur());
        livre.setImageCouverture(livreDetails.getImageCouverture());
        livre.setLectureAuto(livreDetails.getLectureAuto());
        livre.setInteractif(livreDetails.getInteractif());
        livre.setTotalPages(livreDetails.getTotalPages());

        // Relations: recharger par ID pour éviter transients
        if (livreDetails.getNiveau() != null) {
            Long nivId = livreDetails.getNiveau().getId();
            livre.setNiveau(nivId != null ?
                    niveauRepository.findById(nivId).orElseThrow(() -> new ResourceNotFoundException("Niveau", nivId)) : null);
        } else { livre.setNiveau(null); }

        if (livreDetails.getClasse() != null) {
            Long clsId = livreDetails.getClasse().getId();
            livre.setClasse(clsId != null ?
                    classeRepository.findById(clsId).orElseThrow(() -> new ResourceNotFoundException("Classe", clsId)) : null);
        } else { livre.setClasse(null); }

        if (livreDetails.getMatiere() != null) {
            Long matId = livreDetails.getMatiere().getId();
            livre.setMatiere(matId != null ?
                    matiereRepository.findById(matId).orElseThrow(() -> new ResourceNotFoundException("Matière", matId)) : null);
        } else { livre.setMatiere(null); }

        if (livreDetails.getLangue() != null) {
            Long lanId = livreDetails.getLangue().getId();
            livre.setLangue(lanId != null ?
                    langueRepository.findById(lanId).orElseThrow(() -> new ResourceNotFoundException("Langue", lanId)) : null);
        } else { livre.setLangue(null); }
        // Quiz: on ne modifie pas ici

        // Vérifier l'unicité de l'ISBN sur mise à jour
        String newIsbn = livre.getIsbn();
        if (newIsbn != null && !newIsbn.isBlank() && livreRepository.existsByIsbnAndIdNot(newIsbn, livre.getId())) {
            throw new ResourceAlreadyExistsException("ISBN", newIsbn);
        }
        
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

    // ==================== GESTION LIVRES DTO ====================
    public List<LivreResponse> getAllLivresDto() {
        return livreRepository.findAll().stream().map(this::toLivreResponse).collect(Collectors.toList());
    }

    public LivreResponse getLivreByIdDto(Long id) {
        Livre livre = getLivreById(id);
        return toLivreResponse(livre);
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

    // ==================== GESTION EXERCICES DTO ====================
    public List<ExerciceResponse> getAllExercicesDto() {
        return exerciceRepository.findAll().stream().map(this::toExerciceResponse).collect(Collectors.toList());
    }

    public ExerciceResponse getExerciceByIdDto(Long id) {
        Exercice exercice = getExerciceById(id);
        return toExerciceResponse(exercice);
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

    // ==================== GESTION DEFIS DTO ====================
    public List<DefiResponse> getAllDefisDto() {
        return defiRepository.findAll().stream().map(this::toDefiResponse).collect(Collectors.toList());
    }

    public DefiResponse getDefiByIdDto(Long id) {
        Defi defi = getDefiById(id);
        return toDefiResponse(defi);
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

    // ==================== GESTION CHALLENGES DTO ====================
    public List<ChallengeResponse> getAllChallengesDto() {
        return challengeRepository.findAll().stream().map(this::toChallengeResponse).collect(Collectors.toList());
    }

    public ChallengeResponse getChallengeByIdDto(Long id) {
        Challenge challenge = getChallengeById(id);
        return toChallengeResponse(challenge);
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

    // ==================== GESTION BADGES DTO ====================
    public List<BadgeResponse> getAllBadgesDto() {
        return badgeRepository.findAll().stream().map(this::toBadgeResponse).collect(Collectors.toList());
    }

    public BadgeResponse getBadgeByIdDto(Long id) {
        Badge badge = getBadgeById(id);
        return toBadgeResponse(badge);
    }

    // ==================== GESTION PARTENAIRES ====================
    
    @Transactional
    public Partenaire createPartenaire(Partenaire partenaire) {
        if (partenaireRepository.existsByNom(partenaire.getNom())) {
            throw new RuntimeException("Ce partenaire existe déjà");
        }
        return partenaireRepository.save(partenaire);
    }

    public Partenaire updatePartenaire(Long id, Partenaire partenaireDetails) {
        Partenaire partenaire = partenaireRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Partenaire", id));
        
        partenaire.setNom(partenaireDetails.getNom());
        partenaire.setDescription(partenaireDetails.getDescription());
        partenaire.setLogoUrl(partenaireDetails.getLogoUrl());
        partenaire.setSiteWeb(partenaireDetails.getSiteWeb());
        partenaire.setActif(partenaireDetails.getActif());
        
        return partenaireRepository.save(partenaire);
    }

    public void deletePartenaire(Long id) {
        Partenaire partenaire = partenaireRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Partenaire", id));
        partenaireRepository.delete(partenaire);
    }

    public List<Partenaire> getAllPartenaires() {
        return partenaireRepository.findAll();
    }

    public Partenaire getPartenaireById(Long id) {
        return partenaireRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Partenaire", id));
    }

    // ==================== GESTION PARTENAIRES DTO ====================
    public List<PartenaireResponse> getAllPartenairesDto() {
        return partenaireRepository.findAll().stream().map(this::toPartenaireResponse).collect(Collectors.toList());
    }

    public PartenaireResponse getPartenaireByIdDto(Long id) {
        Partenaire partenaire = getPartenaireById(id);
        return toPartenaireResponse(partenaire);
    }

    @Transactional
    public PartenaireResponse createPartenaireDto(PartenaireRequest dto) {
        if (partenaireRepository.existsByNom(dto.getNom())) {
            throw new RuntimeException("Ce partenaire existe déjà");
        }
        Partenaire partenaire = toPartenaireEntity(dto);
        Partenaire saved = partenaireRepository.save(partenaire);
        return toPartenaireResponse(saved);
    }

    @Transactional
    public PartenaireResponse updatePartenaireDto(Long id, PartenaireRequest dto) {
        Partenaire partenaire = partenaireRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Partenaire", id));
        partenaire.setNom(dto.getNom());
        partenaire.setDescription(dto.getDescription());
        partenaire.setLogoUrl(dto.getLogoUrl());
        partenaire.setSiteWeb(dto.getSiteWeb());
        partenaire.setActif(dto.getActif());
        Partenaire saved = partenaireRepository.save(partenaire);
        return toPartenaireResponse(saved);
    }
    
    // ==================== GESTION QUIZZES ====================
    
    @Transactional
    public Quiz createQuiz(Quiz quiz) {
        return quizRepository.save(quiz);
    }

    public Quiz updateQuiz(Long id, Quiz quizDetails) {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz", id));
        
        quiz.setStatut(quizDetails.getStatut());
        
        return quizRepository.save(quiz);
    }

    public void deleteQuiz(Long id) {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz", id));
        quizRepository.delete(quiz);
    }

    public List<Quiz> getAllQuizzes() {
        return quizRepository.findAll();
    }

    public Quiz getQuizById(Long id) {
        return quizRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz", id));
    }
    
    // ====== Helpers ======
    private String deriveStatut(Boolean actif) {
        if (actif == null) return "en_attente";
        return actif ? "actif" : "inactif";
    }

    // ==================== STATISTIQUES ====================
    public StatistiquesPlateformeResponse getStatistiquesPlateforme() {
        return statistiqueService.getStatistiquesPlateforme();
    }
}

