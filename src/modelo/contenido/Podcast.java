package modelo.contenido;

import enums.CategoriaPodcast;
import excepciones.contenido.ContenidoNoDisponibleException;
import excepciones.contenido.DuracionInvalidaException;
import excepciones.contenido.EpisodioNoEncontradoException;
import excepciones.contenido.TranscripcionNoDisponibleException;
import excepciones.descarga.ContenidoYaDescargadoException;
import modelo.artistas.Creador;

import java.util.ArrayList;

public class Podcast  extends  Contenido{

    private Creador creador;
    private  int numeroEpisodio;
    private int temporada;
    private String descripcion;
    private CategoriaPodcast categoria;
    private ArrayList<String> invitados;
    private String transcripcion;

    //estos los hereda de Contenido
    private boolean reproduciendo;
    private boolean pausado;
    private boolean descargado;

    //CONSTRUCTOR A -> padre
    public Podcast(String titulo, int duracionSegundos, Creador creador, int numeroEpisodio, int temporada, CategoriaPodcast categoria) throws DuracionInvalidaException {
        super(titulo, duracionSegundos);

        //Asginacion parametrizados

        this.creador = creador;
        this.numeroEpisodio = numeroEpisodio;
        this.temporada = temporada;
        this.categoria = categoria;

        //Asignacion inicilizar
        this.descripcion = "";
        this.invitados = new ArrayList<>();
        this.transcripcion = null;

        //Asignacion estados --> Contenido

        this.reproduciendo = false;
        this.pausado= false;
        this.descargado = false;


    }

    //CONSTRUCTOR B

    public Podcast(String titulo, int duracionSegundos, Creador creador, int numeroEpisodio, int temporada, CategoriaPodcast categoria, String descripcion) throws DuracionInvalidaException {

        //SIN SUPER y AÑADIR LOS QUE SE REPITEN
        this(titulo, duracionSegundos, creador, numeroEpisodio, temporada, categoria);

        //PONER EL PARAMETRO NUMERO DE ESTE CONSTRUCTOR
        this.descripcion = descripcion;
    }

    //GET Y SETTERS

    public Creador getCreador() {
        return creador;
    }

    public void setCreador(Creador creador) {
        this.creador = creador;
    }

    public int getNumeroEpisodio() {
        return numeroEpisodio;
    }

    public void setNumeroEpisodio(int numeroEpisodio) {
        this.numeroEpisodio = numeroEpisodio;
    }

    public int getTemporada() {
        return temporada;
    }

    public void setTemporada(int temporada) {
        this.temporada = temporada;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public CategoriaPodcast getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaPodcast categoria) {
        this.categoria = categoria;
    }

    public ArrayList<String> getInvitados() {
        return invitados;
    }

    public void setInvitados(ArrayList<String> invitados) {
        this.invitados = invitados;
    }

    public String getTranscripcion() {
        return transcripcion;
    }

    public void setTranscripcion(String transcripcion) {
        this.transcripcion = transcripcion;
    }

    public boolean isReproduciendo() {
        return reproduciendo;
    }

    public void setReproduciendo(boolean reproduciendo) {
        this.reproduciendo = reproduciendo;
    }

    public boolean isPausado() {
        return pausado;
    }

    public void setPausado(boolean pausado) {
        this.pausado = pausado;
    }

    public boolean isDescargado() {
        return descargado;
    }

    public void setDescargado(boolean descargado) {
        this.descargado = descargado;
    }

    //METODOS PROPIOS

    public String obtenerDescripcion()  {
        if(descripcion==null || descripcion.isEmpty()){
            return "La descripcion no esta disponible";

        }
        return this.descripcion;

    }

    public void agregarInvitado(String nombre){
        if(!invitados.contains(nombre)){
            this.invitados.add(nombre);
        }

    }

    public boolean esTemporadaNueva(int nuevaTemporada){

        if (temporada<nuevaTemporada){
            temporada = nuevaTemporada;
            return true;

        }
        return false;
    }

    public String obtenerTranscripcion() throws TranscripcionNoDisponibleException {
        if(transcripcion==null || transcripcion.isEmpty()) throw new TranscripcionNoDisponibleException ("Transcripcion no dispobile");

        return this.transcripcion;

    }

    public void validarEpisodio() throws EpisodioNoEncontradoException{
        if(temporada<=0 || numeroEpisodio<=0) throw new EpisodioNoEncontradoException("No existe el episodio");
    }


    //METODO de --> CONTENIDO
    @Override
    public void reproducir() throws ContenidoNoDisponibleException {
        if(!isDisponible()) throw new ContenidoNoDisponibleException("El podcast actual no esta disponible");
        play();
        aumentarReproducciones();
    }

    @Override
    public boolean descargar() throws  ContenidoYaDescargadoException {
        if(this.descargado){
            throw new ContenidoYaDescargadoException("El contenido "+getTitulo()+" ya esta descargado en tu dispotivo");
        }
        this.descargado = true;
        System.out.println("Descargando " + getTitulo() + "...");
        return true;
    }


    @Override
    public boolean eliminarDescarga() {
        if(this.descargado){
            this.descargado=false;
            System.out.println("Eliminando descarga"+getTitulo()+  "del dispositivo");
        }
        return true;
    }

    @Override
    public int espacioRequerido() {
        return (duracionSegundos/60) +1;
    }


    //METODO de --> REPRODUCIBLE

    @Override
    public void play() {

        this.reproduciendo = true;
        this.pausado = false;

        System.out.println("Reproduciendo podcast: " + getTitulo());
    }

    @Override
    public void pause() {

        this.reproduciendo = false;
        this.pausado = true;

        System.out.println("Podcast pausado: " + getTitulo());
    }

    @Override
    public void stop() {
        //
        this.reproduciendo = false;
        this.pausado = false;

        System.out.println("Podcast detenido: " + getTitulo());
    }

    @Override
    public int getDuracion() {
        // Devolvemos la duración que tiene guardada la clase padre (Contenido)
        return getDuracionSegundos();
    }
}
