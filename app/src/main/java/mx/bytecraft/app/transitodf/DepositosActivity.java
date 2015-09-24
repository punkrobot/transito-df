package mx.bytecraft.app.transitodf;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import mx.bytecraft.app.transitodf.model.Deposito;
import mx.bytecraft.app.transitodf.utils.JsonReader;

public class DepositosActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener {

    private HashMap<String, Deposito> mDepositosHashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_depositos);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.title_activity_depositos));
        setSupportActionBar(toolbar);

        TextView infoTxt = (TextView)findViewById(R.id.depositos_info_txt);
        infoTxt.setMovementMethod(LinkMovementMethod.getInstance());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mDepositosHashMap = new HashMap<>();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        JsonReader jsonReader = new JsonReader(getResources(), R.raw.depositos);
        List<Deposito> depositos = Arrays.asList(jsonReader.getObjects(Deposito[].class));

        for(Deposito deposito : depositos){
            LatLng sydney = new LatLng(deposito.getLatitud(), deposito.getLongitud());
            Marker marker = googleMap.addMarker(
                    new MarkerOptions()
                            .position(sydney)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                            .title(deposito.getNombre())
            );
            mDepositosHashMap.put(marker.getId(), deposito);
        }

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(19.386068, -99.121078), 11));
        googleMap.setOnMarkerClickListener(this);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Deposito deposito = mDepositosHashMap.get(marker.getId());

        String msg = "<b>Dirección: </b> " + deposito.getDireccion() + ", " + deposito.getDelegacion();
        if(!TextUtils.isEmpty(deposito.getTelefono())){
            msg += "<br><br><b>Teléfono:</b> " + "<a href='tel:"+ deposito.getTelefono() + "'>" + deposito.getTelefono() + "</a>";
        }

        new MaterialDialog.Builder(this)
                .title(deposito.getNombre())
                .content(Html.fromHtml(msg))
                .negativeText(R.string.dialog_close)
                .show();

        return false;
    }
}
