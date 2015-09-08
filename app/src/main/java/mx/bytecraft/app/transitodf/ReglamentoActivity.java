package mx.bytecraft.app.transitodf;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import mx.bytecraft.app.transitodf.utils.Util;

public class ReglamentoActivity extends AppCompatActivity implements
        ReglamentoFragment.OnArticuloSelected, SearchView.OnQueryTextListener, SearchView.OnCloseListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reglamento);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String subtitle = sharedPref.getString(getString(R.string.pref_title_key), getString(R.string.title_default));
        ((TextView)findViewById(R.id.subtitle_text)).setText(subtitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_reglamento, menu);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchView.setOnQueryTextListener(this);
        searchView.setIconifiedByDefault(true);
        searchView.setOnCloseListener(this);

        Intent i = getIntent();
        String mQuery = i.getStringExtra(ArticuloFragment.ARG_QUERY_ID);
        if(!TextUtils.isEmpty(mQuery)){
            searchView.onActionViewExpanded();
            searchView.setQuery(mQuery, true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_calc:
                Util.createCalcDialog(this).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onArticuloSelected(int id, int total) {
        SearchView searchView = (SearchView)findViewById(R.id.action_search);
        String query = searchView.getQuery().toString();

        if (getResources().getBoolean(R.bool.is_tablet)) {
            Bundle arguments = new Bundle();
            arguments.putInt(ArticuloFragment.ARG_ITEM_ID, id);
            arguments.putInt(ArticuloFragment.ARG_TOTAL_ID, total);
            arguments.putString(ArticuloFragment.ARG_QUERY_ID, query);

            ArticuloFragment fragment = new ArticuloFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.articulo_container, fragment)
                    .commit();

        } else {
            Intent detailIntent = new Intent(this, ArticuloActivity.class);
            detailIntent.putExtra(ArticuloFragment.ARG_ITEM_ID, id);
            detailIntent.putExtra(ArticuloFragment.ARG_TOTAL_ID, total);
            detailIntent.putExtra(ArticuloFragment.ARG_QUERY_ID, query);
            startActivity(detailIntent);
        }
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if(TextUtils.isEmpty(newText)) {
            ArticuloFragment articuloFragment = getArticuloFragment();
            if(articuloFragment == null) {
                getReglamentoFragment().resetSearchFilter();
            } else {
                articuloFragment.onClose();
            }
        } else if(newText.length() > 3){
            onQueryTextSubmit(newText);
        }

        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query){
        ArticuloFragment articuloFragment = getArticuloFragment();
        if(articuloFragment == null) {
            getReglamentoFragment().searchArticulo(query);
        } else {
            articuloFragment.onQueryTextSubmit(query);
        }
        return true;
    }

    @Override
    public boolean onClose() {
        getReglamentoFragment().resetSearchFilter();

        ArticuloFragment articuloFragment = getArticuloFragment();
        if(articuloFragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .remove(articuloFragment)
                    .commit();
        }

        return false;
    }

    private ArticuloFragment getArticuloFragment(){
        return ((ArticuloFragment)getSupportFragmentManager().findFragmentById(R.id.articulo_container));
    }

    private ReglamentoFragment getReglamentoFragment(){
        return ((ReglamentoFragment) getSupportFragmentManager().findFragmentById(R.id.articulo_list));
    }
}
