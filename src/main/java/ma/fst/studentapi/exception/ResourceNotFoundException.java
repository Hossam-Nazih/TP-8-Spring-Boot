package ma.fst.studentapi.exception;

/** La ressource demandée n'existe pas : se traduit par une réponse 404. */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
