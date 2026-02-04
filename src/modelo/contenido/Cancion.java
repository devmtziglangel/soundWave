package modelo.contenido;

import enums.GeneroMusical;
import excepciones.contenido.ArchivoAudioNoEncontradoException;
import excepciones.contenido.ContenidoNoDisponibleException;
import excepciones.contenido.DuracionInvalidaException;
import excepciones.contenido.LetraNoDisponibleException;
import excepciones.descarga.ContenidoYaDescargadoException;
import excepciones.descarga.LimiteDescargasException;
import modelo.artistas.Album;
import modelo.artistas.Artista;

import java.util.UUID;


public class Cancion  extends Contenido {
    //ATRIBUTOS
    private String letra;
    private Artista artista;
    private Album album;
    private GeneroMusical genero;
    private String audioURL;
    private boolean explicit;
    private String ISRC;
    private boolean reproduciendo;
    private boolean pausado;
    private boolean descargado;


    //CONSTRUCTOR A
    public Cancion(String titulo, int duracionSegundos, Artista artista, GeneroMusical genero) throws DuracionInvalidaException {
        super(titulo, duracionSegundos);

        // Asignar obligatorios
        this.artista = artista;
        this.genero = genero;

        // Inicializar por defecto
        this.audioURL = "";
        this.ISRC = generarISRC();
        this.reproduciendo = false;
        this.pausado = false;
        this.descargado = false;
        this.explicit = false;
        this.letra = null;
    }

    // CONSTRUCTOR B
    public Cancion(String titulo, int duracionSegundos, Artista artista, GeneroMusical genero, String letra, boolean explicit) throws DuracionInvalidaException {
        //reusar del anterior constructor
        this(titulo, duracionSegundos, artista, genero);

        // Solo asignamos lo nuevo
        this.letra = letra;
        this.explicit = explicit;
    }


    //GET Y SETTERS

    public String getLetra() {
        return letra;
    }

    public void setLetra( String letra){
        this.letra=letra;
    }

    public Artista getArtista() {
        return artista;
    }

    public void setArtista(Artista artista){
        this.artista = artista;
    }

    public GeneroMusical getGenero() {
        return genero;
    }

    public void setGenero(GeneroMusical genero) {
        this.genero = genero;
    }

    public String getAudioURL() {
        return audioURL;
    }

    public void setAudioURL(String audioURL) {
        this.audioURL = audioURL;
    }

    public boolean isExplicit() {
        return explicit;
    }

    public void setExplicit(boolean explicit) {
        this.explicit = explicit;
    }

    public String getISRC() {
        return ISRC;
    }


    public boolean isReproduciendo(){
        return this.reproduciendo;
    }

    public boolean isPausado(){
        return this.pausado;
    }

    public boolean isDescargado() {
        return descargado;
    }

    public void setDescargado(boolean descargado) {
        this.descargado = descargado;
    }


    //METODOS PROPIOS

    public String obtenerLetra() throws LetraNoDisponibleException {
        if(letra == null || letra.isEmpty()) throw new LetraNoDisponibleException("La letra de la cancion no esta disponible");

        return this.letra;
    }

    public boolean esExplicit(){
        return this.explicit;
    }

    public void CambiarGenero(GeneroMusical nuevoGenero){
        this.genero = nuevoGenero;

    }



    public void validarAudioURL() throws ArchivoAudioNoEncontradoException {
        if (audioURL == null || audioURL.isEmpty()) {
            throw new ArchivoAudioNoEncontradoException("Archivo de audio no encontrado");
        }
    }

    //METODO PRIVADO

    private String generarISRC() {
        // Generamos un código único aleatorio
        return "ES-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }



    //OVERRIDE DE CONTENIDO
    @Override
    public void reproducir() throws ContenidoNoDisponibleException {
        if(!isDisponible()) throw new ContenidoNoDisponibleException("El contenido no esta disponible");
        play();
        aumentarReproducciones();
    }

    //OVERRIDE DE  IDESCARGABLE
    @Override
    public boolean descargar() throws LimiteDescargasException, ContenidoYaDescargadoException {
        if (this.descargado == true){
            throw  new ContenidoYaDescargadoException("La cancion ya esta descargada");
        }
        this.descargado = true;
        System.out.println("Descargando cancion...." +getTitulo());
        return true;
    }

    @Override
    public boolean eliminarDescarga() {
        if(this.descargado){
            this.descargado=false;
            System.out.println("Eliminando cancion " + getTitulo() + " de descargas");
            return true;

        }
        return false;
    }

    @Override
    public int espacioRequerido() {
        return (duracionSegundos/60) +1;
    }


    //OVERRIDE DE IREPRODUCIBLE
    @Override
    public void play() {
        this.reproduciendo= true;
        this.pausado = false;

        System.out.println("Reproduciendo cancion  " + getTitulo() + " de "+ this.artista.toString());
    }

    @Override
    public void pause() {
        this.reproduciendo= false;
        this.pausado = true;

        System.out.println("Pausada cancion de " + getTitulo());
    }

    @Override
    public void stop() {
        this.reproduciendo = false;
        this.pausado = false;

        System.out.println("Reproduciendo detenida...");
    }

    @Override
    public int getDuracion() {
        return getDuracionSegundos();
    }
}

