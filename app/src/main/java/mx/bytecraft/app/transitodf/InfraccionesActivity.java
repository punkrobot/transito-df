package mx.bytecraft.app.transitodf;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import mx.bytecraft.app.transitodf.adapter.InfraccionesAdapter;
import mx.bytecraft.app.transitodf.model.ConsultaInfracciones;
import mx.bytecraft.app.transitodf.service.BusProvider;
import mx.bytecraft.app.transitodf.utils.Util;

public class InfraccionesActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView mRecyclerView;
    private MaterialDialog mDialog;
    private EditText mPlacasTxt;
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

        findViewById(R.id.inf_buscar_btn).setOnClickListener(this);
        mPlacasTxt = (EditText)findViewById(R.id.inf_placas_txt);

        mPlacasTxt.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    buscar();
                    return true;
                }
                return false;
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onResume() {
        super.onResume();

        getBus().register(this);
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

    @Override
    public void onClick(View view) {
        buscar();
    }

    @Subscribe
    public void onInfraccionesResponse(ConsultaInfracciones consulta) {
        TextView emptyTxt = (TextView)findViewById(R.id.infracciones_empty_view);

        if(!consulta.getConsulta().getInfracciones().isEmpty()) {
            mRecyclerView.setAdapter(new InfraccionesAdapter(this, consulta.getConsulta().getInfracciones()));
            mRecyclerView.setVisibility(View.VISIBLE);
            emptyTxt.setVisibility(View.GONE);
        } else {
            mRecyclerView.setVisibility(View.GONE);
            emptyTxt.setVisibility(View.VISIBLE);
            emptyTxt.setText(getString(R.string.infracciones_empty_text));
        }

        if(mDialog != null) {
            mDialog.cancel();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        getBus().unregister(this);
    }

    private void buscar(){
        getBus().post(mPlacasTxt.getText().toString());

        mPlacasTxt.clearFocus();

        mDialog = new MaterialDialog.Builder(this)
            .title(R.string.infracciones_buscando_text)
            .content(getString(R.string.infracciones_buscando_placa) + mPlacasTxt.getText().toString())
            .progress(true, 0)
            .show();
    }

    private Bus getBus() {
        if (mBus == null) {
            mBus = BusProvider.getInstance();
        }
        return mBus;
    }
}
