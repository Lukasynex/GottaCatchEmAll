package lukasz.marczak.pl.gotta_catch_em_all.connection;

import com.google.gson.JsonElement;

import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by Lukasz Marczak on 2015-08-23.
 */
public interface Pokedex {



//    http://img.pokemondb.net/sprites/black-white/normal/mew.png

    //sprite
//    http://pokeapi.co/media/img/26.png
    @GET("/{id}.png")
    void getPokemonByID(@Path("id") String id, retrofit.Callback<JsonElement> callback);
}
