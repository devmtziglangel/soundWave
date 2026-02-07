package modelo.artistas;

import enums.CategoriaPodcast;
import excepciones.artista.LimiteEpisodiosException;
import excepciones.contenido.EpisodioNoEncontradoException;
import excepciones.descarga.LimiteDescargasException;
import modelo.contenido.Podcast;
import utilidades.EstadisticasCreador;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class Creador {
    private String id;
    private String nombreCanal;
    private  String nombre;
    private ArrayList<Podcast> episodios;
    private int suscriptores;
    private String descripcion;
    private HashMap<String, String>redesSociales;
    private ArrayList<CategoriaPodcast> categoriasPrincipales;

    private  static final int MAX_EPISODIOS=500;


    //CONSTURCTOR A
    public Creador(String nombreCanal, String nombre) {
        //GENERAR ID
        this.id = UUID.randomUUID().toString();

        //VALORES PARAMETIZADOS
        this.nombreCanal = nombreCanal;
        this.nombre = nombre;

        //ATRIBUTOS
        this.episodios = new ArrayList<>();
        this.suscriptores = 0;
        this.descripcion=null;
        this.redesSociales = new HashMap<>();
        this.categoriasPrincipales = new ArrayList<>();

    }
    //CONSTRUCTOR B
    public Creador(String nombreCanal, String nombre, String descripcion) {
        this(nombreCanal, nombre);
        this.descripcion = descripcion;

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
        return episodios;
    }

    public void setEpisodios(ArrayList<Podcast> episodios) {
        this.episodios = episodios;
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
        return redesSociales;
    }

    public void setRedesSociales(HashMap<String, String> redesSociales) {
        this.redesSociales = redesSociales;
    }

    public ArrayList<CategoriaPodcast> getCategoriasPrincipales() {
        return categoriasPrincipales;
    }

    public void setCategoriasPrincipales(ArrayList<CategoriaPodcast> categoriasPrincipales) {
        this.categoriasPrincipales = categoriasPrincipales;
    }

    //MEOTODOS

    public void publicarPodcast(Podcast episodio) throws LimiteEpisodiosException {
        if(episodios.size() == MAX_EPISODIOS) throw  new LimiteEpisodiosException("No entran mas episodios");

        episodios.add(episodio);

    }
    public EstadisticasCreador obtenerEstadisticas(){


        return new EstadisticasCreador(this);
    }

    public void agregarRedSocial(String red, String usuario){
        if(redesSociales.containsKey(red) && redesSociales.containsValue(usuario)){
             throw new IllegalArgumentException("Ya existen");
        }
        redesSociales.put(red, usuario);

    }

    public double calcularPromedioReproducciones(){
        double total = 0;

        for(Podcast p: episodios){
            total += p.getReproducciones();
        }

        if(episodios.isEmpty()){
            System.out.println("No hay episodios");

            return 0;
        }

        return total/episodios.size();

    }
    public void eliminarEpisodio(String idEpisodio) throws EpisodioNoEncontradoException{
        //Buscamos cada uno de los episodios del podcast por su ID
        for(Podcast p : episodios){

            if(p.getId().equals(idEpisodio)){ //comparamos con equals porque son objetos diferentes con la misma información
                episodios.remove(p); //removemos el objeto episodio
                return;

            }
        }
        throw new EpisodioNoEncontradoException("No se encontro el episodio");



    }

    public int getTotalReproducciones(){
        int total = 0;
        for(Podcast c : episodios){
            total+= c.getReproducciones();
        }
        return total;
    }

    public void incrementarSuscriptores(){
        this.suscriptores++;
    }

    public ArrayList<Podcast> obtenerTopEpisodios(int cantidad){

        //Crear copia (ArrayList) a partir de los episoddios, esa copia se llama "ordenadas" (la que manipulemos asi la orginal queda intacta)
        ArrayList<Podcast> ordenadas = new ArrayList<>(this.episodios);

        //ordenamos de mayor a menor
        ordenadas.sort((c1, c2) -> c2.getReproducciones() - c1.getReproducciones());

        //Ahora crear el Array "top" vacio para ir poblandolo
        ArrayList<Podcast>top = new ArrayList<>();
        int limite = Math.min(cantidad, ordenadas.size());

        for(int i = 0; i<limite;i++){
            top.add(ordenadas.get(i));
        }

        return top;



    }

    public int getUltimaTemporada(){
        int maxTemp = 0;

        for(Podcast p : episodios){
            if(p.getTemporada() > maxTemp){
                maxTemp=p.getTemporada();

            }
        }
        return maxTemp;

    }

    //METODOS OVERRIDES
    @Override
    public String toString(){
        return  "Creador " + nombre + ", suscriptores :  " + suscriptores+ " , episodios totales: " + episodios;
    }

    @Override
    public boolean equals (Object obj){
        //Si son el mismo objeto en memoria
        if(this == obj) return true;
        // Se compara con NADA, por tanto, como existe es F // NO Somos la misma especie =  F
        if(obj == null || getClass() != obj.getClass()) return false;

        //Casteamos "obj" como X, lo guardamos en un variable de tipo X llamado "otro
        //Despues compramos el this(YO MISMO).id con el objeto que viene de fuera ("otro") para ver si son iguales
        Creador otro = (Creador) obj;

        return this.id.equals(otro.id);
    }

    @Override
    public int hashCode(){
        return Objects.hash(nombre, suscriptores);
    }



}
