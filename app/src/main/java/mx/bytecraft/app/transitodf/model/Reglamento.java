package mx.bytecraft.app.transitodf.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import java.util.List;

import mx.bytecraft.app.transitodf.R;
import mx.bytecraft.app.transitodf.utils.JsonReader;

public class Reglamento {
    private String nombre;
    private int publicacion;
    private int articulos;
    private List<Capitulo> capitulos;

    public Reglamento(Context context){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        String reglamentoPref = sharedPref.getString(
                context.getString(R.string.pref_reg_key), context.getString(R.string.reglamento_default));

        int resourceId = context.getResources().getIdentifier(reglamentoPref, "raw", context.getPackageName());

        JsonReader reader = new JsonReader(context.getResources(), resourceId);
        Reglamento temp = reader.getObjects(Reglamento.class);

        this.nombre = temp.getNombre();
        this.publicacion = temp.getPublicacion();
        this.capitulos = temp.getCapitulos();
        this.articulos= temp.getArticulos();
    }

    public Articulo getArticulo(int numero) {
        for(Capitulo capitulo : getCapitulos()){
            for(Articulo articulo : capitulo.getArticulos()){
                if(articulo.getId() == numero){
                    return articulo;
                }
            }
        }
        return null;
    }

    public int getPublicacion() {
        return publicacion;
    }

    public void setPublicacion(int publicacion) {
        this.publicacion = publicacion;
    }

    public List<Capitulo> getCapitulos() {
        return capitulos;
    }

    public void setCapitulos(List<Capitulo> capitulos) {
        this.capitulos = capitulos;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getArticulos() {
        return articulos;
    }

    public void setArticulos(int articulos) {
        this.articulos = articulos;
    }
}
