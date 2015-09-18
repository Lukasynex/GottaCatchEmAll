package lukasz.marczak.pl.gotta_catch_em_all.connection;

import android.util.Log;

import com.google.gson.reflect.TypeToken;

import java.util.LinkedList;
import java.util.List;

import io.realm.Realm;
import lukasz.marczak.pl.gotta_catch_em_all.JsonArium.PokeTypeDeserializer;
import lukasz.marczak.pl.gotta_catch_em_all.activities.MainActivity;
import lukasz.marczak.pl.gotta_catch_em_all.data.PokeType;
import lukasz.marczak.pl.gotta_catch_em_all.data.realm.RealmType;
import retrofit.RetrofitError;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Func1;

/**
 * Created by Lukasz Marczak on 2015-09-18.
 */
public class TypesDownloader {
    public static final TypesDownloader INSTANCE = new TypesDownloader();
    public static final String TAG = TypesDownloader.class.getSimpleName();

    private TypesDownloader() {
    }

    public void start(final MainActivity context) {
        Log.d(TAG, "start ");
        List<Integer> types = new LinkedList<>();
        for (int j = 1; j < 19; j++) {
            types.add(j);
        }

        final PokeApi service = new SimpleRestAdapter(PokeApi.POKEMON_API_ENDPOINT, new TypeToken<PokeType>() {
        }.getType(), PokeTypeDeserializer.INSTANCE).getPokedexService();

        Observable.from(types).flatMap(new Func1<Integer, Observable<PokeType>>() {
            @Override
            public Observable<PokeType> call(Integer newType) {
                return service.getPokemonType(newType);
            }
        }).onErrorResumeNext(new Func1<Throwable, Observable<? extends PokeType>>() {
            @Override
            public Observable<? extends PokeType> call(Throwable throwable) {
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
        }).subscribe(new Subscriber<PokeType>() {
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
            public void onNext(PokeType pokeType) {
                if (pokeType == null)
                    return;
                Log.d(TAG, "onNext " + pokeType.getId() + "," + pokeType.getName());
                Realm realm = Realm.getInstance(context);
                realm.beginTransaction();

                RealmType type = realm.createObject(RealmType.class);
                type.setId(pokeType.getId());
                type.setName(pokeType.getName());
                type.setWeakness(pokeType.getWeakness());
                type.setIneffective(pokeType.getIneffective());
                type.setSuperEffective(pokeType.getSuperEffective());
                realm.commitTransaction();
                realm.close();
            }
        });
    }
}
