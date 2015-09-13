package lukasz.marczak.pl.gotta_catch_em_all.connection;

/**
 * Created by Lukasz Marczak on 2015-08-25.
 */
public class PokeSpritesManager {

    public static String getMainPokeByName(String pokemonName) {
        if(pokemonName.isEmpty())
            pokemonName="pikachu";
        return "http://img.pokemondb.net/artwork/" + pokemonName + ".jpg";
    }

    public static String getPokemonBackByName(String pokomonName) {
        return "http://img.pokemondb.net/sprites/heartgold-soulsilver/back-normal/" + pokomonName + ".png";
    }
    public static String getPokemonFrontByName(String pokomonName) {
        return "http://img.pokemondb.net/sprites/heartgold-soulsilver/normal/" + pokomonName + ".png";
    }

}
