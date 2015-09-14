package lukasz.marczak.pl.gotta_catch_em_all.fragments.fight;

import android.app.Activity;
import android.os.Bundle;

import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import io.realm.Realm;
import lukasz.marczak.pl.gotta_catch_em_all.JsonArium.PokeNetNameDeserializer;
import lukasz.marczak.pl.gotta_catch_em_all.R;
import lukasz.marczak.pl.gotta_catch_em_all.activities.FightActivity;
import lukasz.marczak.pl.gotta_catch_em_all.configuration.Config;
import lukasz.marczak.pl.gotta_catch_em_all.configuration.PokeUtils;
import lukasz.marczak.pl.gotta_catch_em_all.connection.PokeSpritesManager;
import lukasz.marczak.pl.gotta_catch_em_all.connection.PokedexService;
import lukasz.marczak.pl.gotta_catch_em_all.connection.SimpleRestAdapter;
import lukasz.marczak.pl.gotta_catch_em_all.data.BeaconsInfo;
import lukasz.marczak.pl.gotta_catch_em_all.data.RealmPoke;
import rx.Observable;
import rx.Subscriber;

public class StartFightFragment extends Fragment {
    private static final String TAG = StartFightFragment.class.getSimpleName();
    private ImageView wildPokemon;
    private TextView pokemonNameTV;
    private TextView title;
    private FightActivity parentActivity;
    private String pokemonName;
    private int pokemonID = 1;
    private Handler handlarzNarkotykow = new Handler();

    public static StartFightFragment newInstance(int ID) {
        StartFightFragment fragment = new StartFightFragment();
        Bundle args = new Bundle();
//        args.putString(BeaconsInfo.PokeInterface.POKEMON_NAME, pokemonName);
        args.putInt(BeaconsInfo.PokeInterface.POKEMON_ID, ID);
        fragment.setArguments(args);

        return fragment;
    }

    public StartFightFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate ");
        if (getArguments() != null) {
//            pokemonName = getArguments().getString(BeaconsInfo.PokeInterface.POKEMON_NAME);
            pokemonID = getArguments().getInt(BeaconsInfo.PokeInterface.POKEMON_ID);
        }
        Log.d(TAG, "onCreate ");
        RealmPoke poke = Realm.getInstance(getActivity()).where(RealmPoke.class)
                .equalTo("id", String.valueOf(pokemonID), false).findFirst();
        pokemonName = poke.getName();
//        String image = PokeUtils.getPokeResByID(pokemonID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_start, container, false);
        wildPokemon = (ImageView) view.findViewById(R.id.wild_pokemon);


        title = (TextView) view.findViewById(R.id.title);
        pokemonNameTV = (TextView) view.findViewById(R.id.pokemonName);
        wildPokemon.setVisibility(View.GONE);
        title.setText("Wild " + PokeUtils.getPrettyPokemonName(pokemonName) + " appeared!!!");
        String image = PokeSpritesManager.getMainPokeByName(PokeUtils.getPokemonNameFromId(getActivity(), pokemonID));
        pokemonNameTV.setText(pokemonName);
        Log.d(TAG, "fetching image: " + image);
        Picasso.with(parentActivity).load(image).into(wildPokemon);
        wildPokemon.setVisibility(View.VISIBLE);

        return view;
    }


    private void startFight() {
        Log.d(TAG, "startFight()");
        handlarzNarkotykow.postDelayed(fightBody(), 3000); //delay of 2 seconds
    }

    private Runnable fightBody() {
        return new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run ");
                Log.d(TAG, "fight Body()");

                parentActivity.switchToFragment(Config.FRAGMENT.RUNNING_FIGHT, pokemonName);
            }
        };

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated ");
        startFight();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        parentActivity = (FightActivity) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public Subscriber<String> getPokeNameSubscriber() {
        return new Subscriber<String>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted()");

                handlarzNarkotykow
                        .post(new Runnable() {
                                  @Override
                                  public void run() {
                                      Picasso.with(parentActivity)
                                              .load(PokeSpritesManager
                                                      .getMainPokeByName(pokemonName)).into(wildPokemon);
                                      if (pokemonName.isEmpty())
                                          pokemonName = "pikachu";
                                      pokemonNameTV.setText(pokemonName.toUpperCase().substring(0, 1) +
                                              pokemonName.substring(1));

                                  }
                              }
                        );
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError()");
                e.printStackTrace();
                Picasso.with(parentActivity)
                        .load(PokeUtils.getPokeResByID(pokemonID)).into(wildPokemon);
                if (pokemonName.isEmpty())
                    pokemonName = "pokemon";

                title.setText("Wild " +
                        pokemonName.toUpperCase().substring(0, 1) +
                        pokemonName.substring(1) + " appeared!!!");

            }

            @Override
            public void onNext(String s) {
                Log.d(TAG, "onNext(" + s + ")");

                if (s.isEmpty())
                    pokemonName = "bulbasaur";
                else
                    pokemonName = s;
            }
        };
    }
}
