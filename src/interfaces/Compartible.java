package interfaces;

public interface Compartible {

    /**
     * Genera un enlace de compartir para el contenido.
     * Incrementa en 1 el contador de veces compartido.
     * @return URL de compartir con formato: https://www.soundwave.com/share/{id}{titulo}
     */
    String generarEnlaceCompartir();

    /**
     * Obtiene el número de veces que ha sido compartido el contenido.
     * @return Cantidad de veces compartido
     */
    int obtenerVecesCompartido();
}

