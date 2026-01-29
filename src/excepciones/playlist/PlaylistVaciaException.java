package excepciones.playlist;

public class PlaylistVaciaException extends RuntimeException {
    public PlaylistVaciaException(String message) {
        super(message);
    }
}
