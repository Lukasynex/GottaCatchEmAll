package lukasz.marczak.pl.gotta_catch_em_all.game;

import android.util.Log;

import lukasz.marczak.pl.gotta_catch_em_all.data.PokeDetail;

/**
 * Created by Lukasz Marczak on 2015-09-20.
 */
public class Engine {
    public static final String TAG = Engine.class.getSimpleName();
    public static final Engine INSTANCE = new Engine();

    Player yourPoke;
    Player opponentPoke;

    public Player getYourPoke() {
        return yourPoke;
    }

    public void setYourPoke(Player yourPoke) {
        this.yourPoke = yourPoke;
    }

    public Player getOpponentPoke() {
        return opponentPoke;
    }

    public void setOpponentPoke(Player opponentPoke) {
        this.opponentPoke = opponentPoke;
    }

    private Engine() {
        yourPoke = new Player();
        opponentPoke = new Player();
    }

    public void setOpponentDetail(PokeDetail detail) {
        Log.d(TAG, "setOpponentDetail ");
        opponentPoke.setDetail(detail);
    }

    public void setYourPokeDetail(PokeDetail detail) {
        Log.d(TAG, "setOpponentDetail ");
        yourPoke.setDetail(detail);
    }
}
