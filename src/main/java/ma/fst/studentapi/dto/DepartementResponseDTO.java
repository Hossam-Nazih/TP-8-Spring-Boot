package ma.fst.studentapi.dto;

/** Données d'un département renvoyées au client. */
public record DepartementResponseDTO(
        Long idDepartement,
        String nomDepartement,
        String batiment
) {
}
