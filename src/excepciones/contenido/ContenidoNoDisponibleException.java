package excepciones.contenido;

public class ContenidoNoDisponibleException extends RuntimeException {

    public ContenidoNoDisponibleException() {
    }


    public ContenidoNoDisponibleException(String message) {
        super(message);
    }
}
