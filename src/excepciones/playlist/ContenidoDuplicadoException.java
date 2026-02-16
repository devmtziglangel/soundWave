package excepciones.playlist;

public class ContenidoDuplicadoException extends RuntimeException {

            public ContenidoDuplicadoException() {
            }
    public ContenidoDuplicadoException(String message) {
        super(message);
    }
}
