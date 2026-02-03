package enums;

public enum CriterioOrden {
    FECHA_AGREGADO("Fecha de agregado", "Ordena por fecha en que se agregó"),
    POPULARIDAD("Popularidad", "Ordena por número de reproducciones"),
    DURACION("Duración", "Ordena por duración del contenido"),
    ALFABETICO("Alfabético", "Ordena alfabéticamente por título"),
    ARTISTA("Artista", "Ordena por nombre del artista"),
    ALEATORIO("Aleatorio", "Orden aleatorio");

    //ATRIBUTOS
    private final String nombre;
    private final String descripcion;

    //CONSTRUCTOR

    CriterioOrden(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }


    //METODOS

    public String getNombre(){
        return  nombre;
    }

    public String getDescripcion(){
        return descripcion;
    }

    //OVERRIDE

    @Override
    public String toString() {
        return  nombre;
    }
}
