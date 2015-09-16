package lukasz.marczak.pl.gotta_catch_em_all.fragments.fight;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import lukasz.marczak.pl.gotta_catch_em_all.R;
import lukasz.marczak.pl.gotta_catch_em_all.activities.FightActivity;
import lukasz.marczak.pl.gotta_catch_em_all.activities.MainActivity;
import lukasz.marczak.pl.gotta_catch_em_all.adapters.MyPokesAdapter;
import lukasz.marczak.pl.gotta_catch_em_all.configuration.PokeUtils;
import lukasz.marczak.pl.gotta_catch_em_all.configuration.Randy;
import lukasz.marczak.pl.gotta_catch_em_all.connection.PokeSpritesManager;


public class FightRunningFragment extends Fragment {

    final static String TAG = FightRunningFragment.class.getSimpleName();
    ImageView yourPokemon, opponentPokemon;
    FightActivity parentActivity;
    String opponentName = "";
    boolean strongPokemon = Randy.randomAnswer();
    ProgressBar opponentHP;
    ProgressBar yourPokeHP;
    TextView yourPokeNameTV;
    TextView question;

    public static FightRunningFragment newInstance(String opponent) {
        FightRunningFragment fragment = new FightRunningFragment();
        fragment.opponentName = opponent;
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
        yourPokemon = (ImageView) view.findViewById(R.id.your_pokemon_back);
        opponentPokemon = (ImageView) view.findViewById(R.id.opponent_front);
        TextView fight = (TextView) view.findViewById(R.id.fight);
        TextView bag = (TextView) view.findViewById(R.id.bag);
        TextView select = (TextView) view.findViewById(R.id.select_pokemon);
        TextView run = (TextView) view.findViewById(R.id.run);
        question = (TextView) view.findViewById(R.id.question);
        question.setText("What will \n " + PokeUtils.getPrettyPokemonName(MainActivity.pokeName) + "\n do?");

        fight.setOnTouchListener(getTouchListener(0, fight));
        bag.setOnTouchListener(getTouchListener(1, bag));
        select.setOnTouchListener(getTouchListener(2, select));
        run.setOnTouchListener(getTouchListener(3, run));

        RelativeLayout includedOpponentBar = (RelativeLayout) view.findViewById(R.id.opponent_front_status);
        RelativeLayout yourPokemonBar = (RelativeLayout) view.findViewById(R.id.your_pokemon_back_status);
        TextView opponentNameTV = (TextView) includedOpponentBar.findViewById(R.id.pokemon_name);
        yourPokeNameTV = (TextView) yourPokemonBar.findViewById(R.id.pokemon_name);

        TextView opponentLevel = (TextView) includedOpponentBar.findViewById(R.id.level);
        TextView yourPokeLevel = (TextView) yourPokemonBar.findViewById(R.id.level);

        //HP indicators
        opponentHP = (ProgressBar) includedOpponentBar.findViewById(R.id.progressBar2);
        yourPokeHP = (ProgressBar) yourPokemonBar.findViewById(R.id.progressBar2);

        yourPokeHP.setProgress(100);
        opponentHP.setProgress(75);

        opponentLevel.setText("Level " + Randy.from(10));
        yourPokeLevel.setText("Level 4");

//        HPManager.INSTANCE.injectOpponentStats(opponentEmptyHP, opponentFullHP, opponentLevel, opponentHP);
//        HPManager.INSTANCE.injectYourPokemonStats(yourPokeEmptyHP, yourPokeFullHP, yourPokeLevel, yourPokeHP);

        opponentNameTV.setText(PokeUtils.getPrettyPokemonName(opponentName));
        yourPokeNameTV.setText(PokeUtils.getPrettyPokemonName(MainActivity.pokeName));

        Picasso.with(getActivity()).load(getOppponentPokemonResource()).into(opponentPokemon);
        Picasso.with(getActivity()).load(getYourPokemonResource()).into(yourPokemon);

        return view;
    }

    private View.OnTouchListener getTouchListener(final int mode, final TextView view) {
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(TAG, "onTouch");
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_UP: {
                        Log.d(TAG, "action up");
                        view.setBackgroundColor(Color.TRANSPARENT);
                        break;
                    }
                    case MotionEvent.ACTION_DOWN: {
                        Log.d(TAG, "action down");
                        view.setBackgroundColor(Color.LTGRAY);
                        switch (mode) {
                            case 2: {
                                new SelectMenuEngine.POKEMON(getActivity()) {

                                    @Override
                                    public void onPokemonChosen(final int position) {
                                        Log.d(TAG, "onPokemonChosen ");
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Log.d(TAG, "run ");
                                                String newPokeName = MyPokesAdapter.dataset.get(position).getName();
                                                String newPokeResource = PokeSpritesManager
                                                        .getPokemonBackByName(newPokeName);
                                                Picasso.with(getActivity()).load(newPokeResource).into(yourPokemon);
                                                String prettyName = PokeUtils.getPrettyPokemonName(newPokeName);
                                                yourPokeNameTV.setText(prettyName);
                                                question.setText("What will \n" + prettyName + " do?");
                                            }
                                        });
                                    }
                                };
                                break;
                            }
                            case 3: {
                                if (!strongPokemon) parentActivity.onBackPressed();
                                else
                                    Snackbar.make(getView(), "Cannot run this fight!", Snackbar.LENGTH_LONG).show();
                                break;
                            }
                            default:
                                Log.d(TAG, "mode " + mode);
                        }
                        break;
                    }
                }
                return true;
            }
        };
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        parentActivity = (FightActivity) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public String getYourPokemonResource() {
        return PokeSpritesManager.getPokemonBackByName(MainActivity.pokeName);
    }

    public String getOppponentPokemonResource() {
        return PokeSpritesManager.getPokemonFrontByName(opponentName);
    }
}
