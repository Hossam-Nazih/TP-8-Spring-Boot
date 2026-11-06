package ma.fst.studentapi.mapper;

import ma.fst.studentapi.dto.StudentRequestDTO;
import ma.fst.studentapi.dto.StudentResponseDTO;
import ma.fst.studentapi.entity.Departement;
import ma.fst.studentapi.entity.Student;
import org.springframework.stereotype.Component;

/**
 * Conversion manuelle entre l'entité Student et ses DTO.
 * Le département n'est pas traité ici : le service doit d'abord vérifier qu'il existe.
 */
@Component
public class StudentMapper {

    /** Construit une nouvelle entité ; son identifiant sera attribué par la base. */
    public Student toEntity(StudentRequestDTO dto) {
        Student student = new Student();
        updateEntityFromDTO(dto, student);
        return student;
    }

    public StudentResponseDTO toResponseDTO(Student student) {
        Departement departement = student.getDepartement();
        return new StudentResponseDTO(
                student.getIdEtudiant(),
                student.getPrenom(),
                student.getNomFamille(),
                student.getAdresseEmail(),
                student.getSpecialite(),
                student.getAgeEtudiant(),
                departement == null ? null : departement.getIdDepartement(),
                departement == null ? null : departement.getNomDepartement()
        );
    }

    /** Recopie les champs reçus sur une entité existante, sans toucher à son identifiant. */
    public void updateEntityFromDTO(StudentRequestDTO dto, Student student) {
        student.setPrenom(dto.prenom());
        student.setNomFamille(dto.nomFamille());
        student.setAdresseEmail(dto.adresseEmail());
        student.setSpecialite(dto.specialite());
        student.setAgeEtudiant(dto.ageEtudiant());
    }
}
