package lukasz.marczak.pl.gotta_catch_em_all.configuration;

import java.util.Random;

import lukasz.marczak.pl.gotta_catch_em_all.data.Attack;

/**
 * Created by Lukasz Marczak on 2015-09-14.
 * Apply randomness to game!!!
 */
public class Randy {
    public static final Random generator = new Random();

    public static int from(int j) {
        return generator.nextInt(j < 0 ? -j : j);

    }

    public static boolean randomAnswer() {
        return generator.nextBoolean();
    }
}
