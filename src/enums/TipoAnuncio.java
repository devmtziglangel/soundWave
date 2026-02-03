package enums;

public enum TipoAnuncio {
    AUDIO(15, 0.05),
    BANNER(0, 0.02),
    VIDEO(30, 0.10);

    //ATRIBUTOS

    private final int duracionSegundos;
    private  final double costoPorImpresion;

    TipoAnuncio(int duracionSegundos, double costoPorImpresion) {
        this.duracionSegundos = duracionSegundos;
        this.costoPorImpresion = costoPorImpresion;
    }

    //METODOS

    public int getDuracionSegundos(){
        return duracionSegundos;
    }

    public double getCostoPorImpresion() {
        return costoPorImpresion;
    }

    //OVERRIDA
    @Override
    public String toString(){
        return name() + "(" + duracionSegundos + " segundos)" ;
    }
}
