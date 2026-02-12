package modelo.artistas;

import excepciones.artista.AlbumYaExisteException; // OJO: El paquete suele ser singular 'excepciones.artista'
import excepciones.artista.ArtistaNoVerificadoException;
import modelo.contenido.Cancion;
// import modelo.contenido.Contenido; // No se usa directamente, se puede quitar
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID; // Necesario para generar el ID

public class Artista {
    // ATRIBUTOS
    private String id;
    private String nombreArtistico;
    private String nombreReal;
    private String paisOrigen;
    private ArrayList<Cancion> discografia;
    private ArrayList<Album> albumes;
    private int oyentesMensuales;
    private boolean verificado;
    private String biografia;

    // CONSTRUCTOR A
    public Artista(String nombreArtistico, String nombreReal, String paisOrigen) {
        //   inicializar el ID! Si no, hashCode() y equals() darán error.
        this.id = UUID.randomUUID().toString();

        this.nombreArtistico = nombreArtistico;
        this.nombreReal = nombreReal;
        this.paisOrigen = paisOrigen;

        this.discografia = new ArrayList<>();
        this.albumes = new ArrayList<>();
        this.oyentesMensuales = 0;
        this.verificado = false; //
        this.biografia = "";
    }

    // CONSTRUCTOR B
    public Artista(String nombreArtistico, String nombreReal, String paisOrigen, boolean verificado, String biografia) {
        this(nombreArtistico, nombreReal, paisOrigen);


        this.verificado = verificado;
        this.biografia = biografia;
    }

    // GET Y SETTERS
    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getNombreArtistico() { return nombreArtistico; }
    public void setNombreArtistico(String nombreArtistico) { this.nombreArtistico = nombreArtistico; }

    public String getNombreReal() { return nombreReal; }
    public void setNombreReal(String nombreReal) { this.nombreReal = nombreReal; }

    public String getPaisOrigen() { return paisOrigen; }
    public void setPaisOrigen(String paisOrigen) { this.paisOrigen = paisOrigen; }

    // "Copia defensiva"
    public ArrayList<Cancion> getDiscografia() { return new ArrayList<>(discografia); }
    public void setDiscografia(ArrayList<Cancion> discografia) { this.discografia = discografia; }

    public ArrayList<Album> getAlbumes() { return new ArrayList<>(albumes); }
    public void setAlbumes(ArrayList<Album> albumes) { this.albumes = albumes; }

    public int getOyentesMensuales() { return oyentesMensuales; }
    public void setOyentesMensuales(int oyentesMensuales) { this.oyentesMensuales = oyentesMensuales; }

    public boolean isVerificado() { return verificado; } // Getter estándar de boolean
    public void setVerificado(boolean verificado) { this.verificado = verificado; }

    public String getBiografia() { return biografia; }
    public void setBiografia(String biografia) { this.biografia = biografia; }

    // METODOS

    public void publicarCancion(Cancion cancion) {
        if (!discografia.contains(cancion)) {
            discografia.add(cancion);
        }
    }

    public Album crearAlbum(String titulo, Date fecha) throws ArtistaNoVerificadoException, AlbumYaExisteException {
        if (!verificado) {
            throw new ArtistaNoVerificadoException("El artista " + getNombreArtistico() + " no esta verificado");
        }
        for (Album a : albumes) {
            if (a.getTitulo().equalsIgnoreCase(titulo)) {
                throw new AlbumYaExisteException("El album " + titulo + " ya existe");
            }
        }

        // Tienes que pasarte a ti mismo ('this') para que el album sepa de quién es.
        Album nuevo = new Album(titulo, this, fecha);

        this.albumes.add(nuevo);
        return nuevo;
    }

    public ArrayList<Cancion> obtenerTopCanciones(int cantidad) {
        ArrayList<Cancion> ordenadas = new ArrayList<>(this.discografia);

        // Ordenar de mayor a menor reproducciones
        ordenadas.sort((c1, c2) -> c2.getReproducciones() - c1.getReproducciones());

        ArrayList<Cancion> top = new ArrayList<>();
        int limite = Math.min(cantidad, ordenadas.size());

        for (int i = 0; i < limite; i++) {
            top.add(ordenadas.get(i));
        }
        return top;
    }

    public double calcularPromedioReproducciones() {
        double total = 0;
        for (Cancion c : discografia) {
            total += c.getReproducciones(); // Asegúrate de usar getReproducciones()
        }

        if (discografia.isEmpty()) {
            return 0;
        }
        return total / discografia.size();
    }

    public boolean esVerificado() {
        return verificado;
    }

    public void verificar() {
        this.verificado = true;
    }

    public int getTotalReproducciones() {
        int total = 0;

        // 1. Sumar las reproducciones de todos los álbumes
        if (this.albumes != null) {
            for (Album album : this.albumes) {
                total += album.getTotalReproducciones(); // Llama al método del álbum
            }
        }

        // 2. Opcional: Si tu artista también tiene una lista de canciones "Sueltas" (Singles)
        // sin álbum, tendrías que sumarlas también aquí. Ejemplo:
        /*
        if (this.singles != null) {
            for (Cancion single : this.singles) {
                total += single.getReproducciones();
            }
        }
        */

        return total;
    }

    public void incrementarOyentes() {
        this.oyentesMensuales++;
    }

    @Override
    public String toString() {
        return this.nombreArtistico + " (" + paisOrigen + ") - Verificado: " + (verificado ? "Si" : "No");
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Artista otro = (Artista) obj;

        return this.id.equals(otro.id);
    }
}