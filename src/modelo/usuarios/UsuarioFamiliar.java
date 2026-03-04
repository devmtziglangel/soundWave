package modelo.usuarios;

import enums.TipoSuscripcion;
import excepciones.contenido.ContenidoNoDisponibleException;
import excepciones.usuario.AnuncioRequeridoException;
import excepciones.usuario.EmailInvalidoException;
import excepciones.usuario.LimiteDiarioAlcanzadoException;
import excepciones.usuario.PasswordDebilException;
import modelo.contenido.Contenido;

public class UsuarioFamiliar extends Usuario {

    // ATRIBUTOS
    protected Usuario cuentaTitular;
    protected String nombreFamilia;

    // CONSTRUCTOR
    public UsuarioFamiliar(String nombre, String email, String password, String nombreFamilia)
            throws EmailInvalidoException, PasswordDebilException {
        // Llamar al constructor del padre con TipoSuscripcion.FAMILIAR
        super(nombre, email, password, TipoSuscripcion.FAMILIAR);
        this.nombreFamilia = nombreFamilia;
        this.cuentaTitular = null; // Se asignará después por el titular
    }

    // GETTERS Y SETTERS

    public Usuario getCuentaTitular() {
        return cuentaTitular;
    }

    public void setCuentaTitular(Usuario cuentaTitular) {
        this.cuentaTitular = cuentaTitular;
    }

    public String getNombreFamilia() {
        return nombreFamilia;
    }

    public void setNombreFamilia(String nombreFamilia) {
        this.nombreFamilia = nombreFamilia;
    }

    // MÉTODO ABSTRACTO SOBRESCRITO

    @Override
    public void reproducir(Contenido contenido)
            throws ContenidoNoDisponibleException, LimiteDiarioAlcanzadoException, AnuncioRequeridoException {

        if (!contenido.isDisponible()) {
            throw new ContenidoNoDisponibleException("El contenido no está disponible");
        }

        // Agregar a historial personal
        this.agregarAlHistorial(contenido);

        // Agregar a historial familiar si el titular es un UsuarioFamiliarTitular
        if (this.cuentaTitular instanceof UsuarioFamiliarTitular) {
            ((UsuarioFamiliarTitular) this.cuentaTitular).agregarAlHistorialFamiliar(contenido);
        }

        // Reproducir el contenido
        contenido.reproducir();
    }

    @Override
    public String toString() {
        return String.format("UsuarioFamiliar: %s | Familia: %s | Email: %s | ID: %s",
                this.getNombre(),
                this.nombreFamilia,
                this.getEmail(),
                this.getId());
    }
}

