package lukasz.marczak.pl.gotta_catch_em_all.fragments.fight;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import lukasz.marczak.pl.gotta_catch_em_all.R;


public class FightRunningFragment extends Fragment {
    public static FightRunningFragment newInstance() {
        FightRunningFragment fragment = new FightRunningFragment();
        return fragment;
    }

    public FightRunningFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fight_running, container, false);
        return view;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
