package tn.enicarthage.speedenicar_projet.common.exception;

/** Token JWT invalide, falsifié, expiré ou d'un mauvais type → HTTP 401. */
public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String message) {
        super(message);
    }
}
