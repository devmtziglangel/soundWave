package modelo.artistas;


import enums.GeneroMusical;
import excepciones.artista.AlbumCompletoException;
import excepciones.contenido.DuracionInvalidaException;
import excepciones.playlist.CancionNoEncontradaException;
import modelo.contenido.Cancion;

import java.util.*;

public class Album {
    private String id;
    private String titulo;
    private Artista artista;
    private Date fechalanzamiento;
    private ArrayList<Cancion>canciones;
    private String portadaURL;
    private String discografica;
    private String tipoAlbum;

    private static final int MAX_CANCIONES = 20;

    //CONSTRUCTOR A
    public Album(String titulo, Artista artista, Date fechalanzamiento) {

        //GENERAR ID
        this.id = UUID.randomUUID().toString();

        //ASIGNAR VALORES PARAMETIZADOS
        this.titulo = titulo;
        this.artista = artista;
        this.fechalanzamiento = fechalanzamiento;

       //ATRIBUTOS
        this.canciones = new ArrayList<>(); //Iniciar array

        this.portadaURL = null;
        this.discografica = null;
        this.tipoAlbum = null;
    }

    //CONSTRUCTOR B

    public Album(String titulo, Artista artista, Date fechalanzamiento, String discografica, String tipoAlbum) {
        //USAR LOS PARAMETIZADOS DEL CONSTRUCTOR A
        this( titulo, artista, fechalanzamiento);

        //ASIGNAR VALORES PARAMETIZADOS
        this.discografica = discografica;
        this.tipoAlbum = tipoAlbum;

        //ATRIBUTO
        // this.portadaURL=null;  ¡No hace falta poner portadaURL = null otra vez, el A ya lo hizo!


    }



// GETTERS Y SETTERS

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

    // ¡ copia defensiva!
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

    //METODOS COMPOSICION AGREGACION

    public Cancion crearCancion(String titulo, int duracionSegundos, GeneroMusical genero)
            throws AlbumCompletoException, DuracionInvalidaException { //EL THROW SE HACE EN LA CLASE CANCION

        // 1. Verificamos si hay espacio
        if (canciones.size() >= MAX_CANCIONES) {
            throw new AlbumCompletoException("El álbum está completo. No caben más canciones.");
        }

        // 2. Instanciamos la canción (COMPOSICIÓN)
        // Pasamos 'this.artista' porque la canción pertenece al artista de este álbum.
        // OJO AL ORDEN: Título -> Duración -> Artista -> Género
        Cancion nuevaCancion = new Cancion(titulo, duracionSegundos, this.artista, genero);

        // 3. Establecemos la relación inversa (La canción debe saber que este es su álbum)
        nuevaCancion.setAlbum(this); // --> "YO SOY TU PADRE"

        // 4. La guardamos en la lista del álbum
        canciones.add(nuevaCancion);

        // 5. Devolvemos la canción creada
        return nuevaCancion;
    }

    public Cancion crearCancion(String titulo, int duracionSegundos, GeneroMusical genero, String letra, boolean explicit)
            throws AlbumCompletoException, DuracionInvalidaException {

        // 1. Validar si cabe (Igual que el anterior)
        if (canciones.size() >= MAX_CANCIONES) {
            throw new AlbumCompletoException("El álbum está completo.");
        }

        // 2. Instanciar (COMPOSICIÓN) usando el constructor COMPLETO
        // Fíjate: pasamos 'this.artista' (el padre) + letra + explicit
        Cancion nuevaCancion = new Cancion(titulo, duracionSegundos, this.artista, genero, letra, explicit);

        // 3. Vincular (Igual que el anterior: "Yo soy tu álbum")
        nuevaCancion.setAlbum(this);

        // 4. Guardar y devolver
        canciones.add(nuevaCancion);
        return nuevaCancion;
    }





    //METODOS GESTION

    public void eliminarCancion(int posicion) throws CancionNoEncontradaException{

        if(posicion<=0 || posicion>canciones.size()){
            throw  new CancionNoEncontradaException("Cancion no encontrada");
        }
        canciones.remove(posicion-1);
    }

    public void eliminarCancion (Cancion cancion)throws CancionNoEncontradaException{
        if(!canciones.contains(cancion)){
           throw new CancionNoEncontradaException("Cancion no encontrada");
        }
        canciones.remove(cancion);
    }

    public int getDuracionTotal(){
        int total = 0;
        for(Cancion c : canciones){
           total+= c.getDuracion();

        }
        return total;

    }

    public String getDuracionTotalFormateada(){

        int totalDuracion = getDuracionTotal();
        int min = totalDuracion/60;
        int seg = totalDuracion%60;


        return String.format("%d:%02d", min, seg);

    }

    public int getNumCanciones(){


        return canciones.size();
    }

    public void ordenarPorPopularidad(){


        Collections.sort(canciones, (c1, c2) -> c2.getReproducciones() - c1.getReproducciones());

    }

    public Cancion getCancion(int posicion) throws CancionNoEncontradaException{
        if(posicion<=0 || posicion>canciones.size()){
            throw  new CancionNoEncontradaException("Cancion no encontrada");
        }
        return canciones.get(posicion-1);
    }

    public int getTotalReproducciones() {
        int total = 0;

        for (Cancion c : canciones) {

            total += c.getReproducciones();
        }

        return total;
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
