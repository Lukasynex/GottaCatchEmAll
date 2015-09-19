package lukasz.marczak.pl.gotta_catch_em_all.connection;

import android.util.Log;

import com.google.gson.reflect.TypeToken;

import java.util.LinkedList;
import java.util.List;

import lukasz.marczak.pl.gotta_catch_em_all.JsonArium.PokeAbilityDeserializer;
import lukasz.marczak.pl.gotta_catch_em_all.JsonArium.PokeDetailDeserializer;
import lukasz.marczak.pl.gotta_catch_em_all.JsonArium.PokeMoveDeserializer;
import lukasz.marczak.pl.gotta_catch_em_all.JsonArium.PokeNetNameDeserializer;
import lukasz.marczak.pl.gotta_catch_em_all.JsonArium.PokeTypeDeserializer;
import lukasz.marczak.pl.gotta_catch_em_all.activities.MainActivity;
import lukasz.marczak.pl.gotta_catch_em_all.data.PokeAbility;
import lukasz.marczak.pl.gotta_catch_em_all.data.PokeDetail;
import lukasz.marczak.pl.gotta_catch_em_all.data.PokeID;
import lukasz.marczak.pl.gotta_catch_em_all.data.PokeMove;
import lukasz.marczak.pl.gotta_catch_em_all.data.PokeType;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * Created by Lukasz Marczak on 2015-09-19.
 */
public class AllDataDownloader {
    public static final AllDataDownloader INSTANCE = new AllDataDownloader();
    public static final String TAG = AllDataDownloader.class.getSimpleName();

    /**
     * services for download
     */
    PokeApi serviceID;
    PokeApi serviceDetail;
    PokeApi serviceAbility;
    PokeApi serviceType;
    PokeApi serviceMove;


    private AllDataDownloader() {
    }

    public void setupServices(final MainActivity context) {
        Log.d(TAG, "start ");
        serviceID = new SimpleRestAdapter(
                PokeApi.POKEMON_API_ENDPOINT, new TypeToken<String>() {
        }.getType(), PokeNetNameDeserializer.INSTANCE).getPokedexService();

        serviceDetail = new SimpleRestAdapter(
                PokeApi.POKEMON_API_ENDPOINT, new TypeToken<PokeDetail>() {
        }.getType(), PokeDetailDeserializer.INSTANCE).getPokedexService();

        serviceAbility = new SimpleRestAdapter(
                PokeApi.POKEMON_API_ENDPOINT, new TypeToken<PokeAbility>() {
        }.getType(), PokeAbilityDeserializer.INSTANCE).getPokedexService();

        serviceType = new SimpleRestAdapter(
                PokeApi.POKEMON_API_ENDPOINT, new TypeToken<PokeType>() {
        }.getType(), PokeTypeDeserializer.INSTANCE).getPokedexService();

        serviceMove = new SimpleRestAdapter(
                PokeApi.POKEMON_API_ENDPOINT, new TypeToken<PokeMove>() {
        }.getType(), PokeMoveDeserializer.getInstance(context)).getPokedexService();
    }

    public void downloadData(final MainActivity context) {
        Log.d(TAG, "downloadData ");

        List<Integer> moves = new LinkedList<>();
        for (int j = 1; j <= PokeApi.MOVES_COUNT; j++) moves.add(j);
        /**download data(explaination)

         FILTER                                          FILTER                                  FILTER
         1 -> Observable<PokeMove> ->  1, PokeMove  ->  1, Observable<PokeDetail> -> PokeDetail -> Observable<PokeAbility> -> PokeAbility -> Observable<PokeType> PokeType
         2 -> Observable<PokeMove> ->  1, PokeMove  ->  1, Observable<PokeDetail> -> PokeDetail -> Observable<PokeAbility> -> PokeAbility -> Observable<PokeType> PokeType
         3 -> Observable<PokeMove> ->  1, PokeMove  ->  1, Observable<PokeDetail> -> PokeDetail -> Observable<PokeAbility> -> PokeAbility -> Observable<PokeType> PokeType
         .
         .
         626->Observable<PokeMove> ->626, PokeMove  ->493, Observable<PokeDetail> -> PokeDetail -> Observable<PokeAbility> -> PokeAbility -> Observable<PokeType> PokeType

         */
        Observable.from(moves).flatMap(new Func1<Integer, Observable<PokeMove>>() {
            @Override
            public Observable<PokeMove> call(Integer id) {
                Log.d(TAG, "fetching 626 moves...");
                return serviceMove.getPokemonMove(id);
            }
        }).map(new Func1<PokeMove, PokeMove>() {
            @Override
            public PokeMove call(PokeMove pokeMove) {
                //save PokeMove in DB
                return pokeMove;
            }
        }).filter(new Func1<PokeMove, Boolean>() {
            @Override
            public Boolean call(PokeMove pokeMove) {
                return pokeMove.getId() <= PokeApi.POKEMONS_COUNT;
            }
        }).flatMap(new Func1<PokeMove, Observable<PokeDetail>>() {
            @Override
            public Observable<PokeDetail> call(PokeMove pokeMove) {
                Log.d(TAG, "fetching 493 pokemon IDs...");
                //saved PokeMove in DB
                return serviceDetail.getPokemonDetail(pokeMove.getId());
            }
        }, new Func2<PokeMove, PokeDetail, PokeDetail>() {
            @Override
            public PokeDetail call(PokeMove pokeMove, PokeDetail pokeDetail) {
                pokeDetail.setId(pokeMove.getId());
                return pokeDetail;
            }
        }).map(new Func1<PokeDetail, PokeDetail>() {
            @Override
            public PokeDetail call(PokeDetail pokeDetail) {
                //saved PokeDetail in DB
                return pokeDetail;
            }
        }).filter(new Func1<PokeDetail, Boolean>() {
            @Override
            public Boolean call(PokeDetail pokeDetail) {
                Log.d(TAG, "filtering : limit to " + PokeApi.ABILITIES_COUNT + " pokemon abilities");
                return pokeDetail.getId() <= PokeApi.ABILITIES_COUNT;
            }
        }).flatMap(new Func1<PokeDetail, Observable<PokeAbility>>() {
            @Override
            public Observable<PokeAbility> call(PokeDetail pokeDetail) {
                return serviceAbility.getPokemonAbility(pokeDetail.getId());
            }
        }).map(new Func1<PokeAbility, PokeAbility>() {
            @Override
            public PokeAbility call(PokeAbility pokeAbility) {
                //saved PokeAbility in DB
                return pokeAbility;
            }
        }).filter(new Func1<PokeAbility, Boolean>() {
            @Override
            public Boolean call(PokeAbility pokeAbility) {
                Log.d(TAG, "filtering : limit to " + PokeApi.TYPES_COUNT + " pokemon types");
                return pokeAbility.getId() <= PokeApi.TYPES_COUNT;
            }
        }).flatMap(new Func1<PokeAbility, Observable<PokeType>>() {
            @Override
            public Observable<PokeType> call(PokeAbility pokeAbility) {
                return serviceType.getPokemonType(pokeAbility.getId());
            }
        }).subscribe(new Subscriber<PokeType>() {
            @Override
            public void onCompleted() {
                SimpleRestAdapter.onCompleted(TAG, this);
            }

            @Override
            public void onError(Throwable e) {
                SimpleRestAdapter.onErrorCompleted(TAG, this, e);
            }

            @Override
            public void onNext(PokeType pokeType) {
                Log.d(TAG, "onNext " + pokeType.getName());
                //saved PokeType in DB
            }
        });
    }
}
