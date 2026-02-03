package enums;

public enum CategoriaPodcast {
    TECNOLOGIA("Tecnología", "Podcasts sobre tecnología e innovación"),
    DEPORTES("Deportes", "Podcasts deportivos"),
    COMEDIA("Comedia", "Podcasts de humor y entretenimiento"),
    TRUE_CRIME("True Crime", "Podcasts de crímenes reales"),
    EDUCACION("Educación", "Podcasts educativos"),
    NEGOCIOS("Negocios", "Podcasts de negocios y emprendimiento"),
    SALUD("Salud", "Podcasts de salud y bienestar"),
    ENTRETENIMIENTO("Entretenimiento", "Podcasts de entretenimiento general"),
    HISTORIA("Historia", "Podcasts históricos"),
    CIENCIA("Ciencia", "Podcasts científicos"),
    POLITICA("Política", "Podcasts de política y actualidad"),
    CULTURA("Cultura", "Podcasts culturales");


    //ATRIBUTOS
    private final String nombre;
    private final String descripcion;


    //CONSTRUCTOR
    CategoriaPodcast(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    //METODOS


    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString (){
        return nombre;
    }


}
