package enums;

public enum AlgoritmoRecomendacion {
    COLABORATIVO("Basado en usuarios similares"),
    CONTENIDO("Basado en características del contenido"),
    HIBRIDO("Combinación de ambos");

    private final String descripcion;

    AlgoritmoRecomendacion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    //OVERRIDE

    @Override
    public String toString(){
        return name() + "(" +  descripcion +")";

    }
}
