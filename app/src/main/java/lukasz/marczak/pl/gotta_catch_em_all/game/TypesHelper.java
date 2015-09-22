package lukasz.marczak.pl.gotta_catch_em_all.game;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import io.realm.Realm;
import lukasz.marczak.pl.gotta_catch_em_all.data.PokeType;
import lukasz.marczak.pl.gotta_catch_em_all.data.realm.RealmType;

/**
 * Created by Lukasz Marczak on 2015-09-22.
 */
public class TypesHelper {
    public static final String TAG = TypesHelper.class.getSimpleName();
    private static Context context;
    private static final TypesHelper instance = new TypesHelper();

    public static TypesHelper getInstance(Context c) {
        context = c;
        return instance;
    }

    public String getMatchingComment(@NonNull PokeType yourPoke, @NonNull PokeType opponent, boolean opponenHits) {
        Log.d(TAG, "getMatchingComment ");

        Realm r = Realm.getInstance(context);
        r.beginTransaction();
        RealmType yourPokeType = r.where(RealmType.class).equalTo("id", yourPoke.getId()).findFirst();
        RealmType opponentType = r.where(RealmType.class).equalTo("id", opponent.getId()).findFirst();

        if (yourPokeType == null || opponentType == null)
            throw new NullPointerException("Wrong parameters here!!!");

        String message;
        if (opponenHits) {
            String yourPokeName = yourPoke.getName();
            message = getMatchingMessage(opponentType, yourPokeName);
        } else {
            String opponentName = opponentType.getName();
            message = getMatchingMessage(yourPokeType, opponentName);
        }
        r.commitTransaction();
        r.close();
        return message;
    }

    @NonNull
    private String getMatchingMessage(RealmType opponentType, String yourPokeName) {
        if (opponentType.getSuperEffective().contains(yourPokeName))
            return "It's super effective!!!";
        else if (opponentType.getIneffective().contains(yourPokeName))
            return "It's not very effective...";
        else if (opponentType.getWeakness().contains(yourPokeName))
            return "No effect!";
        else return "...";
    }

}
