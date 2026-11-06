package ma.fst.studentapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

/** Corps JSON attendu pour créer ou modifier un stage. Les dates suivent le format AAAA-MM-JJ. */
public record StageRequestDTO(

        @NotBlank(message = "L'entreprise est obligatoire")
        @Size(max = 100, message = "L'entreprise ne dépasse pas 100 caractères")
        String entreprise,

        @NotBlank(message = "Le sujet est obligatoire")
        @Size(max = 200, message = "Le sujet ne dépasse pas 200 caractères")
        String sujet,

        @NotNull(message = "La date de début est obligatoire")
        LocalDate dateDebut,

        @NotNull(message = "La date de fin est obligatoire")
        LocalDate dateFin,

        @NotNull(message = "L'étudiant est obligatoire")
        Long idEtudiant
) {
}
