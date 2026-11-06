package ma.fst.studentapi.service;

import ma.fst.studentapi.dto.DepartementRequestDTO;
import ma.fst.studentapi.dto.DepartementResponseDTO;
import ma.fst.studentapi.entity.Departement;
import ma.fst.studentapi.exception.DuplicateResourceException;
import ma.fst.studentapi.exception.ResourceNotFoundException;
import ma.fst.studentapi.mapper.DepartementMapper;
import ma.fst.studentapi.repository.DepartementRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/** Règles métier des départements. */
@Service
public class DepartementService {

    private final DepartementRepository departementRepository;
    private final DepartementMapper departementMapper;

    public DepartementService(DepartementRepository departementRepository, DepartementMapper departementMapper) {
        this.departementRepository = departementRepository;
        this.departementMapper = departementMapper;
    }

    public DepartementResponseDTO addDepartement(DepartementRequestDTO dto) {
        verifierNomDisponible(dto.nomDepartement(), null);
        return departementMapper.toResponseDTO(departementRepository.save(departementMapper.toEntity(dto)));
    }

    public List<DepartementResponseDTO> getAllDepartements() {
        return departementRepository.findAll().stream().map(departementMapper::toResponseDTO).toList();
    }

    public DepartementResponseDTO getDepartementById(Long idDepartement) {
        return departementMapper.toResponseDTO(trouver(idDepartement));
    }

    public DepartementResponseDTO updateDepartement(Long idDepartement, DepartementRequestDTO dto) {
        Departement departement = trouver(idDepartement);
        verifierNomDisponible(dto.nomDepartement(), idDepartement);
        departementMapper.updateEntityFromDTO(dto, departement);
        return departementMapper.toResponseDTO(departementRepository.save(departement));
    }

    public void deleteDepartement(Long idDepartement) {
        departementRepository.delete(trouver(idDepartement));
    }

    /** Partagée avec le service des étudiants pour vérifier un rattachement. */
    public Departement trouver(Long idDepartement) {
        return departementRepository.findById(idDepartement)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Département introuvable avec l'id : " + idDepartement));
    }

    private void verifierNomDisponible(String nomDepartement, Long idActuel) {
        departementRepository.findByNomDepartement(nomDepartement)
                .filter(existant -> !existant.getIdDepartement().equals(idActuel))
                .ifPresent(existant -> {
                    throw new DuplicateResourceException("Ce département existe déjà : " + nomDepartement);
                });
    }
}
