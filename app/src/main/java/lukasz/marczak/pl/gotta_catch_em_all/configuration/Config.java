package lukasz.marczak.pl.gotta_catch_em_all.configuration;

/**
 * Created by Lukasz Marczak on 2015-08-23.
 */
public class Config {

    public interface FRAGMENT {
        int TRIP = 0;
        int RANGE = 1;
        int OPTIONS = 2;
        int STATS = 3;
        int POKEDEX = 4;
    }

    public static FRAGMENT CURRENT_FRAGMENT;

    public interface IntentCode {

        int FAILED_WILD_FIGHT = 1;
        int FAILED_PERSONAL_FIGHT = 2;
        int WON_WILD_FIGHT = 3;
        int WON_PERSONAL_FIGHT = 4;
        int START_WILD_FIGHT = 5;
        int START_PERSONAL_FIGHT = 6;
        int EXIT_WILD_FIGHT = 7;
    }
}
