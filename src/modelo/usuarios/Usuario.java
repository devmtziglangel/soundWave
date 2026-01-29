package modelo.usuarios;

import enums.TipoSuscripcion;
import excepciones.usuario.EmailInvalidoException;
import excepciones.usuario.PasswordDebilException;
import modelo.contenido.Contenido;
import modelo.plataforma.Playlist;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Pattern;

public abstract class Usuario {

    // Atributos
    protected String id;
    protected String nombre;
    protected String email;
    protected String password;
    protected TipoSuscripcion suscripcion;

    // Listas
    protected ArrayList<Playlist> misPlaylist;
    protected ArrayList<Contenido> historial;

    // Fecha
    protected Date fechaRegistro;

    // Constantes Regex
    private static final String EMAIL_REGEX = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    private static final String PASSWORD_REGEX = "^.{8,}$";

    // --- CONSTRUCTOR ---
    public Usuario(String nombre, String email, String password, TipoSuscripcion suscripcion)
            throws EmailInvalidoException, PasswordDebilException {

        this.id = UUID.randomUUID().toString();
        this.nombre = nombre;

        // 1. Asignar y VALIDAR Email inmediatamente
        this.email = email;
        if (!validarEmail()) {
            throw new EmailInvalidoException("El email ingresado no es válido.");
        }

        // 2. Asignar y VALIDAR Password inmediatamente
        this.password = password;
        if (!validarPassword()) {
            throw new PasswordDebilException("La contraseña debe tener al menos 8 caracteres.");
        }

        this.suscripcion = suscripcion;
        this.misPlaylist = new ArrayList<>();
        this.historial = new ArrayList<>();
        this.fechaRegistro = new Date();
    }

    // --- GETTERS Y SETTERS ---

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }

    // Setter de Email con Validación y Rollback
    public void setEmail(String nuevoEmail) throws EmailInvalidoException {
        String emailAnterior = this.email;
        this.email = nuevoEmail;

        if (!validarEmail()) {
            this.email = emailAnterior; // Restaurar si falla
            throw new EmailInvalidoException("El email no es válido.");
        }
    }

    public String getPassword() { return password; }

    // Setter de Password con Validación y Rollback (AÑADIDO)
    public void setPassword(String nuevaPassword) throws PasswordDebilException {
        String passAnterior = this.password;
        this.password = nuevaPassword;

        if (!validarPassword()) {
            this.password = passAnterior; // Restaurar si falla
            throw new PasswordDebilException("La contraseña es muy débil (mínimo 8 caracteres).");
        }
    }

    public TipoSuscripcion getSuscripcion() { return suscripcion; }
    public void setSuscripcion(TipoSuscripcion suscripcion) { this.suscripcion = suscripcion; }

    public ArrayList<Playlist> getMisPlaylist() { return misPlaylist; }
    public void setMisPlaylist(ArrayList<Playlist> misPlaylist) { this.misPlaylist = misPlaylist; }

    public ArrayList<Contenido> getHistorial() { return historial; }
    public void setHistorial(ArrayList<Contenido> historial) { this.historial = historial; }

    public Date getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(Date fechaRegistro) { this.fechaRegistro = fechaRegistro; }


    // --- REGLAS DE NEGOCIO ---

    public abstract void reproducir(Contenido contenido);

    public void crearPlaylist(String nombre) {
        // Esto marcará error hasta que creemos la clase Playlist con el constructor correcto
        this.misPlaylist.add(new Playlist(nombre, this));
    }

    // --- MÉTODOS DE VALIDACIÓN ---

    public boolean validarEmail() {
        if (this.email == null) return false;
        return Pattern.matches(EMAIL_REGEX, this.email);
    }

    public boolean validarPassword() {
        if (this.password == null) return false;
        // CORREGIDO: Ahora valida 'this.password' (antes validaba 'this.email')
        return Pattern.matches(PASSWORD_REGEX, this.password);
    }
}