package excepciones.contenido;

public class EpisodioNoEncontradoException extends RuntimeException {

        public EpisodioNoEncontradoException() {
        }
    public EpisodioNoEncontradoException(String message) {
        super(message);
    }
}
