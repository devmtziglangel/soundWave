package modelo.usuarios;


import enums.TipoSuscripcion;
import excepciones.contenido.ContenidoNoDisponibleException;
import excepciones.usuario.AnuncioRequeridoException;
import excepciones.usuario.EmailInvalidoException;
import excepciones.usuario.LimiteDiarioAlcanzadoException;
import excepciones.usuario.PasswordDebilException;
import modelo.contenido.Contenido;

import java.util.ArrayList;

public class UsuarioPremium  extends Usuario {
    private boolean descargasOffline;
    private  int maxDescargas;
    private ArrayList<Contenido> descargados;
    private String calidadAudio;

    private static final int MAX_DESCARGAS_DEFAULT = 100;




    //constructor padre
    public UsuarioPremium(String nombre, String email, String password, TipoSuscripcion suscripcion) throws EmailInvalidoException, PasswordDebilException {
        super(nombre, email, password, suscripcion);

        //INICIALIZAR ATRIBUTOS
        this.descargasOffline = suscripcion.isDescargasOffline();
        this.maxDescargas = MAX_DESCARGAS_DEFAULT;
        this.descargados = new ArrayList<>();
        this.calidadAudio = "Alta";


    }

    //CONSTRUCTOR HIJO
    public UsuarioPremium(String nombre, String email, String password) throws EmailInvalidoException, PasswordDebilException {
        // le ponemos los datos que nos pide el ReadMe
        // y añadiendo los del Padre
        this(nombre, email, password, TipoSuscripcion.PREMIUM);
    }



        @Override
    public void reproducir(Contenido contenido) throws ContenidoNoDisponibleException, LimiteDiarioAlcanzadoException, AnuncioRequeridoException {

    }
}
