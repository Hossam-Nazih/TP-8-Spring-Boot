package ma.fst.studentapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Corps JSON attendu pour créer ou modifier un étudiant.
 * Les @Size reprennent les longueurs des colonnes : un texte trop long reçoit
 * un message clair au lieu de provoquer une erreur SQL.
 */
public record StudentRequestDTO(

        @NotBlank(message = "Le prénom est obligatoire")
        @Size(max = 100, message = "Le prénom ne dépasse pas 100 caractères")
        String prenom,

        @NotBlank(message = "Le nom est obligatoire")
        @Size(max = 100, message = "Le nom ne dépasse pas 100 caractères")
        String nomFamille,

        @NotBlank(message = "L'email est obligatoire")
        @Email(message = "Format d'email invalide")
        @Size(max = 150, message = "L'email ne dépasse pas 150 caractères")
        String adresseEmail,

        @NotBlank(message = "La spécialité est obligatoire")
        @Size(max = 100, message = "La spécialité ne dépasse pas 100 caractères")
        String specialite,

        @NotNull(message = "L'âge est obligatoire")
        @Min(value = 17, message = "L'âge minimal est 17")
        @Max(value = 100, message = "L'âge maximal est 100")
        Integer ageEtudiant,

        // Facultatif : absent, l'étudiant reste sans département
        Long idDepartement
) {
}
