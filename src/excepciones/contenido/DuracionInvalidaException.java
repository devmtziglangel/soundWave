package excepciones.contenido;

public class DuracionInvalidaException extends RuntimeException {

    public DuracionInvalidaException() {
    }


    public DuracionInvalidaException(String message) {
        super(message);
    }
}
