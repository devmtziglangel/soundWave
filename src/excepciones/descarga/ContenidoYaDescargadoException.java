package excepciones.descarga;

public class ContenidoYaDescargadoException extends RuntimeException {
    public ContenidoYaDescargadoException(String message) {
        super(message);
    }
}
