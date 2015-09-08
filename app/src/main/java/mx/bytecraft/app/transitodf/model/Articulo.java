package mx.bytecraft.app.transitodf.model;

public class Articulo{

    private int id;
    private String titulo;
    private String texto;
    private String sanciones;
    private String extracto;

    public Articulo() {}

    public Articulo(int id, String titulo, String texto, String sanciones) {
        this.id = id;
        this.titulo = titulo;
        this.texto = texto;
        this.sanciones = sanciones;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTexto() {
        return texto;
    }
    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getSanciones() {
        return sanciones;
    }
    public void setSanciones(String sanciones) {
        this.sanciones = sanciones;
    }

    public String getExtracto() {
        return extracto;
    }

    public void setExtracto(String extracto) {
        this.extracto = extracto;
    }
}