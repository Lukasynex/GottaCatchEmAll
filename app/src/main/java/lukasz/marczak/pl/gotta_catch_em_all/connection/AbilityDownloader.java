package lukasz.marczak.pl.gotta_catch_em_all.connection;

import android.util.Log;

import com.google.gson.reflect.TypeToken;

import java.util.LinkedList;
import java.util.List;

import io.realm.Realm;
import lukasz.marczak.pl.gotta_catch_em_all.JsonArium.PokeAbilityDeserializer;
import lukasz.marczak.pl.gotta_catch_em_all.activities.MainActivity;
import lukasz.marczak.pl.gotta_catch_em_all.data.PokeAbility;
import lukasz.marczak.pl.gotta_catch_em_all.data.PokeMove;
import lukasz.marczak.pl.gotta_catch_em_all.data.realm.RealmAbility;
import retrofit.RetrofitError;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Func1;

/**
 * Created by Lukasz Marczak on 2015-09-18.
 */
public class AbilityDownloader {
    public static final AbilityDownloader INSTANCE = new AbilityDownloader();
    public static final String TAG = AbilityDownloader.class.getSimpleName();

    private AbilityDownloader() {
    }

    public void start(final MainActivity context) {
        Log.d(TAG, "start ");
        List<Integer> ability = new LinkedList<>();
        for (int j = 1; j < 248; j++) {
            ability.add(j);
        }

        final PokeApi service = new SimpleRestAdapter(PokeApi.POKEMON_API_ENDPOINT, new TypeToken<PokeMove>() {
        }.getType(), PokeAbilityDeserializer.INSTANCE).getPokedexService();

        Observable.from(ability).flatMap(new Func1<Integer, Observable<PokeAbility>>() {
            @Override
            public Observable<PokeAbility> call(Integer newType) {
                return service.getPokemonAbility(newType);
            }
        }).onErrorResumeNext(new Func1<Throwable, Observable<PokeAbility>>() {
            @Override
            public Observable<PokeAbility> call(Throwable throwable) {
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
        }).subscribe(new Subscriber<PokeAbility>() {
            @Override
            public void onCompleted() {
                Realm realm = Realm.getInstance(context);
                List<RealmAbility> list = realm.where(RealmAbility.class).findAllSorted("id", true);
                for (RealmAbility t : list) {
                    Log.d(TAG, "type no. " + t.getId() + "," + t.getName());
                }
                SimpleRestAdapter.onCompleted(TAG, this);
            }

            @Override
            public void onError(Throwable e) {
                SimpleRestAdapter.onErrorCompleted(TAG, this, e);
            }

            @Override
            public void onNext(PokeAbility pokeAbility) {
                if (pokeAbility == null) {
                    Log.e(TAG, "impossibru to got there");
                    return;
                }
                Log.d(TAG, "onNext " + pokeAbility.getId() + "," + pokeAbility.getName());
                Realm realm = Realm.getInstance(context);
                realm.beginTransaction();

                RealmAbility move = realm.createObject(RealmAbility.class);
                move.setId(pokeAbility.getId());
                move.setName(pokeAbility.getName());
                move.setCreated(pokeAbility.getCreated());
                move.setModified(pokeAbility.getModified());
                move.setDescription(pokeAbility.getDescription());
                move.setResourceUri(pokeAbility.getResourceUri());

                realm.commitTransaction();
                realm.close();
            }
        });
    }
}
















