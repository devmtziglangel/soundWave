package modelo.usuarios;


import enums.TipoSuscripcion;
import excepciones.contenido.ContenidoNoDisponibleException;
import excepciones.descarga.ContenidoYaDescargadoException;
import excepciones.descarga.LimiteDescargasException;
import excepciones.usuario.AnuncioRequeridoException;
import excepciones.usuario.EmailInvalidoException;
import excepciones.usuario.LimiteDiarioAlcanzadoException;
import excepciones.usuario.PasswordDebilException;
import modelo.contenido.Contenido;

import java.util.ArrayList;

public class UsuarioPremium  extends Usuario {
    private boolean descargasOffline;
    private  int maxDescargas;
    private ArrayList<Contenido> descargados;
    private String calidadAudio;

    private static final int MAX_DESCARGAS_DEFAULT = 100;




    //constructor padre
    public UsuarioPremium(String nombre, String email, String password, TipoSuscripcion suscripcion) throws EmailInvalidoException, PasswordDebilException {
        super(nombre, email, password, suscripcion);

        //INICIALIZAR ATRIBUTOS
        this.descargasOffline = suscripcion.isDescargasOffline();
        this.maxDescargas = MAX_DESCARGAS_DEFAULT;
        this.descargados = new ArrayList<>();
        this.calidadAudio = "Alta";


    }

    //CONSTRUCTOR HIJO
    public UsuarioPremium(String nombre, String email, String password) throws EmailInvalidoException, PasswordDebilException {
        // le ponemos los datos que nos pide el ReadMe
        // y añadiendo los del Padre
        this(nombre, email, password, TipoSuscripcion.PREMIUM);
    }

    //GET Y SETTERS


    public String getCalidadAudio() {
        return calidadAudio;
    }

    public void setCalidadAudio(String calidadAudio) {
        this.calidadAudio = calidadAudio;
    }

    public int getMaxDescargas() {
        return maxDescargas;
    }

    public void setMaxDescargas(int maxDescargas) {
        this.maxDescargas = maxDescargas;
    }

    public ArrayList<Contenido> getDescargados() {
        return descargados;
    }

    public void setDescargados(ArrayList<Contenido> descargados) {
        this.descargados = descargados;
    }

    public boolean isDescargasOffline() {
        return descargasOffline;
    }

    public void setDescargasOffline(boolean descargasOffline) {
        this.descargasOffline = descargasOffline;
    }

    //METODOS PROPIOS
    public void descargar(Contenido contenido) throws LimiteDescargasException, ContenidoYaDescargadoException{

        if(descargados.contains(contenido)) throw new ContenidoYaDescargadoException("Contenido  ya esta descargado");
        if(descargados.size()>=MAX_DESCARGAS_DEFAULT) throw new LimiteDescargasException("alcanzdo el limite");



        descargados.add(contenido);
    }

    public boolean eliminarDescarga (Contenido contenido){
        return descargados.remove(contenido);
    }

    public boolean verificarEspacioDescarga(){
        return  descargados.size() < MAX_DESCARGAS_DEFAULT;

    }
    public int getDescargasRestantes (){
        return MAX_DESCARGAS_DEFAULT - descargados.size();
    }

    public void cambiarCalidadAudio (String calidad){
        if(calidad != null && (calidad.equalsIgnoreCase("Alta") || calidad.equalsIgnoreCase("Normal") || calidad.equalsIgnoreCase("Baja"))){
            this.calidadAudio = calidad;
        }
    }

    public void limpiarDescargas(){
        if(!descargados.isEmpty()){
            descargados.clear();
        }
    }


    public int getNumDescargados() {
        return descargados.size();
    }


    //METODO DEL PADRE
    @Override
    public void reproducir(Contenido contenido) throws ContenidoNoDisponibleException, LimiteDiarioAlcanzadoException, AnuncioRequeridoException {

        contenido.reproducir();

        agregarAlHistorial(contenido);

    }

    //METODO OVERRIDE

    @Override
    public String toString() {
        return super.toString() +
                " | Calidad: " + this.calidadAudio;
    }



}
