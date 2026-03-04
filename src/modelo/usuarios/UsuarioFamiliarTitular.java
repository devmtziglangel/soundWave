package modelo.usuarios;

import excepciones.usuario.EmailInvalidoException;
import excepciones.usuario.PasswordDebilException;
import modelo.contenido.Contenido;
import excepciones.contenido.ContenidoNoDisponibleException;
import excepciones.usuario.AnuncioRequeridoException;
import excepciones.usuario.LimiteDiarioAlcanzadoException;

import java.util.ArrayList;

public class UsuarioFamiliarTitular extends UsuarioFamiliar {

    // ATRIBUTOS
    private ArrayList<UsuarioFamiliar> miembrosFamiliares;
    private ArrayList<Contenido> historialFamiliar;
    private static final int MAX_MIEMBROS = 6;

    // CONSTRUCTOR
    public UsuarioFamiliarTitular(String nombre, String email, String password, String nombreFamilia)
            throws EmailInvalidoException, PasswordDebilException {
        // Llamar al constructor de UsuarioFamiliar
        super(nombre, email, password, nombreFamilia);

        // Inicializar listas
        this.miembrosFamiliares = new ArrayList<>();
        this.historialFamiliar = new ArrayList<>();

        // Asignarse a sí mismo como titular y agregarse como primer miembro
        this.cuentaTitular = this;
        this.miembrosFamiliares.add(this);
    }

    // GETTERS Y SETTERS

    public ArrayList<UsuarioFamiliar> getMiembrosFamiliares() {
        return new ArrayList<>(miembrosFamiliares);
    }

    public void setMiembrosFamiliares(ArrayList<UsuarioFamiliar> miembrosFamiliares) {
        this.miembrosFamiliares = miembrosFamiliares;
    }

    public ArrayList<Contenido> getHistorialFamiliar() {
        return new ArrayList<>(historialFamiliar);
    }

    public void setHistorialFamiliar(ArrayList<Contenido> historialFamiliar) {
        this.historialFamiliar = historialFamiliar;
    }

    public static int getMaxMiembros() {
        return MAX_MIEMBROS;
    }

    // MÉTODOS CRUD DE MIEMBROS

    /**
     * Agrega un nuevo miembro familiar a la cuenta.
     * @param nombre Nombre del nuevo miembro
     * @param email Email del nuevo miembro
     * @param password Contraseña del nuevo miembro
     * @return El nuevo miembro familiar creado
     * @throws EmailInvalidoException Si el email no es válido
     * @throws PasswordDebilException Si la contraseña no es fuerte
     * @throws IllegalStateException Si se ha alcanzado el límite de miembros
     */
    public UsuarioFamiliar agregarMiembro(String nombre, String email, String password)
            throws EmailInvalidoException, PasswordDebilException {

        if (miembrosFamiliares.size() >= MAX_MIEMBROS) {
            throw new IllegalStateException("Se ha alcanzado el límite máximo de " + MAX_MIEMBROS + " miembros familiares");
        }

        // Crear nuevo miembro
        UsuarioFamiliar nuevoMiembro = new UsuarioFamiliar(nombre, email, password, this.getNombreFamilia());
        nuevoMiembro.setCuentaTitular(this);

        // Agregar a lista de miembros
        miembrosFamiliares.add(nuevoMiembro);

        return nuevoMiembro;
    }

    /**
     * Elimina un miembro de la familia.
     * @param miembro El miembro a eliminar
     * @return true si se eliminó correctamente, false si no se encontraba
     */
    public boolean eliminarMiembro(UsuarioFamiliar miembro) {
        // No permitir eliminar al titular de la cuenta
        if (miembro.equals(this)) {
            throw new IllegalArgumentException("No se puede eliminar al titular de la cuenta familiar");
        }
        return miembrosFamiliares.remove(miembro);
    }

    /**
     * Obtiene la lista de todos los miembros de la familia.
     * @return Lista defensiva de miembros
     */
    public ArrayList<UsuarioFamiliar> obtenerMiembros() {
        return new ArrayList<>(miembrosFamiliares);
    }

    /**
     * Obtiene el historial de reproducción compartido de la familia.
     * @return Lista defensiva del historial familiar
     */
    public ArrayList<Contenido> obtenerHistorialFamiliar() {
        return new ArrayList<>(historialFamiliar);
    }

    /**
     * Agrega contenido al historial compartido de la familia.
     * @param contenido El contenido a agregar
     */
    public void agregarAlHistorialFamiliar(Contenido contenido) {
        if (contenido != null && !historialFamiliar.contains(contenido)) {
            historialFamiliar.add(contenido);
        }
    }

    // SOBRESCRITURA DE MÉTODO REPRODUCIR

    @Override
    public void reproducir(Contenido contenido)
            throws ContenidoNoDisponibleException, LimiteDiarioAlcanzadoException, AnuncioRequeridoException {

        if (!contenido.isDisponible()) {
            throw new ContenidoNoDisponibleException("El contenido no está disponible");
        }

        // Agregar a historial personal
        this.agregarAlHistorial(contenido);

        // Agregar a historial familiar (ya que soy el titular)
        this.agregarAlHistorialFamiliar(contenido);

        // Reproducir el contenido
        contenido.reproducir();
    }

    @Override
    public String toString() {
        return String.format("UsuarioFamiliarTitular: %s | Familia: %s | Miembros: %d/%d | Email: %s | ID: %s",
                this.getNombre(),
                this.getNombreFamilia(),
                miembrosFamiliares.size(),
                MAX_MIEMBROS,
                this.getEmail(),
                this.getId());
    }
}

