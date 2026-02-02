package modelo.usuarios;

// IMPORTS
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
import java.util.regex.Pattern;

public abstract class Usuario {

    // ATRIBUTOS
    protected String id;
    protected String nombre;
    protected String email;
    protected String password;
    protected TipoSuscripcion suscripcion;
    protected Date fechaRegistro;

    // Listas (Relaciones)
    protected ArrayList<Playlist> misPlaylists;
    protected ArrayList<Contenido> historial;
    protected ArrayList<Playlist> playlistsSeguidas;
    protected ArrayList<Contenido> contenidosLiked;

    // Constantes internas
    private static final String EMAIL_REGEX = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    private static final String PASSWORD_REGEX = "^.{8,}$";
    private static final int MAX_HISTORIAL = 50;

    // CONSTRUCTOR
    public Usuario(String nombre, String email, String password, TipoSuscripcion suscripcion)
            throws EmailInvalidoException, PasswordDebilException {

        this.id = UUID.randomUUID().toString();
        this.nombre = nombre;

        // Validacion inmediata del email
        if (email == null || !Pattern.matches(EMAIL_REGEX, email)) {
            throw new EmailInvalidoException("El email ingresado no tiene un formato válido.");
        }
        this.email = email;

        // Validacion inmediata del password
        if (password == null || !Pattern.matches(PASSWORD_REGEX, password)) {
            throw new PasswordDebilException("La contraseña debe tener al menos 8 caracteres.");
        }
        this.password = password;

        this.suscripcion = suscripcion;
        this.fechaRegistro = new Date();

        // Inicialización de listas vacías
        this.misPlaylists = new ArrayList<>();
        this.historial = new ArrayList<>();
        this.playlistsSeguidas = new ArrayList<>();
        this.contenidosLiked = new ArrayList<>();
    }

    // ==========================================
    // GETTERS Y SETTERS
    // ==========================================

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

    public void setEmail(String email) throws EmailInvalidoException {
        if (email == null || !Pattern.matches(EMAIL_REGEX, email)) {
            throw new EmailInvalidoException("El email no es válido.");
        }
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) throws PasswordDebilException {
        if (password == null || !Pattern.matches(PASSWORD_REGEX, password)) {
            throw new PasswordDebilException("La contraseña es muy débil.");
        }
        this.password = password;
    }

    public TipoSuscripcion getSuscripcion() {
        return suscripcion;
    }

    public void setSuscripcion(TipoSuscripcion suscripcion) {
        this.suscripcion = suscripcion;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }



    // Getters  DEFENSIVOS (Requerido por README)

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

    // ==========================================
    // MÉTODOS DE NEGOCIO
    // ==========================================

    // Método abstracto (Polimorfismo)
    public abstract void reproducir(Contenido contenido)
            throws ContenidoNoDisponibleException, LimiteDiarioAlcanzadoException, AnuncioRequeridoException;

    public void crearPlaylist(String nombre) {


        this.misPlaylists.add(new Playlist(nombre, this));
    }

    public void seguirPlaylist(Playlist playlist) {
        if (!playlistsSeguidas.contains(playlist)) {
            playlistsSeguidas.add(playlist);
            // playlist.agregarSeguidor(this); // Lógica futura en Playlist
        }
    }

    public void dejarDeSeguirPlaylist(Playlist playlist) {
        if (playlistsSeguidas.contains(playlist)) {
            playlistsSeguidas.remove(playlist);
            // playlist.eliminarSeguidor(this); // Lógica futura en Playlist
        }
    }

    public void darLike(Contenido contenido) {
        if (!contenidosLiked.contains(contenido)) {
            contenidosLiked.add(contenido);
            contenido.agregarLike();
        }
    }

    public void quitarLike(Contenido contenido) {
        if (contenidosLiked.contains(contenido)) {
            contenidosLiked.remove(contenido);

        }
    }

    public void agregarAlHistorial(Contenido contenido) {

        historial.remove(contenido);
        historial.add(0, contenido);

        // Limitar tamaño del historial
        if (historial.size() > MAX_HISTORIAL) {
            historial.remove(historial.size() - 1);
        }
    }

    public void limpiarHistorial() {
        historial.clear();
    }

    public boolean esPremium() {
        return this.suscripcion != TipoSuscripcion.GRATUITO;
    }

    // Métodos de utilidad para validaciones internas
    public boolean validarEmail() {
        return email != null && Pattern.matches(EMAIL_REGEX, email);
    }

    public boolean validarPassword() {
        return password != null && Pattern.matches(PASSWORD_REGEX, password);
    }
}