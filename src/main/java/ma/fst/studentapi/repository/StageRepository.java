package ma.fst.studentapi.repository;

import ma.fst.studentapi.entity.Stage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StageRepository extends JpaRepository<Stage, Long> {

    // Parcourt la relation etudiant -> idEtudiant
    List<Stage> findByEtudiantIdEtudiantOrderByDateDebutDesc(Long idEtudiant);
}
