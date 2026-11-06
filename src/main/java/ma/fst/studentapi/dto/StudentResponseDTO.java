package ma.fst.studentapi.dto;

/** Données d'un étudiant renvoyées au client, avec l'identifiant généré. */
public record StudentResponseDTO(
        Long idEtudiant,
        String prenom,
        String nomFamille,
        String adresseEmail,
        String specialite,
        Integer ageEtudiant,
        Long idDepartement,
        String nomDepartement
) {
}
