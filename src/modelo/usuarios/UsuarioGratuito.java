package modelo.usuarios;


import enums.TipoSuscripcion;
import excepciones.contenido.ContenidoNoDisponibleException;
import excepciones.usuario.AnuncioRequeridoException;
import excepciones.usuario.EmailInvalidoException;
import excepciones.usuario.LimiteDiarioAlcanzadoException;
import excepciones.usuario.PasswordDebilException;
import modelo.contenido.Contenido;
import modelo.plataforma.Anuncio;

import java.util.Date;

public class UsuarioGratuito  extends Usuario  {
    //ATRIBUTOS
    private int anunciosEscuchados;
    private Date ultimoAnuncio;
    private int reproduccionesHoy;
    private int limiteReproducciones;
    private int cancionesSinAnuncio;
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
        this.cancionesSinAnuncio = 0;
        this.fechaUltimaReproduccion = new Date();

    }

    //GET Y SETTERS


    public int getAnunciosEscuchados() {
        return anunciosEscuchados;
    }

    public void setAnunciosEscuchados(int anunciosEscuchados) {
        this.anunciosEscuchados = anunciosEscuchados;
    }

    public Date getUltimoAnuncio() {
        return ultimoAnuncio;
    }

    public void setUltimoAnuncio(Date ultimoAnuncio) {
        this.ultimoAnuncio = ultimoAnuncio;
    }

    public int getReproduccionesHoy() {
        return reproduccionesHoy;
    }

    public void setReproduccionesHoy(int reproduccionesHoy) {
        this.reproduccionesHoy = reproduccionesHoy;
    }

    public int getLimiteReproducciones() {
        return limiteReproducciones;
    }

    public void setLimiteReproducciones(int limiteReproducciones) {
        this.limiteReproducciones = limiteReproducciones;
    }

    public int getCancionesSinAnuncio() {
        return cancionesSinAnuncio;
    }

    public void setCancionesSinAnuncio(int cancionesSinAnuncion) {
        this.cancionesSinAnuncio = cancionesSinAnuncion;
    }

    public Date getFechaUltimaReproduccion() {
        return fechaUltimaReproduccion;
    }

    public void setFechaUltimaReproduccion(Date fechaUltimaReproduccion) {
        this.fechaUltimaReproduccion = fechaUltimaReproduccion;
    }

    //METODOS CONCRETOS

    public void verAnuncio(){

        System.out.println("Publicidad Generica ");

        this.anunciosEscuchados++;
        this.ultimoAnuncio = new Date();
        this.cancionesSinAnuncio = 0; //Reiniciar contador
    }

    public void verAnuncio (Anuncio anuncio){
        if(anuncio == null){
            this.verAnuncio();
        }

        System.out.println("Publicdad especifica " + anuncio.getEmpresa());

        anuncio.reproducir();
        anuncio.registrarImpresion();

        this.anunciosEscuchados++;
        this.ultimoAnuncio = new Date();

        this.cancionesSinAnuncio = 0;
    }

    public boolean puedeReproducir(){
        return reproduccionesHoy < LIMITE_DIARIO && !debeVerAnuncio();

    }

    public boolean debeVerAnuncio(){
        if(cancionesSinAnuncio == CANCIONES_ENTRE_ANUNCIOS) return true;

        return false;
    }

    public void reiniciarContadorDiario (){
        this.reproduccionesHoy=0;
    }



    public int getReproduccionesRestantes(){

        return  this. LIMITE_DIARIO - this.reproduccionesHoy;

    }

    public int getCancionesHastaAnuncio(){
        return CANCIONES_ENTRE_ANUNCIOS - this.cancionesSinAnuncio ;
    }


    //OVERRIDE

    @Override
    public String toString() {
        return super.toString() +
                " [GRATUITO] " +
                "Reproducciones hoy: " + this.reproduccionesHoy +
                " | Anuncios totales: " + this.anunciosEscuchados;
    }


    //METODO OVERRIDE DEL PADRE

    @Override
    public void reproducir(Contenido contenido) throws ContenidoNoDisponibleException, LimiteDiarioAlcanzadoException, AnuncioRequeridoException {


       if(reproduccionesHoy>= LIMITE_DIARIO){

        throw new LimiteDiarioAlcanzadoException("Limite alcanzado");

       }



       if(cancionesSinAnuncio>=CANCIONES_ENTRE_ANUNCIOS){
           throw new AnuncioRequeridoException("Debes ver un anuncio para seguir !!");

       }

       contenido.reproducir();
       agregarAlHistorial(contenido);

       //Actualizar contador de reporducion para que salte cada 3

        this.reproduccionesHoy++;
        this.cancionesSinAnuncio++;

        // el que se encarga de reiniciar el contador es el metodoAnuncio;





    }
}