package lukasz.marczak.pl.gotta_catch_em_all.configuration;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import lukasz.marczak.pl.gotta_catch_em_all.adapters.PokedexAdapter;
import lukasz.marczak.pl.gotta_catch_em_all.data.NetPoke;
import lukasz.marczak.pl.gotta_catch_em_all.data.RealmPoke;

/**
 * Created by Lukasz Marczak on 2015-08-24.
 */
public final class PokeUtils {
    public static final String TAG = PokeUtils.class.getSimpleName();


    public static List<NetPoke> netPokes = new ArrayList<>();


    public static String getPokeResByID(int ID) {
        if (ID < 1 || ID > 150)
            ID = 1;
        return "http://pokeapi.co/media/img/" + ID + ".png";

    }

    public static int getRandomPokemonID() {
        int randy = (int) System.currentTimeMillis() % 151 + 1;
        randy = randy < 0 ? -randy : randy;
        Log.d(TAG, "getRandomPokemonID " + randy);
        return randy;
    }

    public static String getPokemonNameFromId(Context context, final int pokemonID) {
        Log.d(TAG, "getPokemonNameFromId " + pokemonID);
        RealmPoke poke = Realm.getInstance(context)
                .where(RealmPoke.class).equalTo("id", String.valueOf(pokemonID), false).findFirst();
        if (poke != null) {
            Log.i(TAG, "found pokemon by id: " + poke.getName());
            return poke.getName();
        }
        return "pikachu";
    }

    public static String getPrettyPokemonName(String pokemonName) {
        return pokemonName.substring(0, 1).toUpperCase() + pokemonName.substring(1);
    }
}
