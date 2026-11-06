package ma.fst.studentapi.mapper;

import ma.fst.studentapi.dto.DepartementRequestDTO;
import ma.fst.studentapi.dto.DepartementResponseDTO;
import ma.fst.studentapi.entity.Departement;
import org.springframework.stereotype.Component;

/** Conversion entre l'entité Departement et ses DTO. */
@Component
public class DepartementMapper {

    public Departement toEntity(DepartementRequestDTO dto) {
        Departement departement = new Departement();
        updateEntityFromDTO(dto, departement);
        return departement;
    }

    public DepartementResponseDTO toResponseDTO(Departement departement) {
        return new DepartementResponseDTO(
                departement.getIdDepartement(),
                departement.getNomDepartement(),
                departement.getBatiment()
        );
    }

    public void updateEntityFromDTO(DepartementRequestDTO dto, Departement departement) {
        departement.setNomDepartement(dto.nomDepartement());
        departement.setBatiment(dto.batiment());
    }
}
