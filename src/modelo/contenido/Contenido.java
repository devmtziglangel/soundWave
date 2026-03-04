package modelo.contenido;

import excepciones.contenido.ContenidoNoDisponibleException;
import excepciones.contenido.DuracionInvalidaException;
import interfaces.Compartible;


import java.util.ArrayList;
import java.util.Date;

import java.util.UUID;

public abstract  class Contenido implements Compartible {
    //ATRIBUTOS
    protected String id;
    protected String titulo;
    protected int reproducciones;
    protected int likes;
    protected  int duracionSegundos;
    protected ArrayList<String> tags;
    protected boolean disponible;
    protected Date fechaPublicacion;




    protected int vecesCompartido;

    //CONSTRUCTOR

    public Contenido(String titulo, int duracionSegundos) throws DuracionInvalidaException {

        if(duracionSegundos<=0) throw new DuracionInvalidaException("La duracion debe de ser positiva");

        this.id = UUID.randomUUID().toString();
        this.titulo = titulo;
        this.reproducciones = 0;
        this.likes = 0;
        this.duracionSegundos = duracionSegundos;
        this.tags = new ArrayList<>();
        this.disponible = true; //Iniciarlo en true o en false;
        this.fechaPublicacion = new Date();
        this.vecesCompartido = 0;

    }

    //GETs Y SETTERs


    public String getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getReproducciones(){
        return reproducciones;
    }

    public void setReproducciones(int reproducciones){
        this.reproducciones = reproducciones;
    }

    public int getLikes(){
        return likes;
    }

    public int getDuracionSegundos(){
        return duracionSegundos;
    }

    public ArrayList<String>getTags(){  // defensivo
        return new ArrayList<>(tags);
    }

    public boolean isDisponible() {
        return disponible;
    }

    public Date getFechaPublicacion(){
        return fechaPublicacion;
    }

    public void setFechaPublicacion(Date fechaPublicacion){
         this.fechaPublicacion = fechaPublicacion;
    }

    public int getVecesCompartido() {
        return vecesCompartido;
    }

    public void setVecesCompartido(int vecesCompartido) {
        this.vecesCompartido = vecesCompartido;
    }

    //MÉTODOS DE LA INTERFAZ COMPARTIBLE

    @Override
    public String generarEnlaceCompartir() {
        this.vecesCompartido++;
        return "https://www.soundwave.com/share/" + this.id + this.titulo;
    }

    @Override
    public int obtenerVecesCompartido() {
        return this.vecesCompartido;
    }

    //METODO Abstracto
    public abstract void reproducir() throws ContenidoNoDisponibleException;

    //METODO CONCRETO

    public void aumentarReproducciones(){
        this.reproducciones++;
    }

    public void agregarLike (){
        this.likes++;
    }

    public boolean esPopular(){
        return reproducciones>100000;
    }

    public  void validarDuracion() throws DuracionInvalidaException{
        if (this.duracionSegundos<=0){
            throw new DuracionInvalidaException("Duracion invalida " + this.duracionSegundos);
        }
    }

    public void agregarTags(String tag) {
        if (tag != null && !tags.contains(tag)){
           this.tags.add(tag);
        }
    }

    public boolean tieneTag (String tag){
        return tags.contains(tag);
    }

    public void  marcarNoDisponible(){
        this.disponible = false;
    }

    public void  marcarDisponible(){
        this.disponible = true;
    }

    public String getDuracionFormateada(){
        int min = duracionSegundos /60;
        int seg = duracionSegundos %60;
        return String.format("%d:%02d", min, seg);
    }

    //OVERRIDES

    @Override
    public String toString() {
        return this.titulo + " (" + getDuracionFormateada() + ")";
    }

    @Override
    public int hashCode(){
        return this.id.hashCode();
    }

    @Override
    public boolean equals (Object obj){
        if(this == obj ){
            return true;
        }

        if (obj == null || getClass() !=obj.getClass()){
            return  false;
        }
        Contenido otro = (Contenido) obj;
        return  this.id.equals(otro.id);
    }



}

