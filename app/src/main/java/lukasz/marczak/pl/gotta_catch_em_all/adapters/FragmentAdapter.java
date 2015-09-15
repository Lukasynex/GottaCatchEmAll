package lukasz.marczak.pl.gotta_catch_em_all.adapters;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import lukasz.marczak.pl.gotta_catch_em_all.fragments.ChoosePokemonFragment;

/**
 * Created by Lukasz Marczak on 2015-09-15.
 */
public class FragmentAdapter extends FragmentPagerAdapter {

    private final String startPokes[] = {"bulbasaur", "charmander", "squirtle"};

    public FragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        return ChoosePokemonFragment.newInstance("Fragment " + i, startPokes[i]);
    }

    @Override
    public int getCount() {
        return startPokes.length;
    }

}
