package mx.bytecraft.app.transitodf;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import mx.bytecraft.app.transitodf.model.Reglamento;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            SettingsFragment fragment = new SettingsFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.settings_container, fragment)
                    .commit();
        }

        final View view = findViewById(R.id.coordinator);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.OnSharedPreferenceChangeListener prefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                if(key.equals(getString(R.string.pref_reg_key))){
                    Reglamento reglamento = new Reglamento(getApplicationContext());
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString(getString(R.string.pref_title_key), reglamento.getNombre());
                    editor.apply();
                }

                if(key.equals(getString(R.string.pref_reg_key)) || key.equals(getString(R.string.pref_tab_key))) {
                    Snackbar.make(view, "La configuración se actualizó correctamente", Snackbar.LENGTH_LONG).show();
                }
            }
        };
        prefs.registerOnSharedPreferenceChangeListener(prefListener);
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle bundle, String s) {
            addPreferencesFromResource(R.xml.settings);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}