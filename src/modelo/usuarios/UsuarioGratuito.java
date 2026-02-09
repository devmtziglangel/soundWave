package modelo.usuarios;


import enums.TipoSuscripcion;
import excepciones.contenido.ContenidoNoDisponibleException;
import excepciones.usuario.AnuncioRequeridoException;
import excepciones.usuario.EmailInvalidoException;
import excepciones.usuario.LimiteDiarioAlcanzadoException;
import excepciones.usuario.PasswordDebilException;
import modelo.contenido.Contenido;

import java.util.Date;

public class UsuarioGratuito  extends Usuario  {
    //ATRIBUTOS
    private int anunciosEscuchados;
    private Date ultimoAnuncio;
    private int reproduccionesHoy;
    private int limiteReproducciones;
    private int cancionesSinAnuncion;
    private Date fechaUltimaReproduccion;

    private final static int LIMITE_DIARIO = 50;
    private final static int CANCIONES_ENTRE_ANUNCIOS = 3;


    //CONSTRUCTOR
    public UsuarioGratuito(String nombre, String email, String password) throws EmailInvalidoException, PasswordDebilException {
        super(nombre, email, password, TipoSuscripcion.GRATUITO);
        this.anunciosEscuchados = 0;
        this.ultimoAnuncio = new Date();
        this.reproduccionesHoy = 0;
        this.limiteReproducciones = LIMITE_DIARIO;
        this.cancionesSinAnuncion = 0;
        this.fechaUltimaReproduccion = new Date();

    }




    //METODO DEL
    @Override
    public void reproducir(Contenido contenido) throws ContenidoNoDisponibleException, LimiteDiarioAlcanzadoException, AnuncioRequeridoException {


       if(reproduccionesHoy>= LIMITE_DIARIO){

        throw new LimiteDiarioAlcanzadoException("Limite alcanzado");

       }

       if(reproduccionesHoy == 3){
           throw  new AnuncioRequeridoException("Anuncio requerido");
       }

       if(!contenido.isDisponible()){
           throw new ContenidoNoDisponibleException("El contenido no esta disponible");
       }



    }
}