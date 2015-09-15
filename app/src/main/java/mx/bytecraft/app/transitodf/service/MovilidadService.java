package mx.bytecraft.app.transitodf.service;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import mx.bytecraft.app.transitodf.model.ConsultaInfracciones;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MovilidadService {
    private MovilidadApi mApi;
    private Bus mBus;

    public MovilidadService(MovilidadApi api, Bus bus) {
        mApi = api;
        mBus = bus;
    }

    @Subscribe
    public void onCargaInfracciones(String placa) {

        mApi.listInfracciones(placa, new Callback<ConsultaInfracciones>(){
            @Override
            public void success(ConsultaInfracciones consultaInfracciones, Response response) {
                mBus.post(consultaInfracciones);
            }
            @Override
            public void failure(RetrofitError error) {
                mBus.post(error);
            }
        });
    }
}

