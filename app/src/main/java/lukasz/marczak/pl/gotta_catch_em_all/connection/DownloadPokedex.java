package lukasz.marczak.pl.gotta_catch_em_all.connection;

import android.content.Context;
import android.util.Log;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import io.realm.Realm;
import lukasz.marczak.pl.gotta_catch_em_all.JsonArium.PokeNetNameDeserializer;
import lukasz.marczak.pl.gotta_catch_em_all.activities.MainActivity;
import lukasz.marczak.pl.gotta_catch_em_all.data.NetPoke;
import lukasz.marczak.pl.gotta_catch_em_all.data.RealmPoke;
import retrofit.client.Response;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * Created by Lukasz Marczak on 2015-09-13.
 */
public class DownloadPokedex {


    public static String TAG = DownloadPokedex.class.getSimpleName();

    public static DownloadPokedex INSTANCE = new DownloadPokedex();

    private DownloadPokedex() {
    }

    private long startMillis = 0;

    public void start(final MainActivity context) {
        Log.d(TAG, "start ");
//
//        Realm.getInstance(context).beginTransaction();
//        while (Realm.getInstance(context).allObjects(RealmPoke.class).size() > 0) {
//            Log.d(TAG, "removing item ");
//            Realm.getInstance(context).allObjects(RealmPoke.class).removeLast();
//        }
//        Realm.getInstance(context).commitTransaction();


        startMillis = System.currentTimeMillis();

        final PokedexService service = new SimpleRestAdapter(
                PokedexService.POKEDEX_NAME_ENDPOINT, new TypeToken<String>() {
        }.getType(),
                PokeNetNameDeserializer.INSTANCE).getPokedexService();


        List<Integer> all_150 = new LinkedList<>();
        for (int j = 1; j < 152; j++) {
            all_150.add(j);
        }

        Observable.from(all_150).flatMap(new Func1<Integer, Observable<String>>() {
            @Override
            public Observable<String> call(Integer integer) {
                Log.d(TAG, "call ");
                return service.getPokemonByID(integer);
            }
        }, new Func2<Integer, String, NetPoke>() {
            @Override
            public NetPoke call(Integer id, String name) {
                return new NetPoke(id, name);
            }
        }).map(new Func1<NetPoke, RealmPoke>() {
            @Override
            public RealmPoke call(NetPoke netPoke) {
                Log.d(TAG, "call ");
                Realm realm = Realm.getInstance(context);

                realm.beginTransaction();
                RealmPoke poke = realm.createObject(RealmPoke.class); // Create a new object
                poke.setName(netPoke.getName());
                poke.setId(String.valueOf(netPoke.getID()-1));
                realm.commitTransaction();

                return poke;
            }
        }).onErrorResumeNext(new Func1<Throwable, Observable<RealmPoke>>() {
            @Override
            public Observable<RealmPoke> call(Throwable throwable) {
                Log.d(TAG, "call empty realmPoke");
                Log.d(TAG, throwable.getMessage());
                Log.d(TAG, throwable.getCause().toString());
                return Observable.empty();
            }
        }).subscribe(new Subscriber<RealmPoke>() {
            @Override
            public void onCompleted() {

                Log.d(TAG, "onCompleted in " + (System.currentTimeMillis() - startMillis));
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        context.showProgressBar(false);
                    }
                });
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError ");
                Log.e(TAG, "message: " + e.getMessage());
                Log.e(TAG, "cause: " + e.getCause());
            }

            @Override
            public void onNext(RealmPoke realmPoke) {
                Log.d(TAG, "onNext " + realmPoke.getId());
            }
        });
    }
}
