package modelo.plataforma;

import enums.CriterioOrden;
import excepciones.plataforma.ContenidoNoEncontradoException;
import excepciones.playlist.ContenidoDuplicadoException;
import excepciones.playlist.PlaylistLlenaException;
import excepciones.playlist.PlaylistVaciaException;
import modelo.artistas.Album;
import modelo.contenido.Contenido;
import modelo.usuarios.Usuario;

import java.util.*;

public class Playlist {

    private String id;
    private String nombre;
    private Usuario creador;
    private ArrayList<Contenido> contenidos;
    private boolean esPublica;
    private int seguidores;
    private String descripcion;
    private String portadaURL;
    private Date fechaCreacion;
    private int maxContenidos;

    private static final int MAX_CONTENIDOS_DEFAULT = 500;

    //CONSTRUCTOR A

    public Playlist(String nombre, Usuario creador) {
        this.id = UUID.randomUUID().toString();

        this.nombre = nombre;
        this.creador = creador;

        this.contenidos = new ArrayList<>();
        this.esPublica=false;
        this.seguidores = 0;
        this.descripcion="";
        this.portadaURL=null;
        this.fechaCreacion = new Date();
        this.maxContenidos = MAX_CONTENIDOS_DEFAULT;
    }

    //CONSTRUCTOR B


    public Playlist(String nombre, Usuario creador, boolean esPublica, String descripcion) {
        this(nombre,creador);

        this.esPublica = esPublica;
        this.descripcion = descripcion;
    }

    //GET Y SETTERS


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Usuario getCreador() {
        return creador;
    }

    public void setCreador(Usuario creador) {
        this.creador = creador;
    }

    public ArrayList<Contenido> getContenidos() {
        return new ArrayList<Contenido>(contenidos);
    }

    public void setContenidos(ArrayList<Contenido> contenidos) {
        this.contenidos = contenidos;
    }

    public boolean isEsPublica() {
        return esPublica;
    }

    public void setEsPublica(boolean esPublica) {
        this.esPublica = esPublica;
    }

    public int getSeguidores() {
        return seguidores;
    }

    public void setSeguidores(int seguidores) {
        this.seguidores = seguidores;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPortadaURL() {
        return portadaURL;
    }

    public void setPortadaURL(String portadaURL) {
        this.portadaURL = portadaURL;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public int getMaxContenidos() {
        return maxContenidos;
    }

    public void setMaxContenidos(int maxContenidos) {
        this.maxContenidos = maxContenidos;
    }

    //METODOS PROPIOS

    public void agregarContenido (Contenido contenido) throws PlaylistLlenaException, ContenidoDuplicadoException{
        if (contenidos.size() >= maxContenidos) throw  new PlaylistLlenaException("PlayList llena");
        if(contenidos.contains(contenido)) throw new ContenidoDuplicadoException("El contenido es igual");

        contenidos.add(contenido);

    }

    public boolean eliminarContenido (String idContenido) throws ContenidoNoEncontradoException{

        for (Contenido c : contenidos){
            if(c.getId().equals(idContenido)){
                contenidos.remove(c);
                return true;
            }
        }

        return false;
    }

    public boolean eliminarContenido(Contenido contenido) {
        return contenidos.remove(contenido);
    }

    public void ordenarPor(CriterioOrden criterioOrden) throws PlaylistVaciaException {
        if (contenidos.isEmpty()) throw new PlaylistVaciaException("La playlist está vacía");

        switch (criterioOrden) {
            case ALFABETICO:
                // Para Strings usamos compareTo
                contenidos.sort((c1, c2) -> c1.getTitulo().compareTo(c2.getTitulo()));
                break;

            case DURACION:
                // Para números usamos Integer.compare
                contenidos.sort((c1, c2) -> Integer.compare(c1.getDuracionSegundos(), c2.getDuracionSegundos()));
                break;

            case POPULARIDAD:
                // Ordenar por reproducciones (de mayor a menor suele ser lo normal aquí)
                contenidos.sort((c1, c2) -> Integer.compare(c2.getReproducciones(), c1.getReproducciones()));
                break;

            case FECHA_AGREGADO:
                // Las fechas también tienen compareTo
                contenidos.sort((c1, c2) -> c1.getFechaPublicacion().compareTo(c2.getFechaPublicacion()));
                break;

            case ALEATORIO:
                Collections.shuffle(this.contenidos);
                break;

            default:
                // Si el criterio no está implementado aún
                break;
        }


    }
    public int getDuracionTotal (){
        int total = 0 ;
        for (Contenido c : contenidos){
            total += c.getDuracionSegundos();
        }
        return total;
    }

    public String getDuracionTotalFormateada(){

        int horas = getDuracionTotal() / 3600;
        int minutos  = (getDuracionTotal() % 3600) / 60;
        int segundos = getDuracionTotal() % 60;

        return String.format("%02d:%02d:%02d", horas, minutos, segundos);

    }

    public void shuffle(){
        Collections.shuffle(this.contenidos);
    }

    public ArrayList<Contenido>buscarContenido(String termino){

        //Creamos una Lista Vacia que iremos poblando con los elementos que sean iguales

        ArrayList<Contenido> temp = new ArrayList<>();

        for (Contenido c : contenidos){
            if(c.getTitulo().equalsIgnoreCase(termino)){
                temp.add(c);
            }
        }

        return temp;

    }

    public void hacerPublica (){
        this.esPublica =true;

    }

    public void hacerPrivada(){
        this.esPublica = false;
    }

    public void incrementarSeguidores (){
        this.seguidores++;
    }

    public void decrementarSeguidores () throws IllegalAccessException {
       if(seguidores<=0){
           throw new IllegalAccessException("Ya tiene 0 seguidores, no puede bajar mas");
       }
       this.seguidores--;
    }

    public int getNumContenidos(){

        return contenidos.size();
    }

    public boolean estaVacia(){

        return contenidos.isEmpty();
    }

    public Contenido getContenido (int posicion) throws ContenidoNoEncontradoException{
        if(posicion<0 || posicion>=contenidos.size()){
            throw  new ContenidoNoEncontradoException("Contenido no encontrado");

        }
        return contenidos.get(posicion);

    }

    //METODOS OVERRIDE
    @Override
    public String toString() {
        return "Playlist: " + nombre +
                " | Creador: " + getCreador() +
                " | Cantidad: " + getNumContenidos() +
                " | Tiempo: " + getDuracionTotalFormateada();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Playlist otra = (Playlist) obj; // ¡Cambiado de Album a Playlist!
        return this.id.equals(otra.id);
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }











}
