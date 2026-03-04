package modelo.contenido;

import excepciones.contenido.ContenidoNoDisponibleException;
import excepciones.contenido.DuracionInvalidaException;
import excepciones.descarga.ContenidoYaDescargadoException;
import excepciones.descarga.LimiteDescargasException;
import interfaces.Descargable;
import interfaces.Reproducible;

public class AudioLibro extends Contenido implements Reproducible, Descargable {

    // ATRIBUTOS
    private String autor;
    private String narrador;
    private int capitulos;
    private String ISBN;

    private boolean reproduciendo;
    private boolean pausado;
    private boolean descargado;

    // CONSTRUCTOR 1: Con todos los atributos
    public AudioLibro(String titulo, int duracionSegundos, String autor, String narrador, int capitulos, String ISBN)
            throws DuracionInvalidaException {
        super(titulo, duracionSegundos);
        this.autor = autor;
        this.narrador = narrador;
        this.capitulos = capitulos;
        this.ISBN = ISBN;
        this.reproduciendo = false;
        this.pausado = false;
        this.descargado = false;
    }

    // CONSTRUCTOR 2: Únicamente con parámetros necesarios (titulo, duracion, autor)
    public AudioLibro(String titulo, int duracionSegundos, String autor)
            throws DuracionInvalidaException {
        super(titulo, duracionSegundos);
        this.autor = autor;
        this.narrador = "";
        this.capitulos = 0;
        this.ISBN = "";
        this.reproduciendo = false;
        this.pausado = false;
        this.descargado = false;
    }

    // GETTERS Y SETTERS

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getNarrador() {
        return narrador;
    }

    public void setNarrador(String narrador) {
        this.narrador = narrador;
    }

    public int getCapitulos() {
        return capitulos;
    }

    public void setCapitulos(int capitulos) {
        this.capitulos = capitulos;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public boolean isReproduciendo() {
        return reproduciendo;
    }

    public void setReproduciendo(boolean reproduciendo) {
        this.reproduciendo = reproduciendo;
    }

    public boolean isPausado() {
        return pausado;
    }

    public void setPausado(boolean pausado) {
        this.pausado = pausado;
    }

    public boolean isDescargado() {
        return descargado;
    }

    public void setDescargado(boolean descargado) {
        this.descargado = descargado;
    }

    // MÉTODOS DE LA INTERFAZ REPRODUCIBLE

    @Override
    public void play() {
        this.reproduciendo = true;
        this.pausado = false;
    }

    @Override
    public void pause() {
        this.pausado = true;
        this.reproduciendo = false;
    }

    @Override
    public void stop() {
        this.reproduciendo = false;
        this.pausado = false;
    }

    @Override
    public int getDuracion() {
        return this.duracionSegundos;
    }

    // MÉTODOS DE LA INTERFAZ DESCARGABLE

    @Override
    public boolean descargar() throws LimiteDescargasException, ContenidoYaDescargadoException {
        if (this.descargado) {
            throw new ContenidoYaDescargadoException("El audiolibro ya está descargado");
        }
        this.descargado = true;
        return true;
    }

    @Override
    public boolean eliminarDescarga() {
        if (this.descargado) {
            this.descargado = false;
            return true;
        }
        return false;
    }

    @Override
    public int espacioRequerido() {
        // Aproximación: 20 MB por hora de audio
        int horas = this.duracionSegundos / 3600;
        return Math.max(horas * 20, 50); // Mínimo 50 MB
    }

    // MÉTODO ABSTRACTO DE CONTENIDO

    @Override
    public void reproducir() throws ContenidoNoDisponibleException {
        if (!this.disponible) {
            throw new ContenidoNoDisponibleException("El audiolibro no está disponible");
        }
        this.play();
        this.aumentarReproducciones();
    }

    @Override
    public String toString() {
        return String.format("AudioLibro: %s por %s - Narrador: %s (%s)",
            this.titulo, this.autor, this.narrador, this.getDuracionFormateada());
    }
}

