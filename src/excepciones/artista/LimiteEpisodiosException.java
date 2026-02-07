package excepciones.artista;

public class LimiteEpisodiosException extends RuntimeException {
    public LimiteEpisodiosException(String message) {
        super(message);
    }
}
