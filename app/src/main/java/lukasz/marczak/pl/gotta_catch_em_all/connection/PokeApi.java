package lukasz.marczak.pl.gotta_catch_em_all.connection;

import com.google.gson.JsonElement;

import javax.security.auth.callback.Callback;

import lukasz.marczak.pl.gotta_catch_em_all.data.NetPoke;
import lukasz.marczak.pl.gotta_catch_em_all.data.PokeDetail;
import lukasz.marczak.pl.gotta_catch_em_all.data.PokeType;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

/**
 * Created by Lukasz Marczak on 2015-08-23.
 */
public interface PokeApi {
    String POKEMON_API_ENDPOINT = "http://pokeapi.co";

    @GET("/api/v1/sprite/{id}")
    rx.Observable<Response> getPokemonNameByID(@Path("id") Integer id);

    @GET("/api/v1/sprite/{id}")
    rx.Observable<String> getPokemonByID(@Path("id") Integer id);

    @GET("/api/v1/pokemon/{id}/")
    rx.Observable<PokeDetail> getPokemonDetails(@Path("id") Integer id);


    @GET("/api/v1/type/{id}/")
    rx.Observable<PokeType> getPokemonType(@Path("id") Integer id);


}
