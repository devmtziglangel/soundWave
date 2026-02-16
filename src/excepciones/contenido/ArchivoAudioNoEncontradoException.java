package excepciones.contenido;

public class ArchivoAudioNoEncontradoException extends RuntimeException {

    public ArchivoAudioNoEncontradoException() {
    }



    public ArchivoAudioNoEncontradoException(String message) {
        super(message);
    }
}
