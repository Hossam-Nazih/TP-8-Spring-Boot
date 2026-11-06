package ma.fst.studentapi.service;

import ma.fst.studentapi.dto.StageRequestDTO;
import ma.fst.studentapi.dto.StageResponseDTO;
import ma.fst.studentapi.entity.Stage;
import ma.fst.studentapi.exception.InvalidDataException;
import ma.fst.studentapi.exception.ResourceNotFoundException;
import ma.fst.studentapi.mapper.StageMapper;
import ma.fst.studentapi.repository.StageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/** Règles métier des stages. */
@Service
public class StageService {

    private final StageRepository stageRepository;
    private final StageMapper stageMapper;
    private final StudentService studentService;

    public StageService(StageRepository stageRepository, StageMapper stageMapper, StudentService studentService) {
        this.stageRepository = stageRepository;
        this.stageMapper = stageMapper;
        this.studentService = studentService;
    }

    public StageResponseDTO addStage(StageRequestDTO dto) {
        return enregistrer(dto, new Stage());
    }

    public List<StageResponseDTO> getAllStages() {
        return stageRepository.findAll().stream().map(stageMapper::toResponseDTO).toList();
    }

    public StageResponseDTO getStageById(Long idStage) {
        return stageMapper.toResponseDTO(trouver(idStage));
    }

    /** Vérifie d'abord l'étudiant, pour répondre 404 s'il n'existe pas plutôt qu'une liste vide. */
    public List<StageResponseDTO> getStagesByStudent(Long idEtudiant) {
        studentService.trouver(idEtudiant);
        return stageRepository.findByEtudiantIdEtudiantOrderByDateDebutDesc(idEtudiant).stream()
                .map(stageMapper::toResponseDTO)
                .toList();
    }

    public StageResponseDTO updateStage(Long idStage, StageRequestDTO dto) {
        return enregistrer(dto, trouver(idStage));
    }

    public void deleteStage(Long idStage) {
        stageRepository.delete(trouver(idStage));
    }

    private StageResponseDTO enregistrer(StageRequestDTO dto, Stage stage) {
        // Cette règle compare deux champs : elle ne peut pas être portée par une annotation simple
        if (dto.dateFin().isBefore(dto.dateDebut())) {
            throw new InvalidDataException("La date de fin ne peut pas précéder la date de début");
        }
        stage.setEtudiant(studentService.trouver(dto.idEtudiant()));
        stageMapper.updateEntityFromDTO(dto, stage);
        return stageMapper.toResponseDTO(stageRepository.save(stage));
    }

    private Stage trouver(Long idStage) {
        return stageRepository.findById(idStage)
                .orElseThrow(() -> new ResourceNotFoundException("Stage introuvable avec l'id : " + idStage));
    }
}
