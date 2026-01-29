package excepciones.usuario;

public class AnuncioRequeridoException extends RuntimeException {
    public AnuncioRequeridoException(String message) {
        super(message);
    }
}
