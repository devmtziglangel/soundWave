package modelo.usuarios;


import enums.TipoSuscripcion;
import excepciones.contenido.ContenidoNoDisponibleException;
import excepciones.usuario.AnuncioRequeridoException;
import excepciones.usuario.EmailInvalidoException;
import excepciones.usuario.LimiteDiarioAlcanzadoException;
import excepciones.usuario.PasswordDebilException;
import modelo.contenido.Contenido;
import modelo.plataforma.Playlist;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public abstract class Usuario {


    private String id;
    private String nombre;
    private String email;
    private String password;
    private TipoSuscripcion suscripcion;
    private ArrayList<Playlist> misPlaylists;
    private ArrayList<Contenido> historial;
    private Date fechaRegistro;
    private ArrayList<Playlist> playlistsSeguidas;
    private ArrayList<Contenido>contenidosLiked;


     private static final String EMAIL_REGEX = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
     private static final String PASSWORD_REGEX = "^.{8,}$";

     private static final int LIMITE_HISTORIAL = 40;

    public Usuario(String nombre, String email, String password, TipoSuscripcion suscripcion) throws EmailInvalidoException, PasswordDebilException {

        this.id = UUID.randomUUID().toString();

        //ATRIBUTOS PARAMETIZADOS
        this.nombre = nombre;
        this.email = email;
                    validarEmail();
        this.password = password;
                        validarPassword();
        this.suscripcion = suscripcion;

        //ATRIBUTOS INICIAR

        this.misPlaylists = new ArrayList<>();
        this.historial = new ArrayList<>();

        this.fechaRegistro = new Date();
        this.playlistsSeguidas = new ArrayList<>();
        this.contenidosLiked = new ArrayList<>();


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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public TipoSuscripcion getSuscripcion() {
        return suscripcion;
    }

    public void setSuscripcion(TipoSuscripcion suscripcion) {
        this.suscripcion = suscripcion;
    }

    public ArrayList<Playlist> getMisPlaylists() {
        return new ArrayList<>(misPlaylists);
    }

    public void setMisPlaylists(ArrayList<Playlist> misPlaylists) {
        this.misPlaylists = misPlaylists;
    }

    public ArrayList<Contenido> getHistorial() {
        return new ArrayList<>(historial);
    }

    public void setHistorial(ArrayList<Contenido> historial) {
        this.historial = historial;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public ArrayList<Playlist> getPlaylistsSeguidas() {
        return new ArrayList<>(playlistsSeguidas);
    }

    public void setPlaylistsSeguidas(ArrayList<Playlist> playlistsSeguidas) {
        this.playlistsSeguidas = playlistsSeguidas;
    }

    public ArrayList<Contenido> getContenidosLiked() {
        return new ArrayList<>(contenidosLiked);
    }

    public void setContenidosLiked(ArrayList<Contenido> contenidosLiked) {
        this.contenidosLiked = contenidosLiked;
    }


    //METODOS ABSTRACTOS

    public abstract void reproducir (Contenido contenido) throws ContenidoNoDisponibleException, LimiteDiarioAlcanzadoException, AnuncioRequeridoException;

    //METODOS CONCRETOS

    public boolean validarEmail () throws EmailInvalidoException{

        if(email.matches(EMAIL_REGEX)) return true;
        throw new EmailInvalidoException("El correo no cumple con los formatos exigidos");
    }

    public boolean validarPassword()throws  PasswordDebilException{
        if(password.matches(PASSWORD_REGEX)) return true;
        throw new PasswordDebilException("La constraseña no cumple con las condiciones de seguridad");


    }

    public Playlist crearPlaylist (String nombrePlaylist){
        Playlist nuevaPlaylist = new Playlist(nombrePlaylist, this);
        this.misPlaylists.add(nuevaPlaylist);
        return nuevaPlaylist;


    }

   public void seguirPlaylist (Playlist playlist){
        if(playlist.isEsPublica() && !playlistsSeguidas.contains(playlist)){
            this.playlistsSeguidas.add(playlist);
            playlist.incrementarSeguidores();
        }
   }

   public void dejarSeguirPlaylist (Playlist playlist) throws IllegalAccessException {
        if(playlistsSeguidas.contains(playlist)){
            playlistsSeguidas.remove(playlist);
            playlist.decrementarSeguidores();
        }
   }

   public void darLike(Contenido contenido){
        if(!contenidosLiked.contains(contenido)){
            contenidosLiked.add(contenido);
        }
        contenido.agregarLike();

   }

    public void quitarLike(Contenido contenido){
        if(contenidosLiked.contains(contenido)){
            contenidosLiked.remove(contenido);
        }
    }

    public void agregarHistorial (Contenido contenido){
       if(historial.size()>=LIMITE_HISTORIAL){
           historial.removeFirst();
       }
       historial.add(contenido);

    }

    public void limpiarHistoril (){
        if(!historial.isEmpty()){
            historial.clear();
        }
    }

    public boolean esPremium() { // llamar al puto enum!'
        return this.suscripcion != TipoSuscripcion.GRATUITO;
    }


    @Override
    public String toString() {
        return String.format("Usuario: %s | Email: %s | Plan: %s | ID: %s",
                this.nombre,
                this.email,
                this.suscripcion,
                this.id);
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) return true;


        if (obj == null || getClass() != obj.getClass()) return false;


        Usuario otroUsuario = (Usuario) obj;
        return this.id.equals(otroUsuario.id);
    }

    @Override
    public int hashCode() {

        return this.id.hashCode();
    }









}