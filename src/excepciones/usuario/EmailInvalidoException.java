package excepciones.usuario;

public class EmailInvalidoException extends Exception {

    public EmailInvalidoException() {
    }


    public EmailInvalidoException (String mensaje){

        super (mensaje);
    }

}
