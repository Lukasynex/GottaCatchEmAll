package lukasz.marczak.pl.gotta_catch_em_all.game;

import android.os.Handler;
import android.util.Log;

import java.util.List;

import lukasz.marczak.pl.gotta_catch_em_all.configuration.Config;
import lukasz.marczak.pl.gotta_catch_em_all.configuration.Randy;
import lukasz.marczak.pl.gotta_catch_em_all.data.PokeDetail;
import lukasz.marczak.pl.gotta_catch_em_all.data.PokeMove;
import lukasz.marczak.pl.gotta_catch_em_all.data.realm.DBManager;
import lukasz.marczak.pl.gotta_catch_em_all.download.MovesDownloader;
import lukasz.marczak.pl.gotta_catch_em_all.fragments.Fightable;
import lukasz.marczak.pl.gotta_catch_em_all.fragments.Progressable;

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

    public void allowMoveOpponent(final Fightable context) {
        Log.d(TAG, "allowMoveOpponent ");
        Config.IDLE_STATE = true;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                context.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        context.setText("???");
                    }
                });
            }
        }, 500);


        final List<Integer> moves = DBManager.extractMoves(Engine.INSTANCE.getOpponentPoke().getDetail().getMoves());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run ");
                new MovesDownloader() {
                    @Override
                    public void onDataReceived(List<PokeMove> moves) {
                        Log.d(TAG, "onDataReceived ");
                        final PokeMove randomMove = moves.get(Randy.from(moves.size()));
                        Log.d(TAG, "Opponent used " + randomMove.getName());
                        context.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                context.setText("Opponent used " + randomMove.getName());
                                context.decreasePoke(randomMove.getPower()/3);
                            }
                        });
                    }
                }.start(context, moves);
            }
        }, 2000);
        Config.IDLE_STATE = false;
    }
}
