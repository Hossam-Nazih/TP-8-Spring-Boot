package ma.fst.studentapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import ma.fst.studentapi.dto.StageResponseDTO;
import ma.fst.studentapi.dto.StudentRequestDTO;
import ma.fst.studentapi.dto.StudentResponseDTO;
import ma.fst.studentapi.service.StageService;
import ma.fst.studentapi.service.StudentService;
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

/**
 * Opérations HTTP sur les étudiants.
 * Le contrôleur reçoit la requête et délègue : aucune règle métier n'est écrite ici.
 */
@RestController
@RequestMapping("/api/students")
@Tag(name = "Étudiants", description = "Création, consultation, modification et suppression")
public class StudentController {

    private final StudentService studentService;
    private final StageService stageService;

    public StudentController(StudentService studentService, StageService stageService) {
        this.studentService = studentService;
        this.stageService = stageService;
    }

    @Operation(summary = "Ajouter un étudiant", description = "Répond 409 si l'email est déjà utilisé")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StudentResponseDTO addStudent(@Valid @RequestBody StudentRequestDTO dto) {
        // @Valid rejette le corps avec un code 400 avant d'entrer dans cette méthode
        return studentService.addStudent(dto);
    }

    @Operation(summary = "Lister tous les étudiants")
    @GetMapping
    public List<StudentResponseDTO> getAllStudents() {
        return studentService.getAllStudents();
    }

    @Operation(summary = "Rechercher un étudiant par identifiant")
    @GetMapping("/{id}")
    public StudentResponseDTO getStudentById(@PathVariable("id") Long idEtudiant) {
        return studentService.getStudentById(idEtudiant);
    }

    @Operation(summary = "Modifier un étudiant")
    @PutMapping("/{id}")
    public StudentResponseDTO updateStudent(@PathVariable("id") Long idEtudiant,
                                            @Valid @RequestBody StudentRequestDTO dto) {
        return studentService.updateStudent(idEtudiant, dto);
    }

    @Operation(summary = "Supprimer un étudiant", description = "Répond 409 s'il possède encore des stages")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStudent(@PathVariable("id") Long idEtudiant) {
        studentService.deleteStudent(idEtudiant);
    }

    @Operation(summary = "Lister les stages d'un étudiant")
    @GetMapping("/{id}/stages")
    public List<StageResponseDTO> getStagesOfStudent(@PathVariable("id") Long idEtudiant) {
        return stageService.getStagesByStudent(idEtudiant);
    }
}
