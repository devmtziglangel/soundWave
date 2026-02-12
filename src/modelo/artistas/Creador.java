package modelo.artistas;

import enums.CategoriaPodcast;
import excepciones.artista.LimiteEpisodiosException;
import excepciones.contenido.EpisodioNoEncontradoException;
import modelo.contenido.Podcast;
import utilidades.EstadisticasCreador;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map; // <--- NUEVO IMPORT
import java.util.Objects;
import java.util.UUID;

public class Creador {
    private String id;
    private String nombreCanal;
    private String nombre;
    private ArrayList<Podcast> episodios;
    private int suscriptores;
    private String descripcion;

    // CAMBIO 1: Usamos la interfaz Map en lugar de la clase concreta HashMap
    private Map<String, String> redesSociales;

    private ArrayList<CategoriaPodcast> categoriasPrincipales;

    private static final int MAX_EPISODIOS = 500;

    // CONSTRUCTOR A
    public Creador(String nombreCanal, String nombre) {
        this.id = UUID.randomUUID().toString();
        this.nombreCanal = nombreCanal;
        this.nombre = nombre;
        this.episodios = new ArrayList<>();
        this.suscriptores = 0;
        this.descripcion = null;

        // CAMBIO 2: Inicialización del Map
        this.redesSociales = new HashMap<>();

        this.categoriasPrincipales = new ArrayList<>();
    }

    // CONSTRUCTOR B
    public Creador(String nombreCanal, String nombre, String descripcion) {
        this(nombreCanal, nombre);
        this.descripcion = descripcion;
    }

    // --- GETTERS Y SETTERS ---

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombreCanal() { return nombreCanal; }
    public void setNombreCanal(String nombreCanal) { this.nombreCanal = nombreCanal; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public ArrayList<Podcast> getEpisodios() { return episodios; }
    public void setEpisodios(ArrayList<Podcast> episodios) { this.episodios = episodios; }

    public int getSuscriptores() { return suscriptores; }
    public void setSuscriptores(int suscriptores) { this.suscriptores = suscriptores; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    // Ajustamos el getter para devolver Map (o HashMap si el test lo exige estrictamente, pero Map es mejor)
    public Map<String, String> getRedesSociales() { return redesSociales; }
    public void setRedesSociales(Map<String, String> redesSociales) { this.redesSociales = redesSociales; }

    public ArrayList<CategoriaPodcast> getCategoriasPrincipales() { return categoriasPrincipales; }
    public void setCategoriasPrincipales(ArrayList<CategoriaPodcast> categoriasPrincipales) { this.categoriasPrincipales = categoriasPrincipales; }

    public int getNumEpisodios() {
        return (episodios != null) ? episodios.size() : 0;
    }

    // --- MÉTODOS SOLICITADOS (CAMBIOS) ---

    // CAMBIO 3: Método agregarRedSocial con toLowerCase()
    public void agregarRedSocial(String red, String usuario) {
        // Convertimos la clave a minúsculas (ej: "Twitter" -> "twitter")
        // Así estandarizamos el guardado.
        redesSociales.put(red.toLowerCase(), usuario);
    }

    // CAMBIO 4: Método específico para Twitter
    public String getTwitter() {
        // Busca específicamente la clave en minúsculas
        return redesSociales.get("twitter");
    }

    // --- MÉTODOS DE NEGOCIO RESTANTES ---

    public void publicarPodcast(Podcast episodio) throws LimiteEpisodiosException {
        if (episodios.size() >= MAX_EPISODIOS) throw new LimiteEpisodiosException("No entran mas episodios");
        episodios.add(episodio);
    }

    public EstadisticasCreador obtenerEstadisticas() {
        return new EstadisticasCreador(this);
    }

    public double calcularPromedioReproducciones() {
        if (episodios.isEmpty()) return 0.0;
        return (double) getTotalReproducciones() / episodios.size();
    }

    public void eliminarEpisodio(String idEpisodio) throws EpisodioNoEncontradoException {
        for (Podcast p : episodios) {
            if (p.getId().equals(idEpisodio)) {
                episodios.remove(p);
                return;
            }
        }
        throw new EpisodioNoEncontradoException("No se encontró el episodio");
    }

    public int getTotalReproducciones() {
        int total = 0;
        for (Podcast p : episodios) {
            total += p.getReproducciones();
        }
        return total;
    }

    public void incrementarSuscriptores() {
        this.suscriptores++;
    }

    public ArrayList<Podcast> obtenerTopEpisodios(int cantidad) {
        ArrayList<Podcast> ordenadas = new ArrayList<>(this.episodios);
        ordenadas.sort((c1, c2) -> Integer.compare(c2.getReproducciones(), c1.getReproducciones()));

        ArrayList<Podcast> top = new ArrayList<>();
        int limite = Math.min(cantidad, ordenadas.size());
        for (int i = 0; i < limite; i++) {
            top.add(ordenadas.get(i));
        }
        return top;
    }

    public int getUltimaTemporada() {
        int maxTemp = 0;
        for (Podcast p : episodios) {
            if (p.getTemporada() > maxTemp) {
                maxTemp = p.getTemporada();
            }
        }
        return maxTemp;
    }

    @Override
    public String toString() {
        return "Creador: " + nombreCanal + " (" + nombre + ") | Suscriptores: " + suscriptores + " | Episodios: " + getNumEpisodios();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Creador otro = (Creador) obj;
        return Objects.equals(this.id, otro.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}