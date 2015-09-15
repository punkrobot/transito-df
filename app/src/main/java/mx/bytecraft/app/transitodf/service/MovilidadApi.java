package mx.bytecraft.app.transitodf.service;

import mx.bytecraft.app.transitodf.model.ConsultaInfracciones;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

public interface MovilidadApi {

    @GET("/movilidad/vehiculos/{placa}.json")
    void listInfracciones(@Path("placa") String placa, Callback<ConsultaInfracciones> response);

}
