package ma.fst.studentapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** Corps JSON attendu pour créer ou modifier un département. */
public record DepartementRequestDTO(

        @NotBlank(message = "Le nom du département est obligatoire")
        @Size(max = 100, message = "Le nom du département ne dépasse pas 100 caractères")
        String nomDepartement,

        @NotBlank(message = "Le bâtiment est obligatoire")
        @Size(max = 50, message = "Le bâtiment ne dépasse pas 50 caractères")
        String batiment
) {
}
