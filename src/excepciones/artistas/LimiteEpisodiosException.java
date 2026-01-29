package excepciones.artistas;

public class LimiteEpisodiosException extends RuntimeException {
    public LimiteEpisodiosException(String message) {
        super(message);
    }
}
