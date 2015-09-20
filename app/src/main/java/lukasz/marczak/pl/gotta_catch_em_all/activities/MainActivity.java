package lukasz.marczak.pl.gotta_catch_em_all.activities;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.tt.whorlviewlibrary.WhorlView;

import lukasz.marczak.pl.gotta_catch_em_all.R;
import lukasz.marczak.pl.gotta_catch_em_all.configuration.Config;
import lukasz.marczak.pl.gotta_catch_em_all.configuration.PokeConstants;
import lukasz.marczak.pl.gotta_catch_em_all.download.AllDataDownloader;
import lukasz.marczak.pl.gotta_catch_em_all.data.AppFirstLauncher;
import lukasz.marczak.pl.gotta_catch_em_all.fragments.main.PokeTypesFragment;
import lukasz.marczak.pl.gotta_catch_em_all.fragments.main.PokedexFragment;
import lukasz.marczak.pl.gotta_catch_em_all.fragments.main.RangeFragment;
import lukasz.marczak.pl.gotta_catch_em_all.fragments.main.RealmPokeFragment;
import lukasz.marczak.pl.gotta_catch_em_all.fragments.main.TripFragment;


public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private RelativeLayout progressBarLayout;
    private FrameLayout main;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] headers;
    public static boolean firstLaunch = true;
    public static String pokeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent data = getIntent();
        pokeName = data.getStringExtra(PokeConstants.NAME);


        mTitle = mDrawerTitle = getTitle();
        headers = Config.HEADERS;//getResources().getStringArray(R.array.planets_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        progressBarLayout = (RelativeLayout) findViewById(R.id.progressBarLayout);
        main = (FrameLayout) findViewById(R.id.content_frame);
        WhorlView whorl = (WhorlView) findViewById(R.id.progressBar_main);
        whorl.start();
        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, headers));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getActionBar().setHomeButtonEnabled(true);
        }
        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.abc_action_bar_home_description,  /* "open drawer" description for accessibility */
                R.string.abc_search_hint  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                //getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {

                //getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            selectItem(0);
        }

        boolean isFirstLaunch = AppFirstLauncher.INSTANCE.ifAppFirstLaunched(this);
        Log.i(TAG, "isFirstLaunch : " + isFirstLaunch);
        if (isFirstLaunch) {
            AppFirstLauncher.INSTANCE.setup(this);
            downloadAllData();
        }else{
            showProgressBar(false);
        }
    }


    private rx.Subscription downloadSubscription;

    public void showProgressBar(boolean show) {
        Log.d(TAG, "showProgressBar " + show);
        int vis = show ? View.VISIBLE : View.GONE;
        int inv = !show ? View.VISIBLE : View.GONE;
        progressBarLayout.setVisibility(vis);
        main.setVisibility(inv);
    }

    private void downloadAllData() {
        Log.d(TAG, "download Pokedex ");
//        showProgressBar(true);
//        downloadSubscription = AppObservable.
//                bindActivity(this, _getDownloadPokedexObservable())
//                .subscribe(_getDummyObserver());
        AllDataDownloader.INSTANCE.setupServices(this).downloadData(this);
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        Log.d(TAG, "isMyServiceRunning()");

        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
//        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
//        menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop ");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
//        switch(item.getItemId()) {
//        case R.id.action_websearch:
//            // create intent to perform web search for this planet
//            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
//            intent.putExtra(SearchManager.QUERY, getActionBar().getTitle());
//            // catch event that there's no activity to handle intent
//            if (intent.resolveActivity(getPackageManager()) != null) {
//                startActivity(intent);
//            } else {
//                Toast.makeText(this, R.string.app_not_available, Toast.LENGTH_LONG).show();
//            }
//            return true;
//        default:
        return super.onOptionsItemSelected(item);
//        }
    }


    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    public void selectItem(int position) {
        Log.d(TAG, "selectItem " + position);
        Fragment fragment = getFragmentForPosition(position);

        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.fab_in, R.anim.fab_out, R.anim.fab_in, R.anim.fab_out)
                .replace(R.id.content_frame, fragment)
                .commit();

        mDrawerList.setItemChecked(position, true);
        setTitle(headers[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    private Fragment getFragmentForPosition(int selectedFragment) {
        Fragment fragment;
        Config.CURRENT_FRAGMENT = selectedFragment;
        switch (selectedFragment) {
            case Config.FRAGMENT.TRIP:

                Log.v(TAG, "switchToFragment - trip");
                fragment = TripFragment.newInstance(pokeName);
                break;
            case Config.FRAGMENT.RANGE:
                Log.v(TAG, "switchToFragment - range");
                fragment = RangeFragment.newInstance();
                break;
            case Config.FRAGMENT.POKEDEX:
                Log.v(TAG, "switchToFragment - POKEDEX");
                fragment = PokedexFragment.newInstance();
                break;
            case Config.FRAGMENT.ALL_POKEMONS:
                Log.v(TAG, "switchToFragment - POKEDEX");
                fragment = RealmPokeFragment.newInstance();
                break;
            case Config.FRAGMENT.ALL_POKEMON_TYPES:
                Log.v(TAG, "switchToFragment - POKEDEX");
                fragment = PokeTypesFragment.newInstance();
                break;
//            case Config.FRAGMENT_SETTINGS:
//                Log.v(TAG, "switchToFragment - settings");
//                fragment = SettingsFragment.newInstance();
//                break;
//            case Config.FRAGMENT_INFO:
//                Log.v(TAG, "switchToFragment - information");
//                fragment = InformationFragment.newInstance();
//                break;
//            case Config.FRAGMENT_STATISTICS:
//                Log.v(TAG, "switchToFragment - statistics");
//                fragment = StatisticsFragment.newInstance();
//                break;
            default:
                Log.v(TAG, "switchToFragment - main fragment");
                fragment = TripFragment.newInstance(pokeName);
                break;
        }
        return fragment;
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        if (getActionBar() != null)
            getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "request code: " + requestCode);
        Log.d(TAG, "result code: " + resultCode);
    }

    @Override
    public void onBackPressed() {
        if (Config.CURRENT_FRAGMENT != 0)
            selectItem(0);
        else super.onBackPressed();
    }
}
