package ma.fst.studentapi.mapper;

import ma.fst.studentapi.dto.StageRequestDTO;
import ma.fst.studentapi.dto.StageResponseDTO;
import ma.fst.studentapi.entity.Stage;
import ma.fst.studentapi.entity.Student;
import org.springframework.stereotype.Component;

/** Conversion entre l'entité Stage et ses DTO ; le service fournit l'étudiant. */
@Component
public class StageMapper {

    public void updateEntityFromDTO(StageRequestDTO dto, Stage stage) {
        stage.setEntreprise(dto.entreprise());
        stage.setSujet(dto.sujet());
        stage.setDateDebut(dto.dateDebut());
        stage.setDateFin(dto.dateFin());
    }

    public StageResponseDTO toResponseDTO(Stage stage) {
        Student etudiant = stage.getEtudiant();
        return new StageResponseDTO(
                stage.getIdStage(),
                stage.getEntreprise(),
                stage.getSujet(),
                stage.getDateDebut(),
                stage.getDateFin(),
                etudiant.getIdEtudiant(),
                etudiant.getPrenom() + " " + etudiant.getNomFamille()
        );
    }
}
