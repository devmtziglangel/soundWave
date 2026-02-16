package excepciones.usuario;

public class LimiteDiarioAlcanzadoException extends RuntimeException {

        public LimiteDiarioAlcanzadoException() {
        }
    public LimiteDiarioAlcanzadoException(String message) {
        super(message);
    }
}
