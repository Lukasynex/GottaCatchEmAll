package lukasz.marczak.pl.gotta_catch_em_all.fragments;

import android.app.Activity;

/**
 * Created by Lukasz Marczak on 2015-09-20.
 */
public interface Fightable extends Progressable{

    void decreasePoke(int i);

    void decreaseOpponent(int i);
}
