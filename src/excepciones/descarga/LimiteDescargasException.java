package excepciones.descarga;

public class LimiteDescargasException extends RuntimeException {

        public LimiteDescargasException() {
        }
    public LimiteDescargasException(String message) {
        super(message);
    }
}
