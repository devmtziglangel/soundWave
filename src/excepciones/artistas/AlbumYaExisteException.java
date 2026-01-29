package excepciones.artistas;

public class AlbumYaExisteException extends RuntimeException {
    public AlbumYaExisteException(String message) {
        super(message);
    }
}
