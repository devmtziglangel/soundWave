package modelo.usuarios;

import java.util.ArrayList;
import java.util.Date;

public  abstract class Usuario {
    protected String id;
    protected  String nombre;
    protected String email;
    protected  String password;
    protected TipoSuscripcion suscripcion;
    protected ArrayList<Playlist>misPlaylists;
    protected ArrayList<Contenido>historial;
    protected Date fechaRegistro;


    
}
