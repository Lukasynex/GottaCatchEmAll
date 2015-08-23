package lukasz.marczak.pl.gotta_catch_em_all.configuration;

/**
 * Created by Lukasz Marczak on 2015-08-23.
 *
 */
public class Config {

    public static String[] HEADERS = {"Trip", "Pokedex", "Range", "Options", "Stats", "Exit"};
    public final static int MORE_POKES = 8;
    public final static int MAX_POKES = 150;

    public interface FRAGMENT {
        int TRIP = 0;
        int POKEDEX = 1;
        int RANGE = 2;
        int OPTIONS = 3;
        int STATS = 4;
        int EXIT = 5;
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
