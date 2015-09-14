package lukasz.marczak.pl.gotta_catch_em_all.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import lukasz.marczak.pl.gotta_catch_em_all.JsonArium.PokeDetailDeserializer;
import lukasz.marczak.pl.gotta_catch_em_all.R;
import lukasz.marczak.pl.gotta_catch_em_all.configuration.Config;
import lukasz.marczak.pl.gotta_catch_em_all.configuration.PokeConstants;
import lukasz.marczak.pl.gotta_catch_em_all.configuration.PokeUtils;
import lukasz.marczak.pl.gotta_catch_em_all.connection.PokeSpritesManager;
import lukasz.marczak.pl.gotta_catch_em_all.connection.PokedexService;
import lukasz.marczak.pl.gotta_catch_em_all.connection.SimpleRestAdapter;
import lukasz.marczak.pl.gotta_catch_em_all.data.BeaconsInfo;
import lukasz.marczak.pl.gotta_catch_em_all.data.PokeDetail;
import lukasz.marczak.pl.gotta_catch_em_all.data.Pokemon;
import lukasz.marczak.pl.gotta_catch_em_all.fragments.fight.FightRunningFragment;
import lukasz.marczak.pl.gotta_catch_em_all.fragments.fight.StartFightFragment;
import rx.Subscriber;

public class PokeInfoActivity extends AppCompatActivity {

    public static final String TAG = PokeInfoActivity.class.getSimpleName();
    private TextView id;
    private TextView name;
    private TextView description;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
        setContentView(R.layout.activity_pokemon_details);
        injectViews();
        Intent data = getIntent();
        if (data != null) {
            int p_id = data.getIntExtra(PokeConstants.ID, 26);

            name.setText("");
            id.setText("");

            String url = PokeSpritesManager.getMainPokeByName(PokeUtils.getPokemonNameFromId(this, p_id));
            Picasso.with(this).load(url).into(image);
            setupDetails(p_id);
        }


//        switchToFragment(Config.FRAGMENT.START_FIGHT);
    }

    private void setupDetails(int _id) {
        Log.d(TAG, "setupDetails ");
        SimpleRestAdapter adapter = new SimpleRestAdapter(PokedexService.POKEMON_API_ENDPOINT,
                new TypeToken<PokeDetail>() {
                }.getType(), PokeDetailDeserializer.INSTANCE);
        PokedexService service = adapter.getPokedexService();
        service.getPokemonDetails(_id).subscribe(new Subscriber<PokeDetail>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted ");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError ");
            }

            @Override
            public void onNext(final PokeDetail pokeDetail) {
                Log.d(TAG, "onNext ");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        description.setText(pokeDetail.toString());
                        name.setText(pokeDetail.getName());
                        id.setText("#" + pokeDetail.getId());
                    }
                });
            }
        });
    }

    private void injectViews() {
        Log.d(TAG, "injectViews ");
        id = (TextView) findViewById(R.id.pokemon_id);
        name = (TextView) findViewById(R.id.pokemon_name);
        description = (TextView) findViewById(R.id.pokemon_description);
        image = (ImageView) findViewById(R.id.pokemon_image);
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

//    public void switchToFragment(int fragmentId) {
//        Fragment fragment;
//        switch (fragmentId) {
//            case Config.FRAGMENT.START_FIGHT:
//                fragment = StartFightFragment.newInstance(
//                        PokeUtils.getRandomPokemonID());
//                break;
//            case Config.FRAGMENT.RUNNING_FIGHT:
//                fragment = FightRunningFragment.newInstance();
//                break;
//            default:
//                fragment = StartFightFragment.newInstance(PokeUtils.getRandomPokemonID());
//        }
//        getSupportFragmentManager()
//                .beginTransaction()
//                .setCustomAnimations(R.anim.fab_in, R.anim.fab_out, R.anim.fab_in, R.anim.fab_out)
//                .replace(R.id.content_frame, fragment)
//                .commit();
//    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d(TAG, "onBackPressed()");
        BeaconsInfo.FORCE_STOP_SCAN = false;
        BeaconsInfo.NEW_FIGHT = false;
    }
}
