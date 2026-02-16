package excepciones.contenido;

public class TranscripcionNoDisponibleException extends RuntimeException {

        public TranscripcionNoDisponibleException() {
        }
    public TranscripcionNoDisponibleException(String message) {
        super(message);
    }
}
