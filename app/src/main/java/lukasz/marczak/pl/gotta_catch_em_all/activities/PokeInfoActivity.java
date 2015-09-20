package lukasz.marczak.pl.gotta_catch_em_all.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.tt.whorlviewlibrary.WhorlView;

import io.realm.Realm;
import lukasz.marczak.pl.gotta_catch_em_all.JsonArium.PokeDetailDeserializer;
import lukasz.marczak.pl.gotta_catch_em_all.R;
import lukasz.marczak.pl.gotta_catch_em_all.configuration.Config;
import lukasz.marczak.pl.gotta_catch_em_all.configuration.PokeConstants;
import lukasz.marczak.pl.gotta_catch_em_all.configuration.PokeUtils;
import lukasz.marczak.pl.gotta_catch_em_all.connection.PokeSpritesManager;
import lukasz.marczak.pl.gotta_catch_em_all.connection.PokeApi;
import lukasz.marczak.pl.gotta_catch_em_all.connection.SimpleRestAdapter;
import lukasz.marczak.pl.gotta_catch_em_all.data.BeaconsInfo;
import lukasz.marczak.pl.gotta_catch_em_all.data.PokeDetail;
import lukasz.marczak.pl.gotta_catch_em_all.data.PokeMove;
import lukasz.marczak.pl.gotta_catch_em_all.data.realm.DBManager;
import lukasz.marczak.pl.gotta_catch_em_all.data.realm.RealmPokeDetail;
import rx.Subscriber;
import rx.functions.Action0;

public class PokeInfoActivity extends AppCompatActivity {

    public static final String TAG = PokeInfoActivity.class.getSimpleName();
    private TextView id;
    private TextView name;
    private TextView description;
    private ImageView image;
    private RelativeLayout mainLayout;
    private WhorlView progressBarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
        setContentView(R.layout.activity_pokemon_details);
        injectViews();
        Intent data = getIntent();
        if (data != null) {
            int p_id = data.getIntExtra(PokeConstants.ID, 26);
            String p_name = data.getStringExtra(PokeConstants.NAME);

            name.setText("");
            id.setText("");

            String url = PokeSpritesManager.getMainPokeByName(p_name
                    /*PokeUtils.getPokemonNameFromId(this, p_id)*/);
            Picasso.with(this).load(url).into(image);
            setupDetails(p_id);
        }


//        switchToFragment(Config.FRAGMENT.START_FIGHT);
    }

    private void setupDetails(int _id) {
        Log.d(TAG, "setupDetails ");

        Realm realm = Realm.getInstance(this);
        realm.beginTransaction();

        RealmPokeDetail pokeDetail = realm.where(RealmPokeDetail.class).equalTo("pkdxId", _id).findFirst();
        realm.commitTransaction();
        if (pokeDetail != null) {
            Log.i(TAG, "pokemon detail already exists, showing details");
            description.setText(pokeDetail.toString());
            name.setText(pokeDetail.getName());
            id.setText("#" + pokeDetail.getPkdxId());
            showProgressLayout(false);
        } else {
            Log.e(TAG, "database does not contain this pokemon! fetching info from json...");
            final PokeApi pokeApi = new SimpleRestAdapter(PokeApi.POKEMON_API_ENDPOINT, new TypeToken<PokeDetail>() {
            }.getType(), PokeDetailDeserializer.getInstance(this)).getPokedexService();

            pokeApi.getPokemonDetail(_id)
                    .doOnSubscribe(new Action0() {
                        @Override
                        public void call() {
                            Log.d(TAG, "doOnSubscribe");
                        }
                    })
                    .doOnCompleted(new Action0() {
                        @Override
                        public void call() {
                            Log.d(TAG, "doOnCompleted");
                            PokeInfoActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showProgressLayout(false);
                                }
                            });
                        }
                    })
                    .subscribe(new Subscriber<PokeDetail>() {
                        @Override
                        public void onCompleted() {
                            PokeInfoActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showProgressLayout(false);
                                }
                            });
                            SimpleRestAdapter.onCompleted(TAG, this);
                        }

                        @Override
                        public void onError(Throwable e) {
                            PokeInfoActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showProgressLayout(false);
                                }
                            });
                            SimpleRestAdapter.onErrorCompleted(TAG, this, e);
                        }

                        @Override
                        public void onNext(final PokeDetail pokeDetail) {
                            Log.d(TAG, "onNext " + pokeDetail);
                            PokeInfoActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    description.setText(pokeDetail.toString());
                                    name.setText(pokeDetail.getName());
                                    id.setText("#" + pokeDetail.getPkdxId());
                                }
                            });
                            DBManager.getInstance(PokeInfoActivity.this).savePokeDetail(pokeDetail);
                        }
                    });
        }
    }

    private void showProgressLayout(boolean show) {
        Log.d(TAG, "showProgressLayout " + show);
        int showMain = !show ? View.VISIBLE : View.GONE;
        int showProgress = show ? View.VISIBLE : View.GONE;
        mainLayout.setVisibility(showMain);
        progressBarLayout.setVisibility(showProgress);
    }

    private void injectViews() {
        Log.d(TAG, "injectViews ");
        id = (TextView) findViewById(R.id.pokemon_id);
        name = (TextView) findViewById(R.id.pokemon_name);
        description = (TextView) findViewById(R.id.pokemon_description);
        image = (ImageView) findViewById(R.id.pokemon_image);
        mainLayout = (RelativeLayout) findViewById(R.id.dataParent3);
        progressBarLayout = (WhorlView) findViewById(R.id.progressBar_details);
        progressBarLayout.start();
        showProgressLayout(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_fight, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + ")");
        switch (requestCode) {
            case Config.IntentCode.START_WILD_FIGHT: {

                if (resultCode == RESULT_OK) {
                    Log.d(TAG, "result OK");
                }
                if (resultCode == RESULT_CANCELED) {
                    Log.d(TAG, "there's no result");
                }
                break;
            }
            default: {
                Log.d(TAG, "nothing");
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d(TAG, "onBackPressed()");
        BeaconsInfo.FORCE_STOP_SCAN = false;
        BeaconsInfo.NEW_FIGHT = false;
    }
}
