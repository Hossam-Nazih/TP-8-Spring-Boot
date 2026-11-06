package ma.fst.studentapi;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Parcourt l'API sur une base H2 : scénario de l'étape 11, relations et codes d'erreur.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class StudentApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private ResultActions envoyerPost(String url, String json) throws Exception {
        return mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(json));
    }

    private ResultActions envoyerPut(String url, String json) throws Exception {
        return mockMvc.perform(put(url).contentType(MediaType.APPLICATION_JSON).content(json));
    }

    private String etudiant(String email) {
        return """
                {"prenom":"Youssef","nomFamille":"Alaoui","adresseEmail":"%s",
                 "specialite":"Mathématiques","ageEtudiant":20}""".formatted(email);
    }

    private long creer(String url, String json, String champId) throws Exception {
        String reponse = envoyerPost(url, json).andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return ((Number) JsonPath.read(reponse, "$." + champId)).longValue();
    }

    @Test
    void scenarioDeLEtape11() throws Exception {
        // Test 1 : ajout
        long id = creer("/api/students", etudiant("youssef.alaoui@example.com"), "idEtudiant");

        // Test 2 : liste
        mockMvc.perform(get("/api/students")).andExpect(status().isOk())
                .andExpect(content().string(containsString("youssef.alaoui@example.com")));

        // Test 3 : recherche par identifiant
        mockMvc.perform(get("/api/students/" + id)).andExpect(status().isOk())
                .andExpect(jsonPath("$.prenom").value("Youssef"));

        // Test 4 : modification
        envoyerPut("/api/students/" + id, """
                {"prenom":"Youssef","nomFamille":"Alaoui","adresseEmail":"youssef.new@example.com",
                 "specialite":"Data Science","ageEtudiant":21}""")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.adresseEmail").value("youssef.new@example.com"))
                .andExpect(jsonPath("$.specialite").value("Data Science"))
                .andExpect(jsonPath("$.ageEtudiant").value(21));

        // Tests 5 et 6 : suppression, puis 404
        mockMvc.perform(delete("/api/students/" + id)).andExpect(status().isNoContent());
        mockMvc.perform(get("/api/students/" + id)).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Ressource introuvable"));
    }

    @Test
    void lesDonneesInvalidesRenvoient400AvecTousLesMessages() throws Exception {
        envoyerPost("/api/students", """
                {"prenom":"","nomFamille":"X","adresseEmail":"pas-un-mail","specialite":"X","ageEtudiant":12}""")
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Erreur de validation"))
                .andExpect(jsonPath("$.erreurs", hasSize(3)));
    }

    @Test
    void unTexteTropLongRenvoie400EtNonUneErreurSql() throws Exception {
        envoyerPost("/api/students", """
                {"prenom":"%s","nomFamille":"X","adresseEmail":"long@example.com","specialite":"X","ageEtudiant":20}"""
                .formatted("x".repeat(101)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void unEmailEnDoubleRenvoie409SansDetailSql() throws Exception {
        creer("/api/students", etudiant("double@example.com"), "idEtudiant");

        envoyerPost("/api/students", etudiant("double@example.com"))
                .andExpect(status().isConflict())
                .andExpect(content().string(not(containsString("insert"))));
    }

    @Test
    void modifierVersLEmailDUnAutreRenvoie409() throws Exception {
        creer("/api/students", etudiant("premier@example.com"), "idEtudiant");
        long second = creer("/api/students", etudiant("second@example.com"), "idEtudiant");

        envoyerPut("/api/students/" + second, etudiant("premier@example.com")).andExpect(status().isConflict());
        envoyerPut("/api/students/" + second, etudiant("second@example.com")).andExpect(status().isOk());
    }

    @Test
    void lesErreursDuClientNeDeviennentPasDes500() throws Exception {
        envoyerPost("/api/students", "{ json cassé").andExpect(status().isBadRequest());
        mockMvc.perform(get("/api/students/abc")).andExpect(status().isBadRequest());
        mockMvc.perform(get("/api/route-inconnue")).andExpect(status().isNotFound());
        mockMvc.perform(delete("/api/students")).andExpect(status().isMethodNotAllowed());
    }

    @Test
    void unEtudiantPeutEtreRattacheAUnDepartementExistant() throws Exception {
        long departement = creer("/api/departements",
                "{\"nomDepartement\":\"Informatique\",\"batiment\":\"Bloc A\"}", "idDepartement");

        envoyerPost("/api/students", """
                {"prenom":"Sara","nomFamille":"El Amrani","adresseEmail":"sara@example.com",
                 "specialite":"Génie logiciel","ageEtudiant":21,"idDepartement":%d}""".formatted(departement))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nomDepartement").value("Informatique"));

        envoyerPost("/api/departements", "{\"nomDepartement\":\"Informatique\",\"batiment\":\"Bloc B\"}")
                .andExpect(status().isConflict());
        envoyerPost("/api/students", """
                {"prenom":"X","nomFamille":"X","adresseEmail":"x@example.com",
                 "specialite":"X","ageEtudiant":21,"idDepartement":999999}""")
                .andExpect(status().isNotFound());
        mockMvc.perform(delete("/api/departements/" + departement)).andExpect(status().isConflict());
    }

    @Test
    void cycleDeVieDUnStage() throws Exception {
        long id = creer("/api/students", etudiant("stagiaire@example.com"), "idEtudiant");
        String stage = """
                {"entreprise":"Atlas Tech","sujet":"API REST","dateDebut":"2026-02-01",
                 "dateFin":"2026-06-30","idEtudiant":%d}""".formatted(id);

        long idStage = creer("/api/stages", stage, "idStage");

        mockMvc.perform(get("/api/students/" + id + "/stages"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nomEtudiant").value("Youssef Alaoui"));

        // L'étudiant reste protégé tant qu'un stage le référence
        mockMvc.perform(delete("/api/students/" + id)).andExpect(status().isConflict());
        mockMvc.perform(delete("/api/stages/" + idStage)).andExpect(status().isNoContent());
        mockMvc.perform(delete("/api/students/" + id)).andExpect(status().isNoContent());
    }

    @Test
    void unStageIncoherentEstRefuse() throws Exception {
        long id = creer("/api/students", etudiant("dates@example.com"), "idEtudiant");

        envoyerPost("/api/stages", """
                {"entreprise":"Atlas","sujet":"API","dateDebut":"2026-06-30","dateFin":"2026-02-01","idEtudiant":%d}"""
                .formatted(id))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("La date de fin ne peut pas précéder la date de début"));
        envoyerPost("/api/stages", """
                {"entreprise":"Atlas","sujet":"API","dateDebut":"2026-02-01","dateFin":"2026-06-30","idEtudiant":999999}""")
                .andExpect(status().isNotFound());
        mockMvc.perform(get("/api/students/999999/stages")).andExpect(status().isNotFound());
    }

    @Test
    void swaggerDecritToutesLesRoutes() throws Exception {
        mockMvc.perform(get("/v3/api-docs")).andExpect(status().isOk())
                .andExpect(jsonPath("$.paths['/api/students/{id}']").exists())
                .andExpect(jsonPath("$.paths['/api/departements']").exists())
                .andExpect(jsonPath("$.paths['/api/stages']").exists());
    }
}
