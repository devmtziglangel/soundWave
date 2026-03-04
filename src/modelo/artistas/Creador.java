package modelo.artistas;

import enums.CategoriaPodcast;
import excepciones.artista.LimiteEpisodiosException;
import excepciones.contenido.EpisodioNoEncontradoException;
import modelo.contenido.Podcast;
import utilidades.EstadisticasCreador;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map; // <--- NUEVO IMPORT
import java.util.Objects;
import java.util.UUID;

public class Creador {
    private String id;
    private String nombreCanal;
    private String nombre;
    private ArrayList<Podcast> episodios;
    private int suscriptores;
    private String descripcion;
    private HashMap<String, String> redesSociales;
    private ArrayList<CategoriaPodcast> categoriasPrincipales;


    private final static int MAX_EPISODIOS = 100; // Límite de episodios por creador

    //---------- CONSTRUCTORES -----------------

    public Creador (String nombreCanal, String nombre){

        this.id= UUID.randomUUID().toString(); // Generamos un ID único

        //ATRIBUTOS PARAMETRIZADOS
        this.nombreCanal = nombreCanal;
        this.nombre = nombre;

        //ATRIBUTOS POR DEFECTO
        this.episodios = new ArrayList<>();
        this.suscriptores = 0;
        this.descripcion = "";
        this.redesSociales = new HashMap<>();
        this.categoriasPrincipales = new ArrayList<>();
    }




    public Creador (String nombreCanal, String nombre, String descripcion){
        this(nombreCanal, nombre);

        this.descripcion=descripcion;

    }

    // Getters And Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombreCanal() {
        return nombreCanal;
    }

    public void setNombreCanal(String nombreCanal) {
        this.nombreCanal = nombreCanal;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ArrayList<Podcast> getEpisodios() {
        return new ArrayList<>(episodios);
    }

    public int getSuscriptores() {
        return suscriptores;
    }

    public void setSuscriptores(int suscriptores) {
        this.suscriptores = suscriptores;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public HashMap<String, String> getRedesSociales() {
        return new HashMap<>(redesSociales);
    }

    public ArrayList<CategoriaPodcast> getCategoriasPrincipales() {
        return new ArrayList<>(categoriasPrincipales);
    }

    public int getNumEpisodios() {
        return episodios.size();
    }


    //---------- MÉTODOS PROPIOS-----------------


    //    /**
    //     * Publica un nuevo episodio de podcast en el canal.
    //     * @param episodio Episodio a publicar.
    //     * @throws LimiteEpisodiosException Si se ha alcanzado el límite de episodios.
    //     */

    public void publicarEpisodio(Podcast episodio) throws LimiteEpisodiosException {
        if (getNumEpisodios() >= MAX_EPISODIOS) {
            throw new LimiteEpisodiosException("No se pueden publicar más de " + MAX_EPISODIOS + " episodios.");
        }
        episodios.add(episodio);
    }

    /**
     * Publica un nuevo podcast en el canal.
     * @param podcast Podcast a publicar.
     * @throws LimiteEpisodiosException Si se ha alcanzado el límite de episodios.
     */
    public void publicarPodcast(Podcast podcast) throws LimiteEpisodiosException {
        publicarEpisodio(podcast);
    }

    /**
     * Genera un reporte de estadísticas del creador.
     * @return Objeto con las estadísticas calculadas.
     */

    public EstadisticasCreador obtenerEstadisticas() {
        return new EstadisticasCreador(this);


    }


    /**
     * Añade o actualiza una red social del creador.
     * @param red Nombre de la red social (ej: "twitter").
     * @param usuario Nombre de usuario en dicha red.
     */

    public void agregarRedSocial (String red, String usuario){

        redesSociales.put(red.toLowerCase(), usuario);

    }
    /**
     * Calcula el promedio de reproducciones por episodio.
     * @return Promedio de reproducciones.
     */

    //usar el metodo getTotalReproducciones() para obtener el total de reproducciones y dividirlo por el número de episodios para obtener el promedio.

    public double calcularPromedioReproducciones(){

        return (double) getTotalReproducciones() / episodios.size();

    }

    public int getTotalReproducciones () {
        int total = 0;
        for (Podcast e : episodios) {
            total += e.getReproducciones();
        }
        return total;
    }


    /**
     * Elimina un episodio basado en su ID.
     * @param idEpisodio ID del episodio a borrar.
     * @throws EpisodioNoEncontradoException Si no se encuentra el episodio.
     */
    public void eliminarEpisodio(String idEpisodio) throws EpisodioNoEncontradoException{

        for(Podcast e : episodios){
            if(e.getId().equals(idEpisodio)){
                episodios.remove(e);
                return;
            }
        }

        throw new EpisodioNoEncontradoException("No se encontró el episodio con ID: " + idEpisodio);

    }
    /**
     * Incrementa en uno el contador de suscriptores.
     */

    public void incrementarSuscriptiores (){

        this.suscriptores++;

    }

    /**
     * Obtiene los episodios más populares del creador.
     * @param cantidad Número de episodios a recuperar.
     * @return Lista de episodios ordenados por reproducciones.
     */

    public ArrayList<Podcast> obtenerTopEpisodios(int cantidad){
        //crear una copia de la lista de episodios para no modificar la original

        ArrayList<Podcast> ordenados = new ArrayList<>(episodios);

        //ordenar la lista por número de reproducciones (de mayor a menor)

        ordenados.sort((c1, c2) -> Integer.compare(c2.getReproducciones(), c1.getReproducciones()));

        //Aray de top episodios a retornar
        ArrayList<Podcast> top = new ArrayList<>();
        int limite = Math.min(cantidad, ordenados.size());
        for (int i = 0; i < limite; i++) {
            top.add(ordenados.get(i));
        }
        return top;
    }

    /**
     * Obtiene el número de la última temporada publicada.
     * @return Número de temporada.
     */

    public int getUltimaTemporada(){
        int maxTemporada = 0 ;

        for(Podcast e : episodios){
            if(e.getTemporada() > maxTemporada){
                maxTemporada = e.getTemporada();
            }
        }

        return maxTemporada;



    }

    //---------- MÉTODOS OVERRIDE-----------------
    @Override
    public String toString() {
        return "Creador: " + nombreCanal + " (" + nombre + ") | Suscriptores: " + suscriptores + " | Episodios: " + getNumEpisodios();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Creador otro = (Creador) obj;
        return Objects.equals(this.id, otro.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}