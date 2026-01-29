package excepciones.recomendacion;

public class ModeloNoEntrenadoException extends RuntimeException {
    public ModeloNoEntrenadoException(String message) {
        super(message);
    }
}
