package excepciones.playlist;

public class PlaylistLlenaException extends RuntimeException {

        public PlaylistLlenaException() {
        }
    public PlaylistLlenaException(String message) {
        super(message);
    }
}
