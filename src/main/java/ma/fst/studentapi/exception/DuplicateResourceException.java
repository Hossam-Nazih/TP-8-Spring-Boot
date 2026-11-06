package ma.fst.studentapi.exception;

/** La valeur doit être unique et existe déjà : se traduit par une réponse 409. */
public class DuplicateResourceException extends RuntimeException {

    public DuplicateResourceException(String message) {
        super(message);
    }
}
