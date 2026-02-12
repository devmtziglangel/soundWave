package modelo.plataforma;

import enums.TipoSuscripcion;
import excepciones.plataforma.UsuarioYaExisteException;
import excepciones.usuario.EmailInvalidoException;
import excepciones.usuario.PasswordDebilException;
import interfaces.Recomendador;
import modelo.artistas.Album;
import modelo.artistas.Artista;
import modelo.artistas.Creador;
import modelo.contenido.Contenido;
import modelo.usuarios.Usuario;
import modelo.usuarios.UsuarioGratuito;
import modelo.usuarios.UsuarioPremium;
import utilidades.RecomendadorIA;

import java.util.ArrayList;
import java.util.HashMap;

public class Plataforma {

    private static  Plataforma plataforma;

    private String nombre;
    private HashMap<String, Usuario> usuarios;
    private HashMap<String, Usuario> usuariosPorEmail;
    private ArrayList<Contenido> catalogo;
    private ArrayList<Playlist> playlistsPublicas;
    private HashMap<String, Artista> artistas;
    private HashMap<String, Creador> creadores;
    private ArrayList<Album> albumes;
    private ArrayList<Anuncio> anuncios;
    private Recomendador recomendador;
    private int totalAnunciosReproducidos;


    //CONSTRUCTOR

    // Patrón Singleton --> privado para que nadie pueda hacer "new Plataforma("Otra")"
    private Plataforma(String nombre) {
        this.nombre = nombre;
        this.usuarios = new HashMap<>();
        this.usuariosPorEmail=new HashMap<>();
        this.catalogo = new ArrayList<>();
        this.playlistsPublicas = new ArrayList<>();
        this.artistas = new HashMap<>();
        this.creadores = new HashMap<>();
        this.albumes = new ArrayList<>();
        this.anuncios = new ArrayList<>();
        this.recomendador = new RecomendadorIA();
        this.totalAnunciosReproducidos = 0;
    }

    //GET Y SETTERS


    public ArrayList<Album> getAlbumes() {
        return albumes;
    }

    public void setAlbumes(ArrayList<Album> albumes) {
        this.albumes = albumes;
    }

    public ArrayList<Anuncio> getAnuncios() {
        return anuncios;
    }

    public void setAnuncios(ArrayList<Anuncio> anuncios) {
        this.anuncios = anuncios;
    }

    public HashMap<String, Artista> getArtistas() {
        return artistas;
    }

    public void setArtistas(HashMap<String, Artista> artistas) {
        this.artistas = artistas;
    }

    public ArrayList<Contenido> getCatalogo() {
        return catalogo;
    }

    public void setCatalogo(ArrayList<Contenido> catalogo) {
        this.catalogo = catalogo;
    }

    public HashMap<String, Creador> getCreadores() {
        return creadores;
    }

    public void setCreadores(HashMap<String, Creador> creadores) {
        this.creadores = creadores;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public static Plataforma getPlataforma() {
        return plataforma;
    }

    public static void setPlataforma(Plataforma plataforma) {
        Plataforma.plataforma = plataforma;
    }

    public ArrayList<Playlist> getPlaylistsPublicas() {
        return playlistsPublicas;
    }

    public void setPlaylistsPublicas(ArrayList<Playlist> playlistsPublicas) {
        this.playlistsPublicas = playlistsPublicas;
    }

    public Recomendador getRecomendador() {
        return recomendador;
    }

    public void setRecomendador(Recomendador recomendador) {
        this.recomendador = recomendador;
    }

    public int getTotalAnunciosReproducidos() {
        return totalAnunciosReproducidos;
    }

    public void setTotalAnunciosReproducidos(int totalAnunciosReproducidos) {
        this.totalAnunciosReproducidos = totalAnunciosReproducidos;
    }

    public HashMap<String, Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(HashMap<String, Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public HashMap<String, Usuario> getUsuariosPorEmail() {
        return usuariosPorEmail;
    }

    public void setUsuariosPorEmail(HashMap<String, Usuario> usuariosPorEmail) {
        this.usuariosPorEmail = usuariosPorEmail;
    }


    //METODOS SINGLETON

    public static synchronized Plataforma getInstancia(String nombre){
        if(plataforma == null ){
             new Plataforma(nombre);
        }
        return plataforma;
    }

    public static synchronized Plataforma getInstancia(){

        return getInstancia();
    }

    public static  synchronized void reiniciarInstancia(){
        plataforma = null;
    }

    //GESTION DE USUARIOS

    public UsuarioPremium registrarUsuarioPremium(String nombre, String email, String password, TipoSuscripcion tipo)
            throws UsuarioYaExisteException, EmailInvalidoException, PasswordDebilException { //

        //VALIDAR

        if (usuariosPorEmail.containsKey(nombre)) throw new UsuarioYaExisteException("El usuario ya existe");

        // Crear instancia (El constructor de Usuario valida pass y email regex)

        UsuarioPremium nuevo = new UsuarioPremium(nombre, email, password, tipo);

       //  Guardar en AMBOS mapas
        this.usuarios.put(nombre, nuevo);
        this.usuariosPorEmail.put(email, nuevo);

        return nuevo;
    }


   public UsuarioPremium registrarUsuarioPremium(String nombre, String email, String password) throws UsuarioYaExisteException, EmailInvalidoException, PasswordDebilException{

        return registrarUsuarioPremium(nombre, email, password, TipoSuscripcion.PREMIUM);
   }

    public UsuarioGratuito registrarUsuarioGratuito(String nombre, String email, String password)
            throws EmailInvalidoException, UsuarioYaExisteException, PasswordDebilException {

        //  Validarl nombre de usuario ya existe
        if (usuarios.containsKey(nombre)) {
            throw new UsuarioYaExisteException("El nombre de usuario ya está en uso.");
        }

        // Validar si el EMAIL ya existe

        if (usuariosPorEmail.containsKey(email)) {
            throw new UsuarioYaExisteException("El email ya está registrado.");
        }

        // Crear instancia
        //
        UsuarioGratuito nuevo = new UsuarioGratuito(nombre, email, password);

        //  Guardar en AMBOS mapas para mantener la integridad
        this.usuarios.put(nombre, nuevo);
        this.usuariosPorEmail.put(email, nuevo);

        return nuevo;
    }

    //Devuelve todos los premium
    public ArrayList<UsuarioPremium> getUsuariosPremium(){
        ArrayList<UsuarioPremium> usersPremium = new ArrayList<>();

        //Recorrer los valores de usuarios y si algun es "intancia de Premium" de lo guardas

        for(Usuario u : usuarios.values()){
            if(u instanceof UsuarioPremium ){
                usersPremium.add((UsuarioPremium) u); // casting
            }
        }
        return  usersPremium;
    }

    //Devuelve todos los gratuitos.
    public ArrayList<UsuarioGratuito> getUsuariosGratuitos(){
        
    }















}
