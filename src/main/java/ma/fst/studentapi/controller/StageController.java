package ma.fst.studentapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import ma.fst.studentapi.dto.StageRequestDTO;
import ma.fst.studentapi.dto.StageResponseDTO;
import ma.fst.studentapi.service.StageService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** Opérations HTTP sur les stages. */
@RestController
@RequestMapping("/api/stages")
@Tag(name = "Stages", description = "Stages effectués par les étudiants")
public class StageController {

    private final StageService stageService;

    public StageController(StageService stageService) {
        this.stageService = stageService;
    }

    @Operation(summary = "Ajouter un stage", description = "Répond 400 si la date de fin précède la date de début")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StageResponseDTO addStage(@Valid @RequestBody StageRequestDTO dto) {
        return stageService.addStage(dto);
    }

    @Operation(summary = "Lister tous les stages")
    @GetMapping
    public List<StageResponseDTO> getAllStages() {
        return stageService.getAllStages();
    }

    @Operation(summary = "Rechercher un stage par identifiant")
    @GetMapping("/{id}")
    public StageResponseDTO getStageById(@PathVariable("id") Long idStage) {
        return stageService.getStageById(idStage);
    }

    @Operation(summary = "Modifier un stage")
    @PutMapping("/{id}")
    public StageResponseDTO updateStage(@PathVariable("id") Long idStage, @Valid @RequestBody StageRequestDTO dto) {
        return stageService.updateStage(idStage, dto);
    }

    @Operation(summary = "Supprimer un stage")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStage(@PathVariable("id") Long idStage) {
        stageService.deleteStage(idStage);
    }
}
