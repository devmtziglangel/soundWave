package excepciones.recomendacion;

public class ModeloNoEntrenadoException extends RuntimeException {

    public ModeloNoEntrenadoException() {
    }



    public ModeloNoEntrenadoException(String message) {
        super(message);
    }
}
