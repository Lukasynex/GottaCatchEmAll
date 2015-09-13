package lukasz.marczak.pl.gotta_catch_em_all.connection;

import com.google.gson.JsonElement;

import javax.security.auth.callback.Callback;

import lukasz.marczak.pl.gotta_catch_em_all.data.NetPoke;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

/**
 * Created by Lukasz Marczak on 2015-08-23.
 */
public interface PokedexService {
    String POKEDEX_SPRITE_ENDPOINT = "http://pokeapi.co/media/img";
    String POKEDEX_NAME_ENDPOINT = "http://pokeapi.co/api/v1/sprite";
    //    http://pokeapi.co/api/v1/sprite/150/

//http://pokeapi.co/

    @GET("/{id}.png")
    void getPokemonSpriteByID(@Path("id") String id, retrofit.Callback<JsonElement> callback);

    @GET("/{id}")
    rx.Observable<Response> getPokemonNameByID(@Path("id") Integer id);

    @GET("/{id}")
    rx.Observable<String> getPokemonByID(@Path("id") Integer id);


    @GET("/{id}")
    rx.Observable<String> getPokemonName(@Path("id") Integer id);



}
