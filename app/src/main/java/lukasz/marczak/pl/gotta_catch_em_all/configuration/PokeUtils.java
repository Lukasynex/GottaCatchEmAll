package lukasz.marczak.pl.gotta_catch_em_all.configuration;

/**
 * Created by Lukasz Marczak on 2015-08-24.
 */
public class PokeUtils {

    public static String getPokeResByID(int ID) {
        return "http://pokeapi.co/media/img/" + ID + ".png";
    }
}
