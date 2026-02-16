package excepciones.recomendacion;

public class RecomendacionException extends RuntimeException {

        public RecomendacionException() {
        }
    public RecomendacionException(String message) {
        super(message);
    }
}
