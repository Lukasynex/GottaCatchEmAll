package lukasz.marczak.pl.gotta_catch_em_all.configuration;

import java.util.Random;

/**
 * Created by Lukasz Marczak on 2015-09-14.
 */
public class Randy {
    public static final Random generator = new Random();

    public static int from(int j) {
        return generator.nextInt(j < 0 ? -j : j);

    }
}