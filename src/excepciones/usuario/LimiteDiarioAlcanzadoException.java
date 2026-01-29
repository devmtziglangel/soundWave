package excepciones.usuario;

public class LimiteDiarioAlcanzadoException extends RuntimeException {
    public LimiteDiarioAlcanzadoException(String message) {
        super(message);
    }
}
