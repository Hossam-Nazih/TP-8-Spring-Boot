package ma.fst.studentapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import ma.fst.studentapi.dto.DepartementRequestDTO;
import ma.fst.studentapi.dto.DepartementResponseDTO;
import ma.fst.studentapi.service.DepartementService;
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

/** Opérations HTTP sur les départements. */
@RestController
@RequestMapping("/api/departements")
@Tag(name = "Départements", description = "Gestion des départements de rattachement")
public class DepartementController {

    private final DepartementService departementService;

    public DepartementController(DepartementService departementService) {
        this.departementService = departementService;
    }

    @Operation(summary = "Ajouter un département", description = "Répond 409 si le nom existe déjà")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DepartementResponseDTO addDepartement(@Valid @RequestBody DepartementRequestDTO dto) {
        return departementService.addDepartement(dto);
    }

    @Operation(summary = "Lister tous les départements")
    @GetMapping
    public List<DepartementResponseDTO> getAllDepartements() {
        return departementService.getAllDepartements();
    }

    @Operation(summary = "Rechercher un département par identifiant")
    @GetMapping("/{id}")
    public DepartementResponseDTO getDepartementById(@PathVariable("id") Long idDepartement) {
        return departementService.getDepartementById(idDepartement);
    }

    @Operation(summary = "Modifier un département")
    @PutMapping("/{id}")
    public DepartementResponseDTO updateDepartement(@PathVariable("id") Long idDepartement,
                                                    @Valid @RequestBody DepartementRequestDTO dto) {
        return departementService.updateDepartement(idDepartement, dto);
    }

    @Operation(summary = "Supprimer un département", description = "Répond 409 si des étudiants y sont rattachés")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDepartement(@PathVariable("id") Long idDepartement) {
        departementService.deleteDepartement(idDepartement);
    }
}
