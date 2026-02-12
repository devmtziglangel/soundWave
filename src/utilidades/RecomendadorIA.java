package utilidades;

import enums.AlgoritmoRecomendacion;
import enums.GeneroMusical;
import enums.CategoriaPodcast;
import excepciones.recomendacion.HistorialVacioException;
import excepciones.recomendacion.ModeloNoEntrenadoException;
import excepciones.recomendacion.RecomendacionException;
import interfaces.Recomendador;
import modelo.contenido.Cancion;
import modelo.contenido.Contenido;
import modelo.contenido.Podcast;
import modelo.usuarios.Usuario;

import java.util.*;

public class RecomendadorIA implements Recomendador {

    private HashMap<String, ArrayList<String>> matrizPreferencias;
    private HashMap<String, ArrayList<Contenido>> historialCompleto;
    private AlgoritmoRecomendacion algoritmo;
    private double umbralSimilitud;
    private boolean modeloEntrenado;
    private ArrayList<Contenido> catalogoReferencia;

    private static final double UMBRAL_DEFAULT = 0.6;

    public RecomendadorIA() {
        this.matrizPreferencias = new HashMap<>();
        this.historialCompleto = new HashMap<>();
        this.catalogoReferencia = new ArrayList<>();
        this.umbralSimilitud = UMBRAL_DEFAULT;
        this.modeloEntrenado = false;
        this.algoritmo = AlgoritmoRecomendacion.FILTRADO_COLABORATIVO;
    }

    public RecomendadorIA(AlgoritmoRecomendacion algoritmo) {
        this();
        this.algoritmo = algoritmo;
    }

    // --- MÉTODOS REQUERIDOS POR EL TEST (ESCENARIO 6) ---

    /**
     * Entrena el modelo guardando la referencia del catálogo y los usuarios.
     * Requerido por Test 6.1
     */
    public void entrenarModelo(ArrayList<Usuario> usuarios, ArrayList<Contenido> catalogo) {
        this.catalogoReferencia = new ArrayList<>(catalogo);
        for (Usuario u : usuarios) {
            actualizarPreferencias(u);
        }
        this.modeloEntrenado = true;
    }

    /**
     * Genera recomendaciones basadas en el historial del usuario.
     * Requerido por Test 6.2, 6.3, 6.4 y 6.10
     */
    @Override
    public ArrayList<Contenido> recomendar(Usuario usuario) throws RecomendacionException {
        if (!modeloEntrenado) {
            throw new ModeloNoEntrenadoException("El modelo no ha sido entrenado.");
        }
        if (usuario.getHistorial().isEmpty()) {
            throw new HistorialVacioException("No se pueden generar recomendaciones para un usuario sin historial.");
        }

        ArrayList<Contenido> recomendaciones = new ArrayList<>();
        ArrayList<Contenido> historial = usuario.getHistorial();

        // Lógica: Buscar contenidos del catálogo que coincidan con géneros/categorías del historial
        // pero que NO estén ya en el historial (Requisito Test 6.10)
        for (Contenido c : catalogoReferencia) {
            if (!historial.contains(c)) {
                if (esContenidoAfin(usuario, c)) {
                    recomendaciones.add(c);
                }
            }
        }
        return recomendaciones;
    }

    /**
     * Obtiene contenido similar a uno dado (por género o categoría).
     * Requerido por Test 6.5 y 6.6
     */
    @Override
    public ArrayList<Contenido> obtenerSimilares(Contenido contenido) throws RecomendacionException {
        ArrayList<Contenido> similares = new ArrayList<>();
        for (Contenido c : catalogoReferencia) {
            if (c.equals(contenido)) continue;

            if (contenido instanceof Cancion && c instanceof Cancion) {
                if (((Cancion) contenido).getGenero().equals(((Cancion) c).getGenero())) {
                    similares.add(c);
                }
            } else if (contenido instanceof Podcast && c instanceof Podcast) {
                if (((Podcast) contenido).getCategoria().equals(((Podcast) c).getCategoria())) {
                    similares.add(c);
                }
            }
        }
        return similares;
    }

    /**
     * Actualiza la matriz de preferencias del usuario.
     * Requerido por Test 6.7
     */
    public void actualizarPreferencias(Usuario usuario) {
        ArrayList<String> preferencias = new ArrayList<>();
        for (Contenido c : usuario.getHistorial()) {
            if (c instanceof Cancion) {
                preferencias.add(((Cancion) c).getGenero().toString());
            } else if (c instanceof Podcast) {
                preferencias.add(((Podcast) c).getCategoria().toString());
            }
        }
        matrizPreferencias.put(usuario.getId(), preferencias);
    }

    /**
     * Calcula la similitud entre dos usuarios (valor entre 0 y 1).
     * Requerido por Test 6.8
     */
    public double calcularSimilitud(Usuario u1, Usuario u2) {
        ArrayList<String> pref1 = matrizPreferencias.get(u1.getId());
        ArrayList<String> pref2 = matrizPreferencias.get(u2.getId());

        if (pref1 == null || pref2 == null || pref1.isEmpty() || pref2.isEmpty()) return 0.0;

        // Similitud simple basada en elementos comunes
        long comunes = pref1.stream().filter(pref2::contains).count();
        return (double) comunes / Math.max(pref1.size(), pref2.size());
    }

    /**
     * Identifica los géneros más escuchados en la plataforma.
     * Requerido por Test 6.9
     */
    public List<GeneroMusical> obtenerGenerosPopulares() {
        Map<GeneroMusical, Integer> conteo = new HashMap<>();
        for (Contenido c : catalogoReferencia) {
            if (c instanceof Cancion) {
                GeneroMusical g = ((Cancion) c).getGenero();
                conteo.put(g, conteo.getOrDefault(g, 0) + c.getReproducciones());
            }
        }

        List<GeneroMusical> lista = new ArrayList<>(conteo.keySet());
        lista.sort((g1, g2) -> conteo.get(g2).compareTo(conteo.get(g1)));
        return lista;
    }

    // --- MÉTODOS DE APOYO INTERNO ---

    private boolean esContenidoAfin(Usuario usuario, Contenido c) {
        ArrayList<String> pref = matrizPreferencias.get(usuario.getId());
        if (pref == null) return false;

        if (c instanceof Cancion) {
            return pref.contains(((Cancion) c).getGenero().toString());
        } else if (c instanceof Podcast) {
            return pref.contains(((Podcast) c).getCategoria().toString());
        }
        return false;
    }

    // --- GETTERS Y SETTERS ---
    public boolean isModeloEntrenado() { return modeloEntrenado; }
    public HashMap<String, ArrayList<String>> getMatrizPreferencias() { return matrizPreferencias; }
    public AlgoritmoRecomendacion getAlgoritmo() { return algoritmo; }
    public void setAlgoritmo(AlgoritmoRecomendacion algoritmo) { this.algoritmo = algoritmo; }
}