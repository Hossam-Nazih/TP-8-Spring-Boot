package ma.fst.studentapi;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Point de départ de l'API : configure Spring, analyse les sous-packages et lance Tomcat.
 */
@SpringBootApplication
@OpenAPIDefinition(info = @Info(
        title = "Student API",
        version = "1.0",
        description = "Gestion des étudiants, de leurs départements et de leurs stages"))
public class StudentApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudentApiApplication.class, args);
    }
}
