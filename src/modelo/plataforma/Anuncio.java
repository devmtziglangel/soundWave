package modelo.plataforma;

import enums.TipoAnuncio;
import modelo.artistas.Creador;

import java.util.Objects;
import java.util.UUID;

public class Anuncio {
    private String id;
    private String empresa;
    private int duracionSegundos;
    private String audioURL;
    private TipoAnuncio tipo;
    private int impresiones;
    private double presupuesto;
    private  boolean activo;

    //CONSTRUCTOR A

    public Anuncio(String empresa, TipoAnuncio tipo, double presupuesto) {

        this.empresa = empresa;
        this.tipo = tipo;
        this.presupuesto = presupuesto;

        this.id = UUID.randomUUID().toString();

        this.duracionSegundos=0;
        this.audioURL=null;
        this.impresiones = 0;

        this.activo = true;
    }
    //CONSTRUCTOR B
    public Anuncio(String empresa, TipoAnuncio tipo, double presupuesto, String audioURL) {
        this(empresa, tipo, presupuesto);
        this.audioURL = audioURL;

    }

    //GETT Y SETTERS

    public String getId() {
        return id;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public int getDuracionSegundos() {
        return duracionSegundos;
    }

    public void setDuracionSegundos(int duracionSegundos) {
        this.duracionSegundos = duracionSegundos;
    }

    public String getAudioURL() {
        return audioURL;
    }

    public void setAudioURL(String audioURL) {
        this.audioURL = audioURL;
    }

    public TipoAnuncio getTipo() {
        return tipo;
    }

    public void setTipo(TipoAnuncio tipo) {
        this.tipo = tipo;
    }

    public int getImpresiones() {
        return impresiones;
    }

    public void setImpresiones(int impresiones) {
        this.impresiones = impresiones;
    }

    public double getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(double presupuesto) {
        this.presupuesto = presupuesto;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

     //METODOS

    public void reproducir(){
        if(isActivo()){
            System.out.println("Reproduciendo anuncio");
        }
    }

    public void registrarImpresion(){
        this.impresiones ++;

        double dineroGastado = this.impresiones * tipo.getCostoPorImpresion();

        if(dineroGastado >= this.presupuesto){
            this.activo = false;
        }


    }

    public double calcularCostoPorImpresion(){
        return tipo.getCostoPorImpresion();


    }

    public double calcularCostoTotal(){
        return  tipo.getCostoPorImpresion()* impresiones;
    }

    public int calcularImpresionesRestantes (){

        double costoUnitario = tipo.getCostoPorImpresion();

        double dineroGastado = this.impresiones * costoUnitario;

        double dineroRestante =  presupuesto - dineroGastado  ;

        return (int) (dineroRestante / costoUnitario);

    }

    public void activar (){
        this.activo = true;


    }

    public boolean puedeMostrarse(){  //Es activo Y le queda dinero para 1 impresión
        return isActivo() &&  calcularImpresionesRestantes()>0;
    }


    //METODOS OVERRIDE


    @Override
    public String toString() {
        return "Anuncio: " + empresa + " [ID: " + id + "] - Tipo: " + tipo +
                " - Métricas: " + impresiones + " impresiones. " +
                "Coste total: " + calcularCostoTotal() + "€ / Presupuesto: " + presupuesto + "€";
    }

    @Override
    public boolean equals(Object obj){
        if(this == obj) return true;

        if(obj == null || getClass()!= obj.getClass() ) return false;

        Anuncio otro = (Anuncio) obj;

        return this.id.equals(otro.id);



    }
    @Override
    public int hashCode(){
        return Objects.hash(id);
    }


}
