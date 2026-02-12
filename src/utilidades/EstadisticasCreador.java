package utilidades;

import modelo.artistas.Creador;
import modelo.contenido.Podcast;
import java.util.ArrayList;

/**
 * Clase encargada de procesar y exponer las métricas de desempeño de un Creador.
 * Requerida para superar los tests del Escenario 8.
 */
public class EstadisticasCreador {
    private Creador creador;

    /**
     * Constructor que vincula las estadísticas a un creador.
     * @param creador El objeto Creador del cual se obtendrán los datos.
     */
    public EstadisticasCreador(Creador creador) {
        this.creador = creador;
    }

    /**
     * Devuelve la referencia al creador asociado.
     * Requerido por Test 8.4.
     */
    public Creador getCreador() {
        return this.creador;
    }

    /**
     * Obtiene el número total de episodios publicados.
     * Requerido por Test 8.4.
     */
    public int getTotalEpisodios() {
        return (creador.getEpisodios() != null) ? creador.getEpisodios().size() : 0;
    }

    /**
     * Obtiene la cifra actual de suscriptores del canal.
     * Requerido por Test 8.4.
     */
    public int getTotalSuscriptores() {
        return creador.getSuscriptores();
    }

    /**
     * Calcula la suma de reproducciones de todos los episodios del creador.
     * Requerido por Test 8.4.
     */
    public int getTotalReproducciones() {
        return creador.getTotalReproducciones();
    }

    /**
     * Identifica el episodio que tiene el mayor número de reproducciones.
     * Requerido por Test 8.13.
     * @return El objeto Podcast con más éxito o null si no hay episodios.
     */
    public Podcast getEpisodioMasPopular() {
        ArrayList<Podcast> episodios = creador.getEpisodios();
        if (episodios == null || episodios.isEmpty()) {
            return null;
        }

        Podcast masPopular = episodios.get(0);
        for (Podcast p : episodios) {
            if (p.getReproducciones() > masPopular.getReproducciones()) {
                masPopular = p;
            }
        }
        return masPopular;
    }

    /**
     * Genera un resumen textual de las estadísticas del canal.
     * Requerido por Test 8.5.
     * @return Un String con el nombre del canal en MAYÚSCULAS y los totales.
     */
    public String generarReporte() {
        // El test 8.5 exige explícitamente que el nombre aparezca en mayúsculas
        String nombreCanal = (creador.getNombreCanal() != null)
                ? creador.getNombreCanal().toUpperCase()
                : "SIN NOMBRE";

        StringBuilder sb = new StringBuilder();
        sb.append("--- REPORTE DE ESTADÍSTICAS: ").append(nombreCanal).append(" ---\n");
        sb.append("Total de episodios publicados: ").append(getTotalEpisodios()).append("\n");
        sb.append("Comunidad de suscriptores: ").append(getTotalSuscriptores()).append("\n");
        sb.append("Impacto total (Reproducciones): ").append(getTotalReproducciones()).append("\n");

        Podcast top = getEpisodioMasPopular();
        if (top != null) {
            sb.append("Episodio más visto: ").append(top.getTitulo())
                    .append(" (").append(top.getReproducciones()).append(" reproducciones)\n");
        }
        sb.append("----------------------------------------------------------");

        return sb.toString();
    }
}