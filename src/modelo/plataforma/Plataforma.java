package modelo.plataforma;

import enums.CategoriaPodcast;
import enums.GeneroMusical;
import enums.TipoAnuncio;
import enums.TipoSuscripcion;
import excepciones.artista.AlbumCompletoException;
import excepciones.artista.AlbumYaExisteException;
import excepciones.artista.ArtistaNoVerificadoException;
import excepciones.artista.LimiteEpisodiosException;
import excepciones.contenido.DuracionInvalidaException;
import excepciones.plataforma.ArtistaNoEncontradoException;
import excepciones.plataforma.ContenidoNoEncontradoException;
import excepciones.plataforma.UsuarioYaExisteException;
import excepciones.usuario.EmailInvalidoException;
import excepciones.usuario.PasswordDebilException;
import modelo.artistas.Album;
import modelo.artistas.Artista;
import modelo.artistas.Creador;
import modelo.contenido.Cancion;
import modelo.contenido.Contenido;
import modelo.contenido.Podcast;
import modelo.usuarios.Usuario;
import modelo.usuarios.UsuarioGratuito;
import modelo.usuarios.UsuarioPremium;
import utilidades.RecomendadorIA;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

/**
 * Clase principal que gestiona todo el sistema de la plataforma SoundWave.
 * Implementa el patrón Singleton para garantizar una única instancia.
 * Es la única clase que gestiona las colecciones del modelo.
 */
public class Plataforma {

    // Instancia única del Singleton
    private static Plataforma instancia;

    private String nombre;
    private HashMap<String, Usuario> usuarios;
    private HashMap<String, Usuario> usuariosPorEmail;
    private ArrayList<Contenido> catalogo;
    private ArrayList<Playlist> playlistsPublicas;
    private HashMap<String, Artista> artistas;
    private HashMap<String, Creador> creadores;
    private ArrayList<Album> albumes;
    private ArrayList<Anuncio> anuncios;
    private RecomendadorIA recomendador;
    private int totalAnunciosReproducidos;

    /**
     * Constructor privado de Plataforma (patrón Singleton).
     */
    private Plataforma(String nombre) {
        this.nombre = nombre;
        this.usuarios = new HashMap<>();
        this.usuariosPorEmail = new HashMap<>();
        this.catalogo = new ArrayList<>();
        this.playlistsPublicas = new ArrayList<>();
        this.artistas = new HashMap<>();
        this.creadores = new HashMap<>();
        this.albumes = new ArrayList<>();
        this.anuncios = new ArrayList<>();
        this.recomendador = new RecomendadorIA();
        this.totalAnunciosReproducidos = 0;
        inicializarAnuncios();
    }

    /**
     * Obtiene la instancia única de la plataforma (Singleton).
     */
    public static synchronized Plataforma getInstancia(String nombre) {
        if (instancia == null) {
            instancia = new Plataforma(nombre);
        }
        return instancia;
    }

    /**
     * Obtiene la instancia única con nombre por defecto.
     */
    public static synchronized Plataforma getInstancia() {
        return getInstancia("SoundWave");
    }

    /**
     * Reinicia la instancia (útil para pruebas).
     */
    public static synchronized void reiniciarInstancia() {
        instancia = null;
    }

    private void inicializarAnuncios() {
        anuncios.add(new Anuncio("Spotify Premium", TipoAnuncio.AUDIO, 10000));
        anuncios.add(new Anuncio("Nike", TipoAnuncio.AUDIO, 5000));
        anuncios.add(new Anuncio("Coca-Cola", TipoAnuncio.AUDIO, 8000));
        anuncios.add(new Anuncio("Apple Music", TipoAnuncio.AUDIO, 7000));
        anuncios.add(new Anuncio("Samsung", TipoAnuncio.VIDEO, 15000));
    }

    // ==================== GESTIÓN DE USUARIOS ====================

    /**
     * Registra un nuevo usuario Premium.
     */
    public UsuarioPremium registrarUsuarioPremium(String nombre, String email, String password, TipoSuscripcion tipo)
            throws UsuarioYaExisteException, EmailInvalidoException, PasswordDebilException {
        validarEmailUnico(email);
        if (tipo == TipoSuscripcion.GRATUITO) {
            tipo = TipoSuscripcion.PREMIUM;
        }
        UsuarioPremium usuario = new UsuarioPremium(nombre, email, password, tipo);
        usuarios.put(usuario.getId(), usuario);
        usuariosPorEmail.put(email.toLowerCase(), usuario);
        return usuario;
    }

    /**
     * Registra un nuevo usuario Premium con suscripción PREMIUM por defecto.
     */
    public UsuarioPremium registrarUsuarioPremium(String nombre, String email, String password)
            throws UsuarioYaExisteException, EmailInvalidoException, PasswordDebilException {
        return registrarUsuarioPremium(nombre, email, password, TipoSuscripcion.PREMIUM);
    }

    /**
     * Registra un nuevo usuario Gratuito.
     */
    public UsuarioGratuito registrarUsuarioGratuito(String nombre, String email, String password)
            throws UsuarioYaExisteException, EmailInvalidoException, PasswordDebilException {
        validarEmailUnico(email);
        UsuarioGratuito usuario = new UsuarioGratuito(nombre, email, password);
        usuarios.put(usuario.getId(), usuario);
        usuariosPorEmail.put(email.toLowerCase(), usuario);
        return usuario;
    }

    private void validarEmailUnico(String email) throws UsuarioYaExisteException {
        if (usuariosPorEmail.containsKey(email.toLowerCase())) {
            throw new UsuarioYaExisteException("Ya existe un usuario con este email");
        }
    }

    /**
     * Obtiene todos los usuarios premium.
     */
    public ArrayList<UsuarioPremium> getUsuariosPremium() {
        ArrayList<UsuarioPremium> lista = new ArrayList<>();
        for (Usuario u : usuarios.values()) {
            if (u instanceof UsuarioPremium) {
                lista.add((UsuarioPremium) u);
            }
        }
        return lista;
    }

    /**
     * Obtiene todos los usuarios gratuitos.
     */
    public ArrayList<UsuarioGratuito> getUsuariosGratuitos() {
        ArrayList<UsuarioGratuito> lista = new ArrayList<>();
        for (Usuario u : usuarios.values()) {
            if (u instanceof UsuarioGratuito) {
                lista.add((UsuarioGratuito) u);
            }
        }
        return lista;
    }

    /**
     * Obtiene todos los usuarios.
     */
    public ArrayList<Usuario> getTodosLosUsuarios() {
        return new ArrayList<>(usuarios.values());
    }

    /**
     * Busca usuario por email.
     */
    public Usuario buscarUsuarioPorEmail(String email) {
        return usuariosPorEmail.get(email.toLowerCase());
    }

    /**
     * Migra un usuario gratuito a una cuenta Premium.
     * Transfiere todos sus datos (playlists, historial, likes) sin pérdida de información.
     * @param usuarioGratuito El usuario gratuito a migrar
     * @return El nuevo usuario premium con todos los datos transferidos
     * @throws EmailInvalidoException Si el email no es válido
     * @throws PasswordDebilException Si la contraseña no es fuerte
     * @throws IllegalArgumentException Si el usuario no es de tipo UsuarioGratuito
     */
    public UsuarioPremium migrarUsuarioAPremium(UsuarioGratuito usuarioGratuito)
            throws EmailInvalidoException, PasswordDebilException {

        if (usuarioGratuito == null) {
            throw new IllegalArgumentException("El usuario no puede ser nulo");
        }

        // Crear el nuevo usuario premium con los mismos datos básicos
        UsuarioPremium usuarioPremium = new UsuarioPremium(
            usuarioGratuito.getNombre(),
            usuarioGratuito.getEmail(),
            usuarioGratuito.getPassword(),
            TipoSuscripcion.PREMIUM
        );

        // Transferir datos personales
        usuarioPremium.setId(usuarioGratuito.getId()); // Mantener el mismo ID
        usuarioPremium.setFechaRegistro(usuarioGratuito.getFechaRegistro()); // Mantener fecha de registro

        // Transferir historial
        usuarioPremium.setHistorial(new ArrayList<>(usuarioGratuito.getHistorial()));

        // Transferir playlists
        usuarioPremium.setMisPlaylists(new ArrayList<>(usuarioGratuito.getMisPlaylists()));

        // Transferir playlists seguidas
        usuarioPremium.setPlaylistsSeguidas(new ArrayList<>(usuarioGratuito.getPlaylistsSeguidas()));

        // Transferir contenidos likeados
        usuarioPremium.setContenidosLiked(new ArrayList<>(usuarioGratuito.getContenidosLiked()));

        // Ahora actualizar los HashMaps
        // Primero eliminar el usuario gratuito
        usuarios.remove(usuarioGratuito.getId());
        usuariosPorEmail.remove(usuarioGratuito.getEmail().toLowerCase());

        // Después agregar el nuevo usuario premium
        usuarios.put(usuarioPremium.getId(), usuarioPremium);
        usuariosPorEmail.put(usuarioPremium.getEmail().toLowerCase(), usuarioPremium);

        return usuarioPremium;
    }


    // ==================== GESTIÓN DE ARTISTAS ====================

    /**
     * Registra un nuevo artista.
     */
    public Artista registrarArtista(String nombreArtistico, String nombreReal, String paisOrigen, boolean verificado) {
        Artista artista = new Artista(nombreArtistico, nombreReal, paisOrigen, verificado, "");
        artistas.put(artista.getId(), artista);
        return artista;
    }

    /**
     * Registra un artista existente.
     */
    public void registrarArtista(Artista artista) {
        if (artista != null && !artistas.containsKey(artista.getId())) {
            artistas.put(artista.getId(), artista);
        }
    }

    /**
     * Obtiene todos los artistas verificados.
     */
    public ArrayList<Artista> getArtistasVerificados() {
        ArrayList<Artista> lista = new ArrayList<>();
        for (Artista a : artistas.values()) {
            if (a.isVerificado()) {
                lista.add(a);
            }
        }
        return lista;
    }

    /**
     * Obtiene todos los artistas no verificados.
     */
    public ArrayList<Artista> getArtistasNoVerificados() {
        ArrayList<Artista> lista = new ArrayList<>();
        for (Artista a : artistas.values()) {
            if (!a.isVerificado()) {
                lista.add(a);
            }
        }
        return lista;
    }

    /**
     * Busca un artista por nombre.
     */
    public Artista buscarArtista(String nombre) throws ArtistaNoEncontradoException {
        String nombreLower = nombre.toLowerCase();
        for (Artista artista : artistas.values()) {
            if (artista.getNombreArtistico().toLowerCase().contains(nombreLower) ||
                artista.getNombreReal().toLowerCase().contains(nombreLower)) {
                return artista;
            }
        }
        throw new ArtistaNoEncontradoException("No se encontró el artista");
    }

    // ==================== GESTIÓN DE ÁLBUMES ====================

    /**
     * Crea un nuevo álbum para un artista.
     */
    public Album crearAlbum(Artista artista, String titulo, Date fecha)
            throws ArtistaNoVerificadoException, AlbumYaExisteException {
        Album album = artista.crearAlbum(titulo, fecha);
        albumes.add(album);
        return album;
    }

    /**
     * Obtiene todos los álbumes.
     */
    public ArrayList<Album> getAlbumes() {
        return new ArrayList<>(albumes);
    }

    // ==================== GESTIÓN DE CANCIONES ====================

    /**
     * Crea y registra una nueva canción independiente (sin álbum).
     */
    public Cancion crearCancion(String titulo, int duracion, Artista artista, GeneroMusical genero)
            throws DuracionInvalidaException {
        Cancion cancion = new Cancion(titulo, duracion, artista, genero);
        catalogo.add(cancion);
        if (artista != null) {
            artista.publicarCancion(cancion);
        }
        return cancion;
    }

    /**
     * Crea una canción dentro de un álbum (COMPOSICIÓN).
     * La creación se delega al álbum, que es el dueño de las canciones.
     */
    public Cancion crearCancionEnAlbum(String titulo, int duracion, Artista artista, GeneroMusical genero, Album album)
            throws DuracionInvalidaException, AlbumCompletoException {
        // Delegar la creación al álbum (composición)
        Cancion cancion = album.crearCancion(titulo, duracion, genero);
        catalogo.add(cancion);
        return cancion;
    }

    /**
     * Agrega contenido al catálogo de la plataforma.
     * @param contenido Contenido a agregar
     */
    public void agregarContenidoCatalogo(Contenido contenido) {
        if (contenido != null && !catalogo.contains(contenido)) {
            catalogo.add(contenido);
        }
    }

    /**
     * Obtiene todas las canciones del catálogo.
     */
    public ArrayList<Cancion> getCanciones() {
        ArrayList<Cancion> lista = new ArrayList<>();
        for (Contenido c : catalogo) {
            if (c instanceof Cancion) {
                lista.add((Cancion) c);
            }
        }
        return lista;
    }

    // ==================== GESTIÓN DE CREADORES Y PODCASTS ====================

    /**
     * Registra un nuevo creador de podcasts.
     */
    public Creador registrarCreador(String nombreCanal, String nombre, String descripcion) {
        Creador creador = new Creador(nombreCanal, nombre, descripcion);
        creadores.put(creador.getId(), creador);
        return creador;
    }

    /**
     * Registra un creador existente.
     */
    public void registrarCreador(Creador creador) {
        if (creador != null && !creadores.containsKey(creador.getId())) {
            creadores.put(creador.getId(), creador);
        }
    }

    /**
     * Crea y registra un nuevo podcast.
     */
    public Podcast crearPodcast(String titulo, int duracion, Creador creador, int numEpisodio, int temporada, CategoriaPodcast categoria)
            throws DuracionInvalidaException, LimiteEpisodiosException {
        Podcast podcast = new Podcast(titulo, duracion, creador, numEpisodio, temporada, categoria);
        creador.publicarPodcast(podcast);
        catalogo.add(podcast);
        return podcast;
    }

    /**
     * Obtiene todos los podcasts del catálogo.
     */
    public ArrayList<Podcast> getPodcasts() {
        ArrayList<Podcast> lista = new ArrayList<>();
        for (Contenido c : catalogo) {
            if (c instanceof Podcast) {
                lista.add((Podcast) c);
            }
        }
        return lista;
    }

    /**
     * Obtiene todos los creadores.
     */
    public ArrayList<Creador> getTodosLosCreadores() {
        return new ArrayList<>(creadores.values());
    }

    // ==================== GESTIÓN DE PLAYLISTS ====================

    /**
     * Crea una playlist pública.
     */
    public Playlist crearPlaylistPublica(String nombre, Usuario creador) {
        Playlist playlist = new Playlist(nombre, creador, true, "");
        playlistsPublicas.add(playlist);
        return playlist;
    }

    /**
     * Obtiene todas las playlists públicas.
     */
    public ArrayList<Playlist> getPlaylistsPublicas() {
        return new ArrayList<>(playlistsPublicas);
    }

    // ==================== BÚSQUEDAS ====================

    /**
     * Busca contenido por término.
     */
    public ArrayList<Contenido> buscarContenido(String termino) throws ContenidoNoEncontradoException {
        ArrayList<Contenido> resultados = new ArrayList<>();
        String terminoLower = termino.toLowerCase();

        for (Contenido contenido : catalogo) {
            if (contenido.getTitulo().toLowerCase().contains(terminoLower)) {
                resultados.add(contenido);
            } else if (contenido instanceof Cancion) {
                Cancion cancion = (Cancion) contenido;
                if (cancion.getArtista() != null &&
                    cancion.getArtista().getNombreArtistico().toLowerCase().contains(terminoLower)) {
                    resultados.add(contenido);
                }
            }
        }

        if (resultados.isEmpty()) {
            throw new ContenidoNoEncontradoException("No se encontró contenido");
        }
        return resultados;
    }

    /**
     * Busca canciones por género.
     */
    public ArrayList<Cancion> buscarPorGenero(GeneroMusical genero) throws ContenidoNoEncontradoException {
        ArrayList<Cancion> resultados = new ArrayList<>();
        for (Contenido c : catalogo) {
            if (c instanceof Cancion && ((Cancion) c).getGenero() == genero) {
                resultados.add((Cancion) c);
            }
        }
        if (resultados.isEmpty()) {
            throw new ContenidoNoEncontradoException("No se encontraron canciones del género " + genero.getNombre());
        }
        return resultados;
    }

    /**
     * Busca podcasts por categoría.
     */
    public ArrayList<Podcast> buscarPorCategoria(CategoriaPodcast categoria) throws ContenidoNoEncontradoException {
        ArrayList<Podcast> resultados = new ArrayList<>();
        for (Contenido c : catalogo) {
            if (c instanceof Podcast && ((Podcast) c).getCategoria() == categoria) {
                resultados.add((Podcast) c);
            }
        }
        if (resultados.isEmpty()) {
            throw new ContenidoNoEncontradoException("No se encontraron podcasts de " + categoria.getNombre());
        }
        return resultados;
    }

    /**
     * Obtiene top contenidos por reproducciones.
     */
    public ArrayList<Contenido> obtenerTopContenidos(int cantidad) {
        ArrayList<Contenido> ordenados = new ArrayList<>(catalogo);
        ordenados.sort((c1, c2) -> c2.getReproducciones() - c1.getReproducciones());
        if (cantidad >= ordenados.size()) {
            return ordenados;
        }
        return new ArrayList<>(ordenados.subList(0, cantidad));
    }

    // ==================== ANUNCIOS ====================

    /**
     * Obtiene un anuncio aleatorio.
     */
    public Anuncio obtenerAnuncioAleatorio() {
        ArrayList<Anuncio> activos = new ArrayList<>();
        for (Anuncio a : anuncios) {
            if (a.puedeMostrarse()) {
                activos.add(a);
            }
        }
        if (activos.isEmpty()) return null;
        Random random = new Random();
        Anuncio anuncio = activos.get(random.nextInt(activos.size()));
        totalAnunciosReproducidos++;
        return anuncio;
    }

    public void incrementarAnunciosReproducidos() {
        totalAnunciosReproducidos++;
    }

    // ==================== ESTADÍSTICAS ====================

    /**
     * Identifica al artista con mayor número de reproducciones totales.
     * Requerido por el Test 10.8
     */
    public Artista getArtistaMasReproducido() {
        if (artistas == null || artistas.isEmpty()) {
            return null;
        }

        Artista artistaMasPopular = null;
        int maxReproducciones = -1; // Iniciamos en -1 para manejar casos con 0 reproducciones

        for (Artista a : artistas.values()) {
            // Usamos el método getTotalReproducciones() que ya existe en la clase Artista
            int total = a.getTotalReproducciones();

            if (total > maxReproducciones) {
                maxReproducciones = total;
                artistaMasPopular = a;
            }
        }
        return artistaMasPopular;
    }

    /**
     * Obtiene estadísticas generales de la plataforma.
     */
    public String obtenerEstadisticasGenerales() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n========================\n");
        sb.append("ESTADÍSTICAS DE ").append(nombre.toUpperCase()).append("\n");
        sb.append("========================\n");

        // Usuarios
        int premiumCount = getUsuariosPremium().size();
        int gratuitoCount = getUsuariosGratuitos().size();
        double ingresos = 0;
        for (Usuario u : usuarios.values()) {
            ingresos += u.getSuscripcion().getPrecioMensual();
        }

        sb.append("Total Usuarios: ").append(usuarios.size())
          .append(" (").append(premiumCount).append(" Premium, ")
          .append(gratuitoCount).append(" Gratuitos)\n");

        // Contenido
        int canciones = getCanciones().size();
        int podcasts = getPodcasts().size();
        sb.append("Total Contenido: ").append(catalogo.size())
          .append(" (").append(canciones).append(" canciones, ")
          .append(podcasts).append(" podcasts)\n");

        // Artista más popular
        Artista artistaMasPopular = null;
        int maxRepros = 0;
        for (Artista a : artistas.values()) {
            int repros = a.getTotalReproducciones();
            if (repros > maxRepros) {
                maxRepros = repros;
                artistaMasPopular = a;
            }
        }
        if (artistaMasPopular != null) {
            sb.append("Artista más popular: ").append(artistaMasPopular.getNombreArtistico())
              .append(" (").append(maxRepros).append(" reproducciones)\n");
        }

        // Creador más popular
        Creador creadorMasPopular = null;
        int maxSubs = 0;
        for (Creador c : creadores.values()) {
            if (c.getSuscriptores() > maxSubs) {
                maxSubs = c.getSuscriptores();
                creadorMasPopular = c;
            }
        }
        if (creadorMasPopular != null) {
            sb.append("Creador más popular: ").append(creadorMasPopular.getNombreCanal())
              .append(" (").append(maxSubs).append(" suscriptores)\n");
        }

        // Género más popular
        HashMap<GeneroMusical, Integer> generoCount = new HashMap<>();
        for (Cancion c : getCanciones()) {
            GeneroMusical g = c.getGenero();
            generoCount.put(g, generoCount.getOrDefault(g, 0) + c.getReproducciones());
        }
        GeneroMusical generoTop = null;
        int maxGenero = 0;
        for (GeneroMusical g : generoCount.keySet()) {
            if (generoCount.get(g) > maxGenero) {
                maxGenero = generoCount.get(g);
                generoTop = g;
            }
        }
        if (generoTop != null) {
            sb.append("Género más escuchado: ").append(generoTop.getNombre()).append("\n");
        }

        // Categoría más popular
        HashMap<CategoriaPodcast, Integer> catCount = new HashMap<>();
        for (Podcast p : getPodcasts()) {
            CategoriaPodcast cat = p.getCategoria();
            catCount.put(cat, catCount.getOrDefault(cat, 0) + p.getReproducciones());
        }
        CategoriaPodcast catTop = null;
        int maxCat = 0;
        for (CategoriaPodcast cat : catCount.keySet()) {
            if (catCount.get(cat) > maxCat) {
                maxCat = catCount.get(cat);
                catTop = cat;
            }
        }
        if (catTop != null) {
            sb.append("Categoría podcast más popular: ").append(catTop.getNombre()).append("\n");
        }

        sb.append("Ingresos mensuales: $").append(String.format("%.2f", ingresos)).append("\n");
        sb.append("Total anuncios reproducidos: ").append(totalAnunciosReproducidos).append("\n");
        sb.append("========================\n");

        return sb.toString();
    }



    // ==================== GETTERS BÁSICOS ====================

    public String getNombre() {
        return nombre;
    }

    public ArrayList<Contenido> getCatalogo() {
        return new ArrayList<>(catalogo);
    }

    public HashMap<String, Artista> getArtistas() {
        return new HashMap<>(artistas);
    }

    public HashMap<String, Creador> getCreadores() {
        return new HashMap<>(creadores);
    }

    public ArrayList<Anuncio> getAnuncios() {
        return new ArrayList<>(anuncios);
    }

    public RecomendadorIA getRecomendador() {
        return recomendador;
    }

    public int getTotalUsuarios() {
        return usuarios.size();
    }

    public int getTotalContenido() {
        return catalogo.size();
    }

    public int getTotalAnunciosReproducidos() {
        return totalAnunciosReproducidos;
    }


    @Override
    public String toString() {
        return nombre + " - " + usuarios.size() + " usuarios, " + catalogo.size() + " contenidos";
    }
}
