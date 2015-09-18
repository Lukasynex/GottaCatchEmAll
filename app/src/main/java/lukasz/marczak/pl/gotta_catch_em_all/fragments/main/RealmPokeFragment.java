package lukasz.marczak.pl.gotta_catch_em_all.fragments.main;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tt.whorlviewlibrary.WhorlView;

import lukasz.marczak.pl.gotta_catch_em_all.R;
import lukasz.marczak.pl.gotta_catch_em_all.adapters.RealmPokeAdapter;

public class RealmPokeFragment extends Fragment {

    public static final String TAG = RealmPokeFragment.class.getSimpleName();

    RealmPokeAdapter pokedexAdapter;
    RecyclerView recyclerView;
    WhorlView progressBar;

    public static RealmPokeFragment newInstance() {
        RealmPokeFragment fragment = new RealmPokeFragment();
        return fragment;
    }

    public RealmPokeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pokedex, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        progressBar = (WhorlView) view.findViewById(R.id.whorlView);
        progressBar.setVisibility(View.GONE);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        pokedexAdapter = new RealmPokeAdapter(this);
        recyclerView.setAdapter(pokedexAdapter);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated()");
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
