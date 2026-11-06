package ma.fst.studentapi;

import ma.fst.studentapi.dto.StudentRequestDTO;
import ma.fst.studentapi.dto.StudentResponseDTO;
import ma.fst.studentapi.entity.Student;
import ma.fst.studentapi.exception.DuplicateResourceException;
import ma.fst.studentapi.exception.ResourceNotFoundException;
import ma.fst.studentapi.mapper.StudentMapper;
import ma.fst.studentapi.repository.StudentRepository;
import ma.fst.studentapi.service.DepartementService;
import ma.fst.studentapi.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/** Teste les règles du service : le repository est simulé, le mapper est le vrai. */
@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private DepartementService departementService;

    private StudentService studentService;

    private final StudentRequestDTO requete = new StudentRequestDTO(
            "Youssef", "Alaoui", "youssef.alaoui@example.com", "Mathématiques", 20, null);

    @BeforeEach
    void preparer() {
        studentService = new StudentService(studentRepository, new StudentMapper(), departementService);
    }

    private Student etudiantExistant(Long id, String email) {
        Student student = new Student();
        student.setIdEtudiant(id);
        student.setAdresseEmail(email);
        return student;
    }

    @Test
    void ajouterConvertitEtEnregistre() {
        when(studentRepository.existsByAdresseEmail(requete.adresseEmail())).thenReturn(false);
        when(studentRepository.save(any(Student.class))).then(returnsFirstArg());

        StudentResponseDTO reponse = studentService.addStudent(requete);

        assertEquals("Youssef", reponse.prenom());
        assertEquals(20, reponse.ageEtudiant());
    }

    @Test
    void ajouterRefuseUnEmailDejaUtilise() {
        when(studentRepository.existsByAdresseEmail(requete.adresseEmail())).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> studentService.addStudent(requete));
        verify(studentRepository, never()).save(any());
    }

    @Test
    void modifierAccepteDeGarderSonPropreEmail() {
        Student existant = etudiantExistant(1L, requete.adresseEmail());
        when(studentRepository.findById(1L)).thenReturn(Optional.of(existant));
        when(studentRepository.findByAdresseEmail(requete.adresseEmail())).thenReturn(Optional.of(existant));
        when(studentRepository.save(any(Student.class))).then(returnsFirstArg());

        assertEquals("Mathématiques", studentService.updateStudent(1L, requete).specialite());
    }

    @Test
    void modifierRefuseLEmailDUnAutreEtudiant() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(etudiantExistant(1L, "ancien@example.com")));
        when(studentRepository.findByAdresseEmail(requete.adresseEmail()))
                .thenReturn(Optional.of(etudiantExistant(2L, requete.adresseEmail())));

        assertThrows(DuplicateResourceException.class, () -> studentService.updateStudent(1L, requete));
        verify(studentRepository, never()).save(any());
    }

    @Test
    void rechercherUnIdentifiantInconnuEchoue() {
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> studentService.getStudentById(99L));
    }
}
