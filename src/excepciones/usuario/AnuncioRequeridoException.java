package excepciones.usuario;

public class AnuncioRequeridoException extends RuntimeException {

        public AnuncioRequeridoException() {
        }
    public AnuncioRequeridoException(String message) {
        super(message);
    }
}
