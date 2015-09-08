package mx.bytecraft.app.transitodf.model;

import java.util.ArrayList;
import java.util.List;

public class Capitulo {
    private String capitulo;
    private List<Articulo> articulos;

    public Capitulo(String capitulo){
        this.capitulo = capitulo;
    }

    public String getCapitulo() {
        return capitulo;
    }
    public void setCapitulo(String capitulo) {
        this.capitulo = capitulo;
    }

    public List<Articulo> getArticulos() {
        return articulos;
    }
    public void setArticulos(ArrayList<Articulo> articulos) {
        this.articulos = articulos;
    }
}
