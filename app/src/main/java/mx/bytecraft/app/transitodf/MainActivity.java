package mx.bytecraft.app.transitodf;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.flipboard.bottomsheet.commons.IntentPickerSheetView;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mx.bytecraft.app.transitodf.adapter.HeaderAdapter;
import mx.bytecraft.app.transitodf.adapter.TipsAdapter;
import mx.bytecraft.app.transitodf.model.Tip;
import mx.bytecraft.app.transitodf.utils.JsonReader;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener,
        TipsAdapter.OnShareTipListener, TipsAdapter.OnHeaderClicListener {

    public static final String ARG_RESOURCE_ID = "resource";
    private BottomSheetLayout mBottomSheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ReglamentoActivity.class);
                startActivity(intent);
            }
        });

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        createTabs(tabLayout);

        mBottomSheet = (BottomSheetLayout) findViewById(R.id.bottomsheet);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        int[] resources = {R.raw.peaton, R.raw.ciclista, R.raw.automovil};
        for(int resource : resources){
            Bundle args = new Bundle();
            args.putInt(ARG_RESOURCE_ID, resource);
            TipsFragment fragment = new TipsFragment();
            fragment.setArguments(args);
            adapter.addFrag(fragment);
        }
        viewPager.setAdapter(adapter);
    }

    private void createTabs(TabLayout tabLayout){
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_directions_walk_white_24dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_directions_bike_white_24dp);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_directions_car_white_24dp);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String tabPref = sharedPref.getString(getString(R.string.pref_tab_key), getString(R.string.tab_default));
        String[] values = getResources().getStringArray(R.array.pref_tab_values);
        for(int i = 0; i < values.length ; i++ ){
            if(values[i].equals(tabPref)){
                tabLayout.getTabAt(i).select();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_settings:
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                return true;
            case R.id.action_about:
                MaterialDialog dialog =  new MaterialDialog.Builder(this)
                        .title(R.string.acerca_title)
                        .customView(R.layout.dialog_acerca, true)
                        .positiveText(R.string.dialog_close)
                        .build();
                dialog.show();

                View v = dialog.getCustomView();
                ((TextView) v.findViewById(R.id.acerca_text)).setMovementMethod(LinkMovementMethod.getInstance());
                ((TextView) v.findViewById(R.id.creditos_text)).setMovementMethod(LinkMovementMethod.getInstance());

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query){
        Intent intent = new Intent(MainActivity.this, ReglamentoActivity.class);
        intent.putExtra(ArticuloFragment.ARG_QUERY_ID, query);
        startActivity(intent);

        return true;
    }

    @Override
    public void onShareTip(Tip tip) {
        final Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, tip.getResumen() + " " + getString(R.string.share_text));

        if(!TextUtils.isEmpty(tip.getImagen())) {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, tip.getImagen());
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
            Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            int imageId = getResources().getIdentifier(tip.getImagen(), "drawable", getPackageName());
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imageId);
            OutputStream outstream;
            try {
                outstream = getContentResolver().openOutputStream(uri);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outstream);
                outstream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
            shareIntent.setType("image/png");

        } else {
            shareIntent.setType("text/plain");
        }

        mBottomSheet.showWithSheetView(new IntentPickerSheetView(this, shareIntent, getString(R.string.share_title),
                new IntentPickerSheetView.OnIntentPickedListener() {
                    @Override
                    public void onIntentPicked(IntentPickerSheetView.ActivityInfo activityInfo) {
                        mBottomSheet.dismissSheet();
                        startActivity(activityInfo.getConcreteIntent(shareIntent));
                    }
                }));
    }

    @Override
    public void onHeaderClic(int id) {
        switch (id){
            case R.id.depositos_btn:
                startActivity(new Intent(MainActivity.this, DepositosActivity.class));
                break;
            case R.id.infracciones_btn:
                startActivity(new Intent(MainActivity.this, InfraccionesActivity.class));
                break;
            case R.id.agentes_btn:
                break;
        }
    }

    static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment) {
            mFragmentList.add(fragment);
        }
    }

    public static class TipsFragment extends Fragment {

        public TipsFragment() {}

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_tips, container, false);
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.tiplist);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setHasFixedSize(true);

            Bundle args = getArguments();
            JsonReader reader = new JsonReader(getResources(), args.getInt(ARG_RESOURCE_ID, 0));

            boolean showHeader = args.getInt(ARG_RESOURCE_ID, 0) == R.raw.automovil;
            TipsAdapter adapter = new TipsAdapter(getContext(), Arrays.asList(reader.getObjects(Tip[].class)), showHeader);
            recyclerView.setAdapter(adapter);
            adapter.setOnShareTipListener((TipsAdapter.OnShareTipListener) getActivity());
            adapter.setOnHeaderClicListener((TipsAdapter.OnHeaderClicListener) getActivity());

            return view;
        }
    }
}
