package excepciones.plataforma;

public class UsuarioYaExisteException extends RuntimeException {

        public UsuarioYaExisteException() {
        }
    public UsuarioYaExisteException(String message) {
        super(message);
    }
}
