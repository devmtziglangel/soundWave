package modelo.artistas;

import excepciones.artistas.AlbumYaExisteException;
import excepciones.artistas.ArtistaNoVerificadoException;
import modelo.contenido.Cancion;

import java.util.ArrayList;
import java.util.Date;

public class Artista {
    //ATRIBUTOS

    private String id;
    private String nombreArtistico;
    private String nombreReal;
    private String paisOrigen;
    private ArrayList<Cancion>discografia;
    private ArrayList<Album>albumes;
    private int oyentesMensuales;
    private boolean verificado;
    private String biografia;

    //CONSTRUCTOR a
    public Artista(String nombreArtistico, String nombreReal, String paisOrigen) {
        this.nombreArtistico = nombreArtistico;
        this.nombreReal = nombreReal;
        this.paisOrigen = paisOrigen;

        this.discografia=new ArrayList<>();
        this.albumes=new ArrayList<>();
        this.oyentesMensuales=0;
        this.verificado=true;
        this.biografia="";
    }

    //CONSTRUCOTR b

    public Artista(String nombreArtistico, String nombreReal, String paisOrigen, boolean verificado, String biografia) {
        this(nombreArtistico, nombreReal,paisOrigen );

        this.verificado = true;
        this.biografia = null;

    }

    //GET Y SETTERS


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombreArtistico() {
        return nombreArtistico;
    }

    public void setNombreArtistico(String nombreArtistico) {
        this.nombreArtistico = nombreArtistico;
    }

    public String getNombreReal() {
        return nombreReal;
    }

    public void setNombreReal(String nombreReal) {
        this.nombreReal = nombreReal;
    }

    public String getPaisOrigen() {
        return paisOrigen;
    }

    public void setPaisOrigen(String paisOrigen) {
        this.paisOrigen = paisOrigen;
    }

    public ArrayList<Cancion> getDiscografia() {
        return discografia;
    }

    public void setDiscografia(ArrayList<Cancion> discografia) {
        this.discografia = discografia;
    }

    public ArrayList<Album> getAlbumes() {
        return albumes;
    }

    public void setAlbumes(ArrayList<Album> albumes) {
        this.albumes = albumes;
    }

    public int getOyentesMensuales() {
        return oyentesMensuales;
    }

    public void setOyentesMensuales(int oyentesMensuales) {
        this.oyentesMensuales = oyentesMensuales;
    }

    public boolean isVerificado() {
        return verificado;
    }

    public void setVerificado(boolean verificado) {
        this.verificado = verificado;
    }

    public String getBiografia() {
        return biografia;
    }

    public void setBiografia(String biografia) {
        this.biografia = biografia;
    }

    //METODOS

    public void publicarCancion(Cancion cancion){
        if(!discografia.contains(cancion)){
            discografia.add(cancion);
        }
    }
    //----------------------------------------------------------
    //VER BIEN ESTOS  METODOS
    //----------------------------------------------
    public Album crearAlbum(String titulo, Date fecha) throws ArtistaNoVerificadoException, AlbumYaExisteException{
        if(!verificado){
            throw new ArtistaNoVerificadoException("El artista " + getNombreArtistico()+" no esta verificado");

        }
        for (Album a : albumes){
            if(a.getTitulo().equalsIgnoreCase(titulo)){
                throw new AlbumYaExisteException("El album "+getAlbumes()+" ya esxiste");
            }

        }

        this.albumes.add(new Album(titulo, fecha));

        return titulo;
    }

    public ArrayList<Cancion>obtenerTopCanciones(int cantidad){
        ArrayList<Cancion> ordenadas = new ArrayList<>(discografia);

        ordenadas.sort((c1, c2) -> c2.getReproducciones() - c1.getReproducciones());

        ArrayList<Cancion> top = new ArrayList<>();
        int limite = Math.min(cantidad, ordenadas.size());

        for(int i = 0; i<limite;i++){
            top.add(ordenadas.get(i));
        }

        return top;

    }

    public double calcularPromedioReproducciones(){

        double total = 0;
        for (Cancion c : discografia){
            total += c.getReproducciones();

        }

        if(discografia.isEmpty()){
            return 0;

        }

        return total/discografia.size();






    }

    public boolean esVerificado(){

    }

    public int getTotalReproducciones(){

    }

    public int getTotalReproducciones(){

    }

    public void verificar(){}

    public void incrementar(){}










}
