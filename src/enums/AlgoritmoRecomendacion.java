package enums;

public enum AlgoritmoRecomendacion {
    FILTRADO_COLABORATIVO("Basado en usuarios similares"), // Antes COLABORATIVO
    BASADO_EN_CONTENIDO("Basado en características del contenido"), // Antes CONTENIDO
    HIBRIDO("Combinación de ambos");

    private final String descripcion;

    AlgoritmoRecomendacion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString(){
        return name() + " (" +  descripcion +")";
    }
}