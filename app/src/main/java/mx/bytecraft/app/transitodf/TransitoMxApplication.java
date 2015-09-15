package mx.bytecraft.app.transitodf;

import android.app.Application;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import mx.bytecraft.app.transitodf.service.BusProvider;
import mx.bytecraft.app.transitodf.service.MovilidadApi;
import mx.bytecraft.app.transitodf.service.MovilidadService;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.converter.GsonConverter;

public class TransitoMxApplication extends Application {

    private MovilidadService mMovilidadService;
    private Bus mBus = BusProvider.getInstance();

    @Override
    public void onCreate() {
        super.onCreate();

        mMovilidadService = new MovilidadService(buildApi(), mBus);
        mBus.register(mMovilidadService);

        mBus.register(this);
    }

    private MovilidadApi buildApi() {
        Gson gson = new GsonBuilder()
                .setDateFormat(getString(R.string.service_infracciones_date_format))
                .create();

        return new RestAdapter.Builder()
                .setConverter(new GsonConverter(gson))
                .setEndpoint(getString(R.string.service_infracciones_url))
                .build()
                .create(MovilidadApi.class);
    }

    @Subscribe
    public void onApiError(RetrofitError error) {
        error.printStackTrace();
    }
}