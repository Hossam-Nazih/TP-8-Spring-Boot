package ma.fst.studentapi.repository;

import ma.fst.studentapi.entity.Departement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DepartementRepository extends JpaRepository<Departement, Long> {

    Optional<Departement> findByNomDepartement(String nomDepartement);
}
