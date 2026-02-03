package modelo.contenido;

import excepciones.contenido.DuracionInvalidaException;
import interfaces.Descargable;
import interfaces.Reproducible;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public abstract  class Contenido   implements Descargable, Reproducible {
    //ATRIBUTOS
    protected String id;
    protected String titulo;
    protected int likes;
    protected  int duracionSegundos;
    protected ArrayList<String> tags;
    protected boolean disponiles;
    protected Date fechaPublicacion;

    //CONSTRUCTOR

    public Contenido(String titulo, int duracionSegundos) throws DuracionInvalidaException {

        if(duracionSegundos<=0) throw new DuracionInvalidaException("La duracion debe de ser positiva");

        this.id = UUID.randomUUID().toString();
        this.titulo = titulo;
        this.likes
        this.duracionSegundos = duracionSegundos;

    }
}
