package excepciones.plataforma;

public class ContenidoNoEncontradoException extends RuntimeException {

        public ContenidoNoEncontradoException() {
        }
    public ContenidoNoEncontradoException(String message) {
        super(message);
    }
}
