package lukasz.marczak.pl.gotta_catch_em_all.connection;

import lukasz.marczak.pl.gotta_catch_em_all.data.PokeAbility;
import lukasz.marczak.pl.gotta_catch_em_all.data.PokeDetail;
import lukasz.marczak.pl.gotta_catch_em_all.data.PokeID;
import lukasz.marczak.pl.gotta_catch_em_all.data.PokeMove;
import lukasz.marczak.pl.gotta_catch_em_all.data.PokeType;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by Lukasz Marczak on 2015-08-23.
 */
public interface PokeApi {
    String POKEMON_API_ENDPOINT = "http://pokeapi.co";
    int POKEMONS_COUNT = 493;
    int MOVES_COUNT = 626;
    int ABILITIES_COUNT = 248;
    int TYPES_COUNT = 18;

    @GET("/api/v1/sprite/{id}")
    rx.Observable<Response> getPokemonNameByID(@Path("id") Integer id);

    @GET("/api/v1/sprite/{id}")
    rx.Observable<String> getPokemonByID(@Path("id") Integer id);

    //718 pokemons available, but we download only 493 due to limited sprites available
    @GET("/api/v1/pokemon/{id}/")
    rx.Observable<PokeDetail> getPokemonDetail(@Path("id") Integer id);

    //18 types available
    @GET("/api/v1/type/{id}/")
    rx.Observable<PokeType> getPokemonType(@Path("id") Integer id);

    //626 moves available
    @GET("/api/v1/move/{id}/")
    rx.Observable<PokeMove> getPokemonMove(@Path("id") Integer id);

    //248 abilitiess available
    @GET("/api/v1/ability/{id}/")
    rx.Observable<PokeAbility> getPokemonAbility(@Path("id") Integer id);
}
