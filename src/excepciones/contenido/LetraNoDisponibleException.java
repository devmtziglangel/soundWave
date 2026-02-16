package excepciones.contenido;

public class LetraNoDisponibleException extends RuntimeException {

        public LetraNoDisponibleException() {
        }

    public LetraNoDisponibleException(String message) {
        super(message);
    }
}
