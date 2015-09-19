package lukasz.marczak.pl.gotta_catch_em_all.connection;

import android.util.Log;

import com.google.gson.reflect.TypeToken;

import java.util.LinkedList;
import java.util.List;

import io.realm.Realm;
import lukasz.marczak.pl.gotta_catch_em_all.JsonArium.PokeMoveDeserializer;
import lukasz.marczak.pl.gotta_catch_em_all.activities.MainActivity;
import lukasz.marczak.pl.gotta_catch_em_all.data.PokeMove;
import lukasz.marczak.pl.gotta_catch_em_all.data.realm.RealmMove;
import lukasz.marczak.pl.gotta_catch_em_all.data.realm.RealmType;
import retrofit.RetrofitError;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Func1;

/**
 * Created by Lukasz Marczak on 2015-09-18.
 */
public class MovesDownloader {
    public static final MovesDownloader INSTANCE = new MovesDownloader();
    public static final String TAG = MovesDownloader.class.getSimpleName();

    private MovesDownloader() {
    }

    public void start(final MainActivity context) {
        Log.d(TAG, "start ");
        List<Integer> moves = new LinkedList<>();
        for (int j = 1; j < 626; j++) {
            moves.add(j);
        }

        final PokeApi service = new SimpleRestAdapter(PokeApi.POKEMON_API_ENDPOINT, new TypeToken<PokeMove>() {
        }.getType(), PokeMoveDeserializer.INSTANCE).getPokedexService();

        Observable.from(moves).flatMap(new Func1<Integer, Observable<PokeMove>>() {
            @Override
            public Observable<PokeMove> call(Integer newType) {
                return service.getPokemonMove(newType);
            }
        }).onErrorResumeNext(new Func1<Throwable, Observable<PokeMove>>() {
            @Override
            public Observable<PokeMove> call(Throwable throwable) {
                Log.e(TAG, "error with");
                if (throwable instanceof RetrofitError)
                    Log.e(TAG, "url = " + ((RetrofitError) throwable).getUrl());

                return Observable.empty();
            }
        })
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d(TAG, "show loader");
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
                        Log.d(TAG, "hide loader");
                        context.showProgressBar(false);
                    }
                });
            }
        }).subscribe(new Subscriber<PokeMove>() {
            @Override
            public void onCompleted() {
                Realm realm = Realm.getInstance(context);
                List<RealmType> list = realm.where(RealmType.class).findAllSorted("id", true);
                for (RealmType t : list) {
                    Log.d(TAG, "type no. " + t.getId() + "," + t.getName());
                }
                SimpleRestAdapter.onCompleted(TAG, this);
            }

            @Override
            public void onError(Throwable e) {
                SimpleRestAdapter.onErrorCompleted(TAG, this, e);
            }

            @Override
            public void onNext(PokeMove pokeMove) {
                if (pokeMove == null) {
                    Log.e(TAG, "impossibru to got there");
                    return;
                }
                Log.d(TAG, "onNext " + pokeMove.getId() + "," + pokeMove.getName());
                Realm realm = Realm.getInstance(context);
                realm.beginTransaction();

                RealmMove move = realm.createObject(RealmMove.class);
                move.setId(pokeMove.getId());
                move.setPp(pokeMove.getPp());
                move.setName(pokeMove.getName());
                move.setPower(pokeMove.getPower());
                move.setCreated(pokeMove.getCreated());
                move.setAccuracy(pokeMove.getAccuracy());
                move.setCategory(pokeMove.getCategory());
                move.setModified(pokeMove.getModified());
                move.setDescription(pokeMove.getDescription());
                move.setResourceUri(pokeMove.getResourceUri());

                realm.commitTransaction();
                realm.close();
            }
        });
    }
}
















