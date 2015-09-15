package mx.bytecraft.app.transitodf;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import mx.bytecraft.app.transitodf.adapter.InfraccionesAdapter;
import mx.bytecraft.app.transitodf.model.ConsultaInfracciones;
import mx.bytecraft.app.transitodf.service.BusProvider;
import mx.bytecraft.app.transitodf.utils.Util;

public class InfraccionesActivity extends AppCompatActivity {

    private InfraccionesAdapter mInfraccionesAdapter;
    private RecyclerView mRecyclerView;
    private Bus mBus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infracciones);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.title_activity_infracciones));
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.infracciones_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onResume() {
        super.onResume();

        getBus().register(this);
        getBus().post("436per");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_infracciones, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_calc:
                Util.createCalcDialog(this).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Subscribe
    public void onInfraccionesResponse(ConsultaInfracciones consulta) {
        mInfraccionesAdapter = new InfraccionesAdapter(this, consulta.getConsulta().getInfracciones());
        mRecyclerView.setAdapter(mInfraccionesAdapter);
    }

    @Override
    public void onPause() {
        super.onPause();

        getBus().unregister(this);
    }

    private Bus getBus() {
        if (mBus == null) {
            mBus = BusProvider.getInstance();
        }
        return mBus;
    }

}
