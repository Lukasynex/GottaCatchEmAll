package lukasz.marczak.pl.gotta_catch_em_all.fragments.fight;

import lukasz.marczak.pl.gotta_catch_em_all.configuration.Randy;
import lukasz.marczak.pl.gotta_catch_em_all.data.Attack;

/**
 * Created by Lukasz Marczak on 2015-09-16.
 */
public class FightEngine {
    public void opponentMove(String opponentName) {
        Attack atk = Randy.getRandomAttackFor(opponentName);
    }
}
