package excepciones.playlist;

public class CancionNoEncontradaException extends RuntimeException {

        public CancionNoEncontradaException() {
        }
    public CancionNoEncontradaException(String message) {
        super(message);
    }
}
