package ma.fst.studentapi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Structure de stockage d'un étudiant, liée à la table students.
 * Cette classe n'est jamais envoyée au client : l'API passe par les DTO.
 */
@Entity
@Table(name = "students")
public class Student {

    /** La valeur est produite par l'auto-incrément de la base. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEtudiant;

    @Column(nullable = false, length = 100)
    private String prenom;

    @Column(nullable = false, length = 100)
    private String nomFamille;

    /** La contrainte unique protège des doublons même si deux requêtes arrivent en même temps. */
    @Column(nullable = false, unique = true, length = 150)
    private String adresseEmail;

    @Column(nullable = false, length = 100)
    private String specialite;

    @Column(nullable = false)
    private Integer ageEtudiant;

    /** Facultatif : un étudiant peut exister avant d'être rattaché à un département. */
    @ManyToOne
    @JoinColumn(name = "id_departement")
    private Departement departement;

    public Student() {
    }

    public Long getIdEtudiant() {
        return idEtudiant;
    }

    public void setIdEtudiant(Long idEtudiant) {
        this.idEtudiant = idEtudiant;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNomFamille() {
        return nomFamille;
    }

    public void setNomFamille(String nomFamille) {
        this.nomFamille = nomFamille;
    }

    public String getAdresseEmail() {
        return adresseEmail;
    }

    public void setAdresseEmail(String adresseEmail) {
        this.adresseEmail = adresseEmail;
    }

    public String getSpecialite() {
        return specialite;
    }

    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }

    public Integer getAgeEtudiant() {
        return ageEtudiant;
    }

    public void setAgeEtudiant(Integer ageEtudiant) {
        this.ageEtudiant = ageEtudiant;
    }

    public Departement getDepartement() {
        return departement;
    }

    public void setDepartement(Departement departement) {
        this.departement = departement;
    }
}
