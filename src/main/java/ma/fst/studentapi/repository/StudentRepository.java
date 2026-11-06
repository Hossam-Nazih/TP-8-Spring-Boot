package ma.fst.studentapi.repository;

import ma.fst.studentapi.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Accès aux étudiants. Spring génère l'implémentation au démarrage ;
 * save, findAll, findById et delete sont hérités de JpaRepository.
 */
public interface StudentRepository extends JpaRepository<Student, Long> {

    // Le nom de la méthode suit celui de l'attribut : Spring en déduit la requête
    Optional<Student> findByAdresseEmail(String adresseEmail);

    boolean existsByAdresseEmail(String adresseEmail);
}
