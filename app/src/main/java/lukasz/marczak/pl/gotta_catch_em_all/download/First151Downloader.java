package lukasz.marczak.pl.gotta_catch_em_all.download;

import android.util.Log;

import com.google.gson.reflect.TypeToken;

import java.util.LinkedList;
import java.util.List;

import lukasz.marczak.pl.gotta_catch_em_all.JsonArium.PokeIdDeserializer;
import lukasz.marczak.pl.gotta_catch_em_all.activities.MainActivity;
import lukasz.marczak.pl.gotta_catch_em_all.connection.PokeApi;
import lukasz.marczak.pl.gotta_catch_em_all.connection.SimpleRestAdapter;
import lukasz.marczak.pl.gotta_catch_em_all.data.PokeID;
import lukasz.marczak.pl.gotta_catch_em_all.data.realm.DBManager;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Lukasz Marczak on 2015-09-19.
 */
public class First151Downloader {
    public static final First151Downloader INSTANCE = new First151Downloader();
    public static final String TAG = First151Downloader.class.getSimpleName();

    private static long millisStart;
    PokeApi serviceID;

    private First151Downloader() {
    }

    public First151Downloader setupServices(final MainActivity _context) {
        Log.d(TAG, "start ");
        serviceID = new SimpleRestAdapter(
                PokeApi.POKEMON_API_ENDPOINT, new TypeToken<PokeID>() {
        }.getType(), PokeIdDeserializer.getInstance(_context)).getPokedexService();
        return this;
    }

    public void downloadData(final MainActivity context) {
        Log.d(TAG, "downloadData ");

        List<Integer> details = new LinkedList<>();
        for (int j = 1; j <= 151; j++) details.add(j);


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
        }).doOnSubscribe(new Action0() {
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
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(TAG, "time elapsed: " + (System.currentTimeMillis() - millisStart) + " ms");
                        context.showProgressBar(false);
                    }
                });
                SimpleRestAdapter.onErrorCompleted(TAG, this, e);
            }

            @Override
            public void onNext(Boolean aBoolean) {
                Log.d(TAG, "onNext ");
            }
        });
    }
}
