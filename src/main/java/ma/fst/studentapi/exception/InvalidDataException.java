package ma.fst.studentapi.exception;

/** Une règle portant sur plusieurs champs n'est pas respectée : se traduit par une réponse 400. */
public class InvalidDataException extends RuntimeException {

    public InvalidDataException(String message) {
        super(message);
    }
}
