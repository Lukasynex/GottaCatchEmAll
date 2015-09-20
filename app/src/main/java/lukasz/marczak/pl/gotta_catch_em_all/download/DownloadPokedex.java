package lukasz.marczak.pl.gotta_catch_em_all.download;

import android.util.Log;

import com.google.gson.reflect.TypeToken;

import java.util.LinkedList;
import java.util.List;

import io.realm.Realm;
import lukasz.marczak.pl.gotta_catch_em_all.JsonArium.PokeNetNameDeserializer;
import lukasz.marczak.pl.gotta_catch_em_all.activities.MainActivity;
import lukasz.marczak.pl.gotta_catch_em_all.configuration.PokeUtils;
import lukasz.marczak.pl.gotta_catch_em_all.connection.PokeApi;
import lukasz.marczak.pl.gotta_catch_em_all.connection.SimpleRestAdapter;
import lukasz.marczak.pl.gotta_catch_em_all.data.PokeID;
import lukasz.marczak.pl.gotta_catch_em_all.data.realm.RealmID;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

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

        final PokeApi service = new SimpleRestAdapter(
                PokeApi.POKEMON_API_ENDPOINT, new TypeToken<PokeID>() {
        }.getType(), PokeNetNameDeserializer.INSTANCE).getPokedexService();


        List<Integer> all_150 = new LinkedList<>();
        for (int j = 1; j < 718; j++) {
            all_150.add(j);
        }


        Observable.from(all_150).flatMap(new Func1<Integer, Observable<PokeID>>() {
            @Override
            public Observable<PokeID> call(Integer integer) {
                Log.d(TAG, "call ");
                return service.getPokemonByID(integer);
            }
        }).onErrorResumeNext(new Func1<Throwable, Observable<PokeID>>() {
            @Override
            public Observable<PokeID> call(Throwable throwable) {
                Log.d(TAG, "call empty realmPoke");
                Log.d(TAG, throwable.getMessage());
                Log.d(TAG, throwable.getCause().toString());
                return Observable.empty();
            }
        }).subscribe(new Subscriber<PokeID>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted in " + (System.currentTimeMillis() - startMillis));
                Realm realm = Realm.getInstance(context);
                realm.beginTransaction();

                for (PokeID netPoke : PokeUtils.netPokes) {
                    RealmID poke = realm.createObject(RealmID.class); // Create a new object
                    poke.setName(netPoke.getName());
                    poke.setId(netPoke.getId());
                }
                realm.commitTransaction();
//                        realm.close();
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
            public void onNext(PokeID realPoke) {
                Log.d(TAG, "onNext " + realPoke.getId());
                PokeUtils.netPokes.add(realPoke);
            }
        });
    }
}
