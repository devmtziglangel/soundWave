package modelo.usuarios;

import excepciones.usuario.EmailInvalidoException;
import excepciones.usuario.PasswordDebilException;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public abstract class Usuario {
    protected String id;
    protected String nombre;
    protected String email;
    protected String password;
    protected TipoSuscripcion suscripcion;
    protected ArrayList<Playlist> misPlaylists;
    protected ArrayList<Contenido> historial;
    protected Date fechaRegistro;

    // CONSTRUCTOR
    public Usuario( String nombre, String email, String password, TipoSuscripcion suscripcion) {
        this.id = UUID.randomUUID().toString();;
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.suscripcion = suscripcion;


        this.misPlaylists = new ArrayList<>();
        this.historial = new ArrayList<>();


        this.fechaRegistro = new Date();
    }

    // GETTERS Y SETTERS
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getEmail() { return email; }

    public void setEmail(String email) throws EmailInvalidoException {
        if (validarEmail(email)) {
            this.email = email;
        } else {
            throw new EmailInvalidoException("Error: El formato del email no es válido.");
        }
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getPassword() { return password; }

    public void setPassword(String password) throws PasswordDebilException {
        if (validarPassword(password)) {
            this.password = password;
        } else {
            throw new PasswordDebilException("Error: El password es débil. Debe tener al menos 8 caracteres.");
        }
    }

    public TipoSuscripcion getSuscripcion() { return suscripcion; }
    public void setSuscripcion(TipoSuscripcion suscripcion) { this.suscripcion = suscripcion; }

    public ArrayList<Playlist> getMisPlaylists() { return misPlaylists; }
    public void setMisPlaylists(ArrayList<Playlist> misPlaylists) { this.misPlaylists = misPlaylists; }

    public ArrayList<Contenido> getHistorial() { return historial; }
    public void setHistorial(ArrayList<Contenido> historial) { this.historial = historial; }

    public Date getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(Date fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    // METODO ABSTRACTO
    public abstract void reproducir(Contenido contenido);

    // METODOS CONCRETOS
    public void crearPlaylist(String nombre) {
        this.misPlaylists.add(new Playlist(nombre));
    }

    public void seguirPlaylist(Playlist playlist) {
        this.misPlaylists.add(playlist);
    }

    public void darLike(Contenido contenido) {
        contenido.agregarLike();
    }

    // VALIDACIONES
    public boolean validarEmail(String email) {
        if (email == null) return false;
        String regex = "^[\\w.-]+@[\\w.-]+\\.[a-z]{2,4}$";
        return email.matches(regex);
    }

    public boolean validarPassword(String password) {
        // Corregido: Validamos el parámetro 'password', no el atributo de la clase
        return password != null && password.length() >= 8;
    }
}