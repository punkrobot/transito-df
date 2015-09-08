package mx.bytecraft.app.transitodf;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.afollestad.materialdialogs.MaterialDialog;

import mx.bytecraft.app.transitodf.utils.Util;

public class ArticuloActivity extends AppCompatActivity {

    private int mArticuloId;
    private int mTotal;

    private SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articulo);

        mArticuloId = getIntent().getIntExtra(ArticuloFragment.ARG_ITEM_ID, 0);
        mTotal = getIntent().getIntExtra(ArticuloFragment.ARG_TOTAL_ID, 0);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final MaterialDialog dialog = Util.createCalcDialog(this);

        ImageView calcButton = (ImageView) findViewById(R.id.calc_button);
        calcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });

        findViewById(R.id.prev_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mArticuloId - 1 > 0) {
                    createOrReplaceFragment(--mArticuloId, false);
                }
            }
        });

        findViewById(R.id.next_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mArticuloId + 1 <= mTotal) {
                    createOrReplaceFragment(++mArticuloId, false);
                }
            }
        });

        updateNav();

        if (savedInstanceState == null) {
            createOrReplaceFragment(mArticuloId, true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_reglamento, menu);

        mSearchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));

        ArticuloFragment fragment = (ArticuloFragment)getSupportFragmentManager().findFragmentById(R.id.articulo_container);
        mSearchView.setOnQueryTextListener(fragment);
        mSearchView.setOnCloseListener(fragment);
        mSearchView.setIconifiedByDefault(true);

        String query = getIntent().getStringExtra(ArticuloFragment.ARG_QUERY_ID);
        if(!TextUtils.isEmpty(query)){
            mSearchView.onActionViewExpanded();
            mSearchView.setQuery(query, true);
            fragment.onQueryTextSubmit(query);
        }

        return true;
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

    private void createOrReplaceFragment(int id, boolean create){
        Bundle arguments = new Bundle();
        arguments.putInt(ArticuloFragment.ARG_ITEM_ID, id);
        arguments.putString(ArticuloFragment.ARG_QUERY_ID, getIntent().getStringExtra(ArticuloFragment.ARG_QUERY_ID));

        ArticuloFragment fragment = new ArticuloFragment();
        fragment.setArguments(arguments);
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        if(create) {
            t.add(R.id.articulo_container, fragment);
        } else {
            t.replace(R.id.articulo_container, fragment);
            mSearchView.setOnQueryTextListener(fragment);
            mSearchView.setOnCloseListener(fragment);
            mSearchView.setQuery("", false);
            mSearchView.onActionViewCollapsed();
        }
        t.commit();

        updateNav();
    }

    private void updateNav(){
        findViewById(R.id.prev_button).setVisibility(mArticuloId - 1 > 0 ? View.VISIBLE : View.GONE);
        findViewById(R.id.next_button).setVisibility(mArticuloId + 1 <= mTotal ? View.VISIBLE : View.GONE);
    }

}
