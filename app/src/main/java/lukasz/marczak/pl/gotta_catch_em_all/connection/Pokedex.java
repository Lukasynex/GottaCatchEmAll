package lukasz.marczak.pl.gotta_catch_em_all.connection;

import com.google.gson.JsonElement;

import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by Lukasz Marczak on 2015-08-23.
 */
public interface Pokedex {
    String POKEDEX_SPRITE_ENDPOINT = "http://pokeapi.co/media/img";
    String POKEDEX_NAME_ENDPOINT = "http://pokeapi.co/api/v1/sprite";
    //    http://pokeapi.co/api/v1/sprite/150/

//http://pokeapi.co/

    @GET("/{id}.png")
    void getPokemonByID(@Path("id") String id, retrofit.Callback<JsonElement> callback);

    @GET("/{id}")
    rx.Observable<String> getPokemonNameByID(@Path("id") Integer id);
}
