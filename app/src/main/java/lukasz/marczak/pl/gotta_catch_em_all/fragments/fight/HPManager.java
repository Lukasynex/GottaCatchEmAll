package lukasz.marczak.pl.gotta_catch_em_all.fragments.fight;

import android.widget.TextView;

/**
 * Created by Lukasz Marczak on 2015-09-14.
 */
public class HPManager {
    private static final int FULL_HP = 75 + 65;
    private static final int NULL_HP = 0;
    public static HPManager INSTANCE = new HPManager();

    private HPManager() {
    }

    //75 for full, 65 for empty
    private int emptyOppHP = 75, fullOppHP = 65;
    private int emptyPokeHP = 75, fullPokeHP = 65;
    private TextView emptyOpp, emptyPoke;
    private TextView fullOpp, fullPoke;
    private TextView levelOpp, levelPoke;
    private TextView yourPokeHP, opponentHP;

    public void injectOpponentStats(TextView empty, TextView full, TextView level, TextView HP) {
        emptyOpp = empty;
        fullOpp = full;
        levelOpp = level;
        opponentHP = HP;
        fullOpp.setWidth(FULL_HP);
        emptyOpp.setWidth(NULL_HP);
    }

    public void injectYourPokemonStats(TextView empty, TextView full, TextView level, TextView HP) {
        emptyPoke = empty;
        fullPoke = full;
        levelPoke = level;
        fullPoke.setWidth(FULL_HP);
        emptyPoke.setWidth(NULL_HP);
        yourPokeHP = HP;
    }

    public static boolean recentSelection;

    public boolean decreaseHP(int value, boolean isPoke) {
        TextView empty = isPoke ? emptyPoke : emptyOpp;
        TextView full = isPoke ? fullPoke : fullOpp;
        recentSelection = isPoke;

        int pixelsEmpty = empty.getWidth();
        int pixelsFull = full.getWidth();
        int scaledValue = (int) (value * 1.4);
        if (pixelsFull - scaledValue < NULL_HP) {
            full.setWidth(NULL_HP);
            empty.setWidth(FULL_HP);
            return true;
        } else {
            full.setWidth(pixelsFull - scaledValue);
            empty.setWidth(pixelsEmpty + scaledValue);
            return false;
        }
    }
}
