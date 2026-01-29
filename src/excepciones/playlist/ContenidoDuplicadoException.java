package excepciones.playlist;

public class ContenidoDuplicadoException extends RuntimeException {
    public ContenidoDuplicadoException(String message) {
        super(message);
    }
}
