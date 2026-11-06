package ma.fst.studentapi.service;

import ma.fst.studentapi.dto.StudentRequestDTO;
import ma.fst.studentapi.dto.StudentResponseDTO;
import ma.fst.studentapi.entity.Student;
import ma.fst.studentapi.exception.DuplicateResourceException;
import ma.fst.studentapi.exception.ResourceNotFoundException;
import ma.fst.studentapi.mapper.StudentMapper;
import ma.fst.studentapi.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Règles métier des étudiants. Le contrôleur ne manipule que des DTO,
 * le repository que des entités : la conversion se fait ici.
 */
@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final DepartementService departementService;

    public StudentService(StudentRepository studentRepository, StudentMapper studentMapper,
                          DepartementService departementService) {
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
        this.departementService = departementService;
    }

    public StudentResponseDTO addStudent(StudentRequestDTO dto) {
        if (studentRepository.existsByAdresseEmail(dto.adresseEmail())) {
            throw new DuplicateResourceException("Cet email est déjà utilisé : " + dto.adresseEmail());
        }
        Student student = studentMapper.toEntity(dto);
        rattacherDepartement(dto, student);
        return studentMapper.toResponseDTO(studentRepository.save(student));
    }

    public List<StudentResponseDTO> getAllStudents() {
        return studentRepository.findAll().stream().map(studentMapper::toResponseDTO).toList();
    }

    public StudentResponseDTO getStudentById(Long idEtudiant) {
        return studentMapper.toResponseDTO(trouver(idEtudiant));
    }

    public StudentResponseDTO updateStudent(Long idEtudiant, StudentRequestDTO dto) {
        Student student = trouver(idEtudiant);
        // L'étudiant peut garder son email ; seul un email porté par un autre est refusé
        studentRepository.findByAdresseEmail(dto.adresseEmail())
                .filter(existant -> !existant.getIdEtudiant().equals(idEtudiant))
                .ifPresent(existant -> {
                    throw new DuplicateResourceException("Cet email est déjà utilisé : " + dto.adresseEmail());
                });

        studentMapper.updateEntityFromDTO(dto, student);
        rattacherDepartement(dto, student);
        return studentMapper.toResponseDTO(studentRepository.save(student));
    }

    public void deleteStudent(Long idEtudiant) {
        studentRepository.delete(trouver(idEtudiant));
    }

    /** Partagée avec le service des stages. */
    public Student trouver(Long idEtudiant) {
        return studentRepository.findById(idEtudiant)
                .orElseThrow(() -> new ResourceNotFoundException("Étudiant introuvable avec l'id : " + idEtudiant));
    }

    private void rattacherDepartement(StudentRequestDTO dto, Student student) {
        student.setDepartement(dto.idDepartement() == null ? null : departementService.trouver(dto.idDepartement()));
    }
}
