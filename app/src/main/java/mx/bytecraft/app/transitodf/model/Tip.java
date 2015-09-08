package mx.bytecraft.app.transitodf.model;

import com.google.gson.annotations.SerializedName;

public class Tip {

    private int id;
    private Tipo tipo;
    private String titulo;
    private String texto;
    private int articulo;
    private String imagen;
    private String resumen;

    public enum Tipo {
        @SerializedName("consejo") CONSEJO,
        @SerializedName("obligacion") OBLIGACION,
        @SerializedName("derecho") DERECHO,
        OTRO
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Tipo getTipo() {
        if(tipo == null) {
            tipo = Tipo.OTRO;
        }

        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
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

    public int getArticulo() {
        return articulo;
    }

    public void setArticulo(int articulo) {
        this.articulo = articulo;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getResumen() {
        return resumen;
    }

    public void setResumen(String resumen) {
        this.resumen = resumen;
    }
}
