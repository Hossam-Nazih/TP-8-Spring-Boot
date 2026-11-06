package ma.fst.studentapi.dto;

import java.time.LocalDate;

/** Données d'un stage renvoyées au client, avec le nom de l'étudiant concerné. */
public record StageResponseDTO(
        Long idStage,
        String entreprise,
        String sujet,
        LocalDate dateDebut,
        LocalDate dateFin,
        Long idEtudiant,
        String nomEtudiant
) {
}
