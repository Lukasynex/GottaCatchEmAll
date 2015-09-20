package lukasz.marczak.pl.gotta_catch_em_all.connection;

import android.text.BoringLayout;
import android.util.Log;

import com.google.gson.reflect.TypeToken;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.realm.Realm;
import lukasz.marczak.pl.gotta_catch_em_all.JsonArium.PokeAbilityDeserializer;
import lukasz.marczak.pl.gotta_catch_em_all.JsonArium.PokeDetailDeserializer;
import lukasz.marczak.pl.gotta_catch_em_all.JsonArium.PokeIdDeserializer;
import lukasz.marczak.pl.gotta_catch_em_all.JsonArium.PokeMoveDeserializer;
import lukasz.marczak.pl.gotta_catch_em_all.JsonArium.PokeTypeDeserializer;
import lukasz.marczak.pl.gotta_catch_em_all.activities.MainActivity;
import lukasz.marczak.pl.gotta_catch_em_all.data.PokeAbility;
import lukasz.marczak.pl.gotta_catch_em_all.data.PokeDetail;
import lukasz.marczak.pl.gotta_catch_em_all.data.PokeID;
import lukasz.marczak.pl.gotta_catch_em_all.data.PokeMove;
import lukasz.marczak.pl.gotta_catch_em_all.data.PokeType;
import lukasz.marczak.pl.gotta_catch_em_all.data.realm.DBManager;
import lukasz.marczak.pl.gotta_catch_em_all.data.realm.RealmAbility;
import lukasz.marczak.pl.gotta_catch_em_all.data.realm.RealmID;
import lukasz.marczak.pl.gotta_catch_em_all.data.realm.RealmMove;
import lukasz.marczak.pl.gotta_catch_em_all.data.realm.RealmPokeDetail;
import lukasz.marczak.pl.gotta_catch_em_all.data.realm.RealmType;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Lukasz Marczak on 2015-09-19.
 */
public class AllDataDownloader {
    public static final AllDataDownloader INSTANCE = new AllDataDownloader();
    public static final String TAG = AllDataDownloader.class.getSimpleName();
    private static MainActivity context;
    private static long millisStart;
    /**
     * services for download
     */
    PokeApi serviceDetail;
    PokeApi serviceID;
    PokeApi serviceAbility;
    PokeApi serviceType;
    PokeApi serviceMove;

    private AllDataDownloader() {
    }

    public AllDataDownloader setupServices(final MainActivity _context) {
        Log.d(TAG, "start ");
        context = _context;

//        typeRealm = Realm.getInstance(context);
//        abilityRealm = Realm.getInstance(context);
//        detailRealm = Realm.getInstance(context);
//        moveRealm = Realm.getInstance(context);

//        typeRealm.beginTransaction();


        serviceDetail = new SimpleRestAdapter(
                PokeApi.POKEMON_API_ENDPOINT, new TypeToken<PokeDetail>() {
        }.getType(), PokeDetailDeserializer.getInstance(context)).getPokedexService();

        serviceID = new SimpleRestAdapter(
                PokeApi.POKEMON_API_ENDPOINT, new TypeToken<PokeID>() {
        }.getType(), PokeIdDeserializer.getInstance(context)).getPokedexService();

        serviceAbility = new SimpleRestAdapter(
                PokeApi.POKEMON_API_ENDPOINT, new TypeToken<PokeAbility>() {
        }.getType(), PokeAbilityDeserializer.getInstance(context)).getPokedexService();

        serviceType = new SimpleRestAdapter(
                PokeApi.POKEMON_API_ENDPOINT, new TypeToken<PokeType>() {
        }.getType(), PokeTypeDeserializer.getInstance(context)).getPokedexService();

        serviceMove = new SimpleRestAdapter(
                PokeApi.POKEMON_API_ENDPOINT, new TypeToken<PokeMove>() {
        }.getType(), PokeMoveDeserializer.getInstance()).getPokedexService();
        return this;
    }

    public void downloadData(final MainActivity context) {
        Log.d(TAG, "downloadData ");

        List<Integer> moves = new LinkedList<>();
        List<Integer> types = new LinkedList<>();
        List<Integer> abilities = new LinkedList<>();
        List<Integer> details = new LinkedList<>();
        for (int j = 1; j <= PokeApi.TYPES_COUNT; j++) types.add(j);
        for (int j = 1; j <= PokeApi.ABILITIES_COUNT; j++) abilities.add(j);
        for (int j = 1; j <= PokeApi.MOVES_COUNT; j++) moves.add(j);
        for (int j = 1; j <= 151/*PokeApi.POKEMONS_COUNT*/; j++) details.add(j);
        /**download data(explaination) O<T> means Observable<T>

         FILTER                                          FILTER                                  FILTER
         1 -> O<PokeMove> ->  1, PokeMove  ->  1, O<PokeDetail> -> PokeDetail -> O<PokeAbility> -> PokeAbility ->1, O<PokeType> PokeType
         2 -> O<PokeMove> ->  1, PokeMove  ->  1, O<PokeDetail> -> PokeDetail -> O<PokeAbility> -> PokeAbility ->2, O<PokeType> PokeType
         3 -> O<PokeMove> ->  1, PokeMove  ->  1, O<PokeDetail> -> PokeDetail -> O<PokeAbility> -> PokeAbility ->3, O<PokeType> PokeType
         .
         .
         626->O <PokeMove> ->626, PokeMove  ->493,O<PokeDetail> -> PokeDetail -> O<PokeAbility> -> PokeAbility->18, O<PokeType> PokeType

         */
        rx.Observable<Boolean> pokeMoves =
                Observable.from(moves).flatMap(new Func1<Integer, Observable<PokeMove>>() {
                    @Override
                    public Observable<PokeMove> call(Integer id) {
                        Log.d(TAG, "fetching " + id + " of 626 moves...");
                        return serviceMove.getPokemonMove(id);
                    }
                }).map(new Func1<PokeMove, Boolean>() {
                    @Override
                    public Boolean call(PokeMove pokeMove) {
                        DBManager.getInstance(context).savePokeMove(pokeMove);
                        return true;
                    }
                });
        rx.Observable<Boolean> pokeDetails =
                Observable.from(details).flatMap(new Func1<Integer, Observable<PokeDetail>>() {
                    @Override
                    public Observable<PokeDetail> call(Integer id) {
                        Log.d(TAG, "call observable<" + id + ">");
                        return serviceDetail.getPokemonDetail(id);
                    }
                }).map(new Func1<PokeDetail, Boolean>() {
                    @Override
                    public Boolean call(PokeDetail pokeDetail) {
                        DBManager.getInstance(context).savePokeDetail(pokeDetail);
                        return true;
                    }
                });
        rx.Observable<Boolean> pokeIDs =
                Observable.from(details).flatMap(new Func1<Integer, Observable<PokeID>>() {
                    @Override
                    public Observable<PokeID> call(Integer id) {
                        Log.d(TAG, "call observable<" + id + ">");
                        return serviceID.getPokemonByID(id);
                    }
                }).map(new Func1<PokeID, Boolean>() {
                    @Override
                    public Boolean call(PokeID poke) {
                        DBManager.getInstance(context).savePokeID(poke);
                        return true;
                    }
                });


        rx.Observable<Boolean> pokeAbilities =
                Observable.from(abilities).flatMap(new Func1<Integer, Observable<PokeAbility>>() {
                    @Override
                    public Observable<PokeAbility> call(Integer id) {
                        return serviceAbility.getPokemonAbility(id);
                    }
                }).map(new Func1<PokeAbility, Boolean>() {
                    @Override
                    public Boolean call(PokeAbility pokeAbility) {
                        DBManager.getInstance(context).savePokeAbility(pokeAbility);
                        return true;
                    }
                });



        pokeIDs.doOnSubscribe(new Action0() {
            @Override
            public void call() {
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        millisStart = System.currentTimeMillis();
                        context.showProgressBar(true);
                    }
                });
            }
        }).doOnCompleted(new Action0() {
            @Override
            public void call() {
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(TAG, "time elapsed: " + (System.currentTimeMillis() - millisStart) + " ms");
                        context.showProgressBar(false);
                    }
                });
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(Throwable throwable) {
                        SimpleRestAdapter.onError(TAG, throwable);
                        return Observable.empty();
                    }
                }).subscribe(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted ");
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(TAG, "time elapsed: " + (System.currentTimeMillis() - millisStart) + " ms");
                        context.showProgressBar(false);
                    }
                });
                SimpleRestAdapter.onCompleted(TAG, this);
            }

            @Override
            public void onError(Throwable e) {
//            SimpleRestAdapter.onErrorCompleted(TAG,this,e);
                SimpleRestAdapter.onError(TAG, e);
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(TAG, "time elapsed: " + (System.currentTimeMillis() - millisStart) + " ms");
                        context.showProgressBar(false);
                    }
                });
            }
            @Override
            public void onNext(Boolean aBoolean) {
                Log.d(TAG, "onNext ");
            }
        });
    }
}
