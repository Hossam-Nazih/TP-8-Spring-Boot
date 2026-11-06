package ma.fst.studentapi.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

/**
 * Transforme les exceptions en réponses JSON au format ProblemDetail.
 * La classe parente garde les bons codes pour les erreurs de Spring : JSON illisible (400),
 * route inconnue (404), méthode HTTP non prise en charge (405)...
 * Sans elle, le gestionnaire générique les transformerait toutes en erreur 500.
 */
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger journal = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private ProblemDetail probleme(HttpStatus statut, String titre, String detail) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(statut);
        problemDetail.setTitle(titre);
        problemDetail.setDetail(detail);
        return problemDetail;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleResourceNotFound(ResourceNotFoundException ex) {
        return probleme(HttpStatus.NOT_FOUND, "Ressource introuvable", ex.getMessage());
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ProblemDetail handleDuplicate(DuplicateResourceException ex) {
        return probleme(HttpStatus.CONFLICT, "Conflit de données", ex.getMessage());
    }

    @ExceptionHandler(InvalidDataException.class)
    public ProblemDetail handleInvalidData(InvalidDataException ex) {
        return probleme(HttpStatus.BAD_REQUEST, "Données invalides", ex.getMessage());
    }

    /** Doublon concurrent, ou suppression d'une ligne encore référencée par une autre table. */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleDataIntegrity(DataIntegrityViolationException ex) {
        return probleme(HttpStatus.CONFLICT, "Conflit de données",
                "Opération refusée : valeur déjà utilisée, ou donnée liée à d'autres enregistrements");
    }

    /** Déclenché par @Valid : le détail donne le premier message, "erreurs" les liste tous. */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatusCode status,
                                                                  WebRequest request) {
        List<String> messages = ex.getBindingResult().getFieldErrors().stream()
                .map(erreur -> erreur.getDefaultMessage())
                .sorted()
                .toList();
        ProblemDetail problemDetail = probleme(HttpStatus.BAD_REQUEST, "Erreur de validation",
                messages.isEmpty() ? "Données invalides" : messages.get(0));
        problemDetail.setProperty("erreurs", messages);
        return ResponseEntity.badRequest().body(problemDetail);
    }

    /** Dernier recours : la cause reste dans les journaux, le client ne voit aucun détail technique. */
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGenericException(Exception ex) {
        journal.error("Erreur non prévue", ex);
        return probleme(HttpStatus.INTERNAL_SERVER_ERROR, "Erreur interne du serveur",
                "Une erreur inattendue est survenue");
    }
}
