package modelo.usuarios;

import enums.TipoSuscripcion;
import excepciones.contenido.ContenidoNoDisponibleException; // <--- FALTABA ESTO
import excepciones.usuario.AnuncioRequeridoException;
import excepciones.usuario.EmailInvalidoException;
import excepciones.usuario.LimiteDiarioAlcanzadoException;
import excepciones.usuario.PasswordDebilException;
import modelo.contenido.Contenido;
import modelo.plataforma.Anuncio;

import java.util.Date;

public class UsuarioGratuito extends Usuario {

    // --- ATRIBUTOS ---
    private int anunciosEscuchados;
    private Date ultimoAnuncio;
    private int reproduccionesHoy;
    private int limiteReproducciones;
    private int cancionesSinAnuncio;
    private Date fechaUltimaReproduccion;

    // --- CONSTANTES ---
    private static final int LIMITE_DIARIO = 50;
    private static final int CANCIONES_ENTRE_ANUNCIOS = 3;

    // --- CONSTRUCTOR ---
    public UsuarioGratuito(String nombre, String email, String password)
            throws EmailInvalidoException, PasswordDebilException {
        super(nombre, email, password, TipoSuscripcion.GRATUITO);

        this.limiteReproducciones = LIMITE_DIARIO;
        this.reproduccionesHoy = 0;
        this.cancionesSinAnuncio = 0;
        this.anunciosEscuchados = 0;
    }

    // --- GETTERS Y SETTERS ---
    public int getAnunciosEscuchados() { return anunciosEscuchados; }
    public void setAnunciosEscuchados(int anunciosEscuchados) { this.anunciosEscuchados = anunciosEscuchados; }

    public Date getUltimoAnuncio() { return ultimoAnuncio; }

    public void setUltimoAnuncio(Date ultimoAnuncio) { this.ultimoAnuncio = ultimoAnuncio; }

    public int getReproduccionesHoy() { return reproduccionesHoy; }
    public void setReproduccionesHoy(int reproduccionesHoy) { this.reproduccionesHoy = reproduccionesHoy; }

    public int getLimiteReproducciones() { return limiteReproducciones; }
    public void setLimiteReproducciones(int limiteReproducciones) { this.limiteReproducciones = limiteReproducciones; }

    public int getCancionesSinAnuncio() { return cancionesSinAnuncio; }
    public void setCancionesSinAnuncio(int cancionesSinAnuncio) { this.cancionesSinAnuncio = cancionesSinAnuncio; }

    public Date getFechaUltimaReproduccion() { return fechaUltimaReproduccion; }
    public void setFechaUltimaReproduccion(Date fechaUltimaReproduccion) { this.fechaUltimaReproduccion = fechaUltimaReproduccion; }

    // --- MÉTODOS DE LÓGICA ---

    public boolean puedeReproducir(){
        return this.reproduccionesHoy < this.limiteReproducciones;
    }

    public boolean debeVerAnuncio(){
        return this.cancionesSinAnuncio >= CANCIONES_ENTRE_ANUNCIOS;
    }

    // CORREGIDO: Llamamos a la versión con argumento pasándole null
    public void verAnuncio (){
        verAnuncio(null);
    }

    public void verAnuncio (Anuncio anuncio){
        if(anuncio != null){
            System.out.println("Viendo anuncio específico...");
            // anuncio.reproducir();
        } else {
            System.out.println("Viendo anuncio genérico...");
        }

        this.cancionesSinAnuncio = 0;
        this.anunciosEscuchados++;
        this.ultimoAnuncio = new Date();
    }


    public void reiniciarContadorDiario(){
        this.reproduccionesHoy = 0;
    }

    public int getReproduccionesRestantes(){
        // Math.max evita negativos
        return Math.max(0, this.limiteReproducciones - this.reproduccionesHoy);
    }

    public int getCancionesHastaAnuncio(){
        // Math.max evita negativos
        return Math.max(0, CANCIONES_ENTRE_ANUNCIOS - this.cancionesSinAnuncio);
    }


    @Override
    public void reproducir(Contenido contenido) throws ContenidoNoDisponibleException, LimiteDiarioAlcanzadoException, AnuncioRequeridoException {

        if(!puedeReproducir()){
            // CORREGIDO: Faltaba el punto y coma final
            throw new LimiteDiarioAlcanzadoException("Límite diario alcanzado");
        }

        if (debeVerAnuncio()) {
            throw new AnuncioRequeridoException("Es necesario ver un anuncio");
        }

        contenido.reproducir();

        this.reproduccionesHoy++;
        this.cancionesSinAnuncio++;
        this.fechaUltimaReproduccion = new Date();

        // Guarda en historial
        agregarAlHistorial(contenido);
    }
}