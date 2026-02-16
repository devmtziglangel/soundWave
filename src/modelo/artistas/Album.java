package modelo.artistas;


import enums.GeneroMusical;
import excepciones.artista.AlbumCompletoException;
import excepciones.contenido.DuracionInvalidaException;
import excepciones.playlist.CancionNoEncontradaException;
import modelo.contenido.Cancion;

import java.util.*;

public class Album {

    //ATRIBUTOS

    private String id;                      // ID único generado automáticamente
    private String titulo;                   // Título del álbum
    private Artista artista;                // Artista o banda que publica el álbum
    private Date fechalanzamiento;          // Fecha de lanzamiento del álbum
    private ArrayList<Cancion> canciones;   // Lista de canciones del álbum (máximo 20)
    private String portadaURL;              // URL de la portada del álbum
    private String discografica;            // Discográfica que publica el álbum
    private String tipoAlbum;              // Tipo de álbum (ej. "Álbum de estudio", "EP", "Single")


    private static final int MAX_CANCIONES = 20; // Constante para el límite de canciones por álbum

    //CONSTRUCTOR solo con los datos básicos

    public Album (String titulo, Artista artista, Date fechalanzamiento){
        this.id = UUID.randomUUID().toString(); // Genera un ID único automáticamente

        // Validación de parámetros
        this.titulo = titulo;
        this.artista = artista;
        this.fechalanzamiento = fechalanzamiento;

        // Inicialización de atributos no parametrizados
        this.canciones = new ArrayList<>();
        this.portadaURL = null;
        this.discografica = null;
        this.tipoAlbum = null;
    }


    //constructor con todos los atributo

    public Album(String titulo, Artista artista, Date fechalanzamiento, String discografia, String tipoAlbum) {

        //usar los atributos básicos parametizados del constructor anterior para evitar duplicación de código
        this(titulo, artista, fechalanzamiento);

        //valores parametizados adicionales
        this.discografica = discografia;
        this.tipoAlbum = tipoAlbum;
    }


// GETTERS Y SETTERS----------------------------------------------------------

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Artista getArtista() {
        return artista;
    }

    public void setArtista(Artista artista) {
        this.artista = artista;
    }


    public Date getFechaLanzamiento() {
        return fechalanzamiento;
    }

    public void setFechaLanzamiento(Date fechalanzamiento) {
        this.fechalanzamiento = fechalanzamiento;
    }

    //  copia defensiva
    public ArrayList<Cancion> getCanciones() {
        return new ArrayList<>(canciones);
    }

    //  NO PONGO  setCanciones PARA RESPETAR LA COMPOSICIÓN Y EL LÍMITE DE 20

    public int getMaxCanciones() {
        return MAX_CANCIONES;
    }

    public String getPortadaURL() {
        return portadaURL;
    }

    public void setPortadaURL(String portadaURL) {
        this.portadaURL = portadaURL;
    }

    public String getDiscografica() {
        return discografica;
    }

    public void setDiscografica(String discografica) {
        this.discografica = discografica;
    }

    public String getTipoAlbum() {
        return tipoAlbum;
    }

    public void setTipoAlbum(String tipoAlbum) {
        this.tipoAlbum = tipoAlbum;
    }




    //------------METODOS COMPOSICION COMPOSICION / CREACION------------------



    /**
     * Crea y añade una nueva canción al álbum.
     * @param titulo Título de la canción.
     * @param duracionSegundos Duración en segundos.
     * @param genero Género musical.
     * @param letra
     * @param explicit
     *

     * @throws AlbumCompletoException Si el álbum ya alcanzó el máximo de canciones.
     * @throws DuracionInvalidaException Si la duración es inválida.
     */


    public Cancion crearCancion (String titulo, int duracionSegundos, GeneroMusical genero, String letra, boolean explicit) throws AlbumCompletoException, DuracionInvalidaException {

        if (canciones.size() >= MAX_CANCIONES) {
            throw new AlbumCompletoException("El álbum '" + this.titulo + "' ya tiene el máximo de canciones (" + MAX_CANCIONES + "). No se pueden añadir más canciones.");
        }

        if (duracionSegundos <= 0) {
            throw new DuracionInvalidaException("Duración debe ser un número positivo.");
        }


        // llamamos al constructor de la clase Cancion el completo
        Cancion nuevaCancion = new Cancion(titulo, duracionSegundos, artista, genero, letra, explicit);


        nuevaCancion.setAlbum(this); // establecemos la relación de composición entre la canción y el álbum
        canciones.add(nuevaCancion);
        return nuevaCancion;

    }

    /**
     * Crea y añade una nueva canción al álbum.
     * @param titulo Título de la canción.
     * @param duracionSegundos Duración en segundos.
     * @param genero Género musical.
     *

     * @throws AlbumCompletoException Si el álbum ya alcanzó el máximo de canciones.
     * @throws DuracionInvalidaException Si la duración es inválida.
     */

    public Cancion crearCancion (String titulo, int duracionSegundos, GeneroMusical genero) throws AlbumCompletoException, DuracionInvalidaException{

        return crearCancion(titulo, duracionSegundos, genero, null, false);

//
//
//        LO PODEMOS OBVIAS PORQUE EL CONSTRUCTOR DE LA CLASE CANCIÓN QUE VAMOS A LLAMAR, YA TIENE LAS VALIDACIONES DE DURACIÓN, Y EL LÍMITE DE CANCIONES LO TENEMOS QUE CONTROLAR EN ESTE MÉTODO PARA RESPETAR LA              COMPOSICIÓN Y NO PERMITIR CREAR MÁS DE 20 CANCIONES EN EL ÁLBUM
//
//        if(canciones.size()>=MAX_CANCIONES){
//            throw  new AlbumCompletoException("El álbum '" + this.titulo + "' ya tiene el máximo de canciones (" + MAX_CANCIONES + "). No se pueden añadir más canciones.");
//        }
//
//        if(duracionSegundos<=0){
//            throw new DuracionInvalidaException("Duración debe ser un número positivo.");
//        }
//
//
//        // llamamos al constructor de la clase Cancion el basico
//        Cancion nuevaCancion = new Cancion(titulo, duracionSegundos, artista, genero);
//
//
//        nuevaCancion.setAlbum(this); // establecemos la relación de composición entre la canción y el álbum
//        canciones.add(nuevaCancion);
//        return nuevaCancion;



    }



    //------------METODOS DE GESTION------------------

    /**
     * Elimina una canción del álbum por su posición.
     * @param posicion Posición en la lista (1-based).
     * @throws CancionNoEncontradaException Si la posición no es válida.
     */

    public void eliminarCancion (int posicion) throws CancionNoEncontradaException{
        if(posicion > canciones.size() || posicion<=0){
            throw new CancionNoEncontradaException("Posición " + posicion + " no válida. El álbum '" + this.titulo + "' tiene " + canciones.size() + " canciones.");
        }

        canciones.remove(posicion-1); // restamos 1 para convertir a índice 0-based
    }

    /**
     * Obtiene una canción del álbum por su posición.
     * @param posicion Posición en la lista (1-based).
     * @return La canción en la posición especificada.
     * @throws CancionNoEncontradaException Si la posición no es válida.
     */

    public Cancion getCancion(int posicion) throws CancionNoEncontradaException{
        if(posicion>canciones.size() || posicion<=0){
            throw new CancionNoEncontradaException("Posición " + posicion + " no válida. El álbum '" + this.titulo + "' tiene " + canciones.size() + " canciones.");
        }

        return canciones.get(posicion-1); // restamos 1 para convertir a índice 0-based
    }

    /**
     * Elimina una canción específica del álbum.
     * @param cancion Objeto Cancion a eliminar.
     * @throws CancionNoEncontradaException Si la canción no está en el álbum.
     */

    public void elimminarCancion (Cancion cancion) throws CancionNoEncontradaException{
        if(!canciones.contains(cancion)){
            throw new CancionNoEncontradaException("La canción '" + cancion.getTitulo() + "' no se encuentra en el álbum '" + this.titulo + "'.");
        }

        canciones.remove(cancion);
    }

    /**
     * Calcula la duración total del álbum sumando la duración de todas sus canciones.
     * @return Duración total en segundos.
     */

    public int getDuracionTotal(){
        int duracionTotal = 0;
        for (Cancion c: canciones) {
            duracionTotal += c.getDuracionSegundos();
        }
        return duracionTotal;
    }

    /**
     * Devuelve la duración total formateada en horas:minutos:segundos.
     * @return String con la duración formateada.
     */


    public String getDuracionTotalFormateada() {
        int duracionTotal = getDuracionTotal();
        int horas = duracionTotal / 3600;
        int minutos = (duracionTotal % 3600) / 60;
        int segundos = duracionTotal % 60;

        return String.format("%02d:%02d:%02d", horas, minutos, segundos);
    }

    /**
     * Obtiene el número de canciones del álbum.
     * @return Cantidad de canciones.
     */
    public int getNumCanciones() {
        return canciones.size();
    }

    /**
     * Ordena las canciones del álbum por popularidad (reproducciones) de mayor a menor.
     */

    public void ordenarPorPopularidad(){

        Collections.sort(canciones, (c1, c2) -> c2.getReproducciones() - c1.getReproducciones());

    }

    /**
     * Calcula el total de reproducciones acumuladas de todas las canciones del álbum.
     * @return Total de reproducciones.
     */
    public int getTotalReproducciones() {
        int reproduccionesTotalAlbum = 0;

        for (Cancion cancion : canciones){
            reproduccionesTotalAlbum += cancion.getReproducciones();
        }

        return reproduccionesTotalAlbum;
    }


    //METODOS OVERRIDE

    @Override
    public String toString() {

        return this.titulo + " de " + getArtista();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Album otro = (Album) obj;
        return this.id.equals(otro.id);
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }


}
