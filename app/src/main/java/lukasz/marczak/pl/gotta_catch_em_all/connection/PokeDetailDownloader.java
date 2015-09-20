package lukasz.marczak.pl.gotta_catch_em_all.connection;

import android.util.Log;

import com.google.gson.reflect.TypeToken;

import java.util.LinkedList;
import java.util.List;

import io.realm.Realm;
import lukasz.marczak.pl.gotta_catch_em_all.JsonArium.PokeDetailDeserializer;
import lukasz.marczak.pl.gotta_catch_em_all.activities.MainActivity;
import lukasz.marczak.pl.gotta_catch_em_all.data.PokeDetail;
import lukasz.marczak.pl.gotta_catch_em_all.data.realm.RealmPokeDetail;
import lukasz.marczak.pl.gotta_catch_em_all.data.realm.RealmType;
import retrofit.RetrofitError;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Func1;

/**
 * Created by Lukasz Marczak on 2015-09-18.
 */
public class PokeDetailDownloader {
    public static final PokeDetailDownloader INSTANCE = new PokeDetailDownloader();
    public static final String TAG = PokeDetailDownloader.class.getSimpleName();

    private PokeDetailDownloader() {
    }

    public void start(final MainActivity context) {
        Log.d(TAG, "start ");
        List<Integer> pokemons = new LinkedList<>();
        for (int j = 1; j < 719; j++) {
            pokemons.add(j);
        }

        final PokeApi service = new SimpleRestAdapter(PokeApi.POKEMON_API_ENDPOINT, new TypeToken<PokeDetail>() {
        }.getType(), PokeDetailDeserializer.getInstance(null)).getPokedexService();

        Observable.from(pokemons).flatMap(new Func1<Integer, Observable<PokeDetail>>() {
            @Override
            public Observable<PokeDetail> call(Integer newType) {
                return service.getPokemonDetail(newType);
            }
        }).onErrorResumeNext(new Func1<Throwable, Observable<PokeDetail>>() {
            @Override
            public Observable<PokeDetail> call(Throwable throwable) {
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
        }).subscribe(new Subscriber<PokeDetail>() {
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
            public void onNext(PokeDetail pokeDetail) {
                if (pokeDetail == null) {
                    Log.e(TAG, "impossibru to got there");
                    return;
                }
                Log.d(TAG, "onNext : " + pokeDetail.getName());
                Realm realm = Realm.getInstance(context);
                realm.beginTransaction();

                RealmPokeDetail detail = realm.createObject(RealmPokeDetail.class);

                detail.setHp(pokeDetail.getHp());
                detail.setExp(pokeDetail.getExp());
                detail.setName(pokeDetail.getName());
                detail.setTotal(pokeDetail.getTotal());
                detail.setSpAtk(pokeDetail.getSpAtk());
                detail.setSpDef(pokeDetail.getSpDef());
                detail.setSpeed(pokeDetail.getSpeed());
                detail.setPkdxId(pokeDetail.getPkdxId());
                detail.setAttack(pokeDetail.getAttack());
                detail.setDefense(pokeDetail.getDefense());
                detail.setEggCycles(pokeDetail.getEggCycles());
                detail.setCatchRate(pokeDetail.getCatchRate());
                detail.setHappiness(pokeDetail.getHappiness());
                detail.setNationalId(pokeDetail.getNationalId());

                realm.commitTransaction();
                realm.close();
            }
        });
    }
}