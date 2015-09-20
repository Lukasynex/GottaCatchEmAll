package lukasz.marczak.pl.gotta_catch_em_all.fragments.fight;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tt.whorlviewlibrary.WhorlView;

import java.io.File;
import java.util.List;

import io.realm.Realm;
import lukasz.marczak.pl.gotta_catch_em_all.R;
import lukasz.marczak.pl.gotta_catch_em_all.activities.FightActivity;
import lukasz.marczak.pl.gotta_catch_em_all.activities.MainActivity;
import lukasz.marczak.pl.gotta_catch_em_all.adapters.MyPokesAdapter;
import lukasz.marczak.pl.gotta_catch_em_all.adapters.PokeAttacksAdapter;
import lukasz.marczak.pl.gotta_catch_em_all.configuration.Config;
import lukasz.marczak.pl.gotta_catch_em_all.configuration.PokeUtils;
import lukasz.marczak.pl.gotta_catch_em_all.configuration.Randy;
import lukasz.marczak.pl.gotta_catch_em_all.connection.PokeDetailDownloader;
import lukasz.marczak.pl.gotta_catch_em_all.connection.PokeSpritesManager;
import lukasz.marczak.pl.gotta_catch_em_all.data.BeaconsInfo;
import lukasz.marczak.pl.gotta_catch_em_all.data.PokeDetail;
import lukasz.marczak.pl.gotta_catch_em_all.data.PokeMove;
import lukasz.marczak.pl.gotta_catch_em_all.data.realm.DBManager;
import lukasz.marczak.pl.gotta_catch_em_all.data.realm.RealmPokeDetail;
import lukasz.marczak.pl.gotta_catch_em_all.fragments.Fightable;
import lukasz.marczak.pl.gotta_catch_em_all.fragments.Progressable;
import lukasz.marczak.pl.gotta_catch_em_all.game.Engine;
import lukasz.marczak.pl.gotta_catch_em_all.game.Player;


public class FightRunningFragment extends Fragment implements Fightable {

    final static String TAG = FightRunningFragment.class.getSimpleName();
    ImageView yourPokemon, opponentPokemon;
    WhorlView action;
    FightActivity parentActivity;
    String opponentName = "";
    boolean strongPokemon = Randy.randomAnswer();
    ProgressBar opponentHP;
    ProgressBar yourPokeHP;
    TextView yourPokeNameTV;
    TextView question, message;

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
        fetchPokemonDetails(opponentName);
    }

    private void fetchNewSelectedPokemonData(Fightable context, String pokemonName) {
        Log.d(TAG, "fetchNewSelectedPokemonData ");
        int yourPokeID = PokeUtils.getPokemonIdFromName(context.getActivity(), pokemonName);
        Config.IDLE_STATE = true;
        //check if pokemon has already downloaded moves
        Realm realm = Realm.getInstance(context.getActivity());
        realm.beginTransaction();
        RealmPokeDetail detail = realm.where(RealmPokeDetail.class).equalTo("name", pokemonName, false).findFirst();
        realm.commitTransaction();
        if (detail == null) {
            new PokeDetailDownloader() {
                @Override
                public void onDataReceived(PokeDetail detail) {
                    Log.i(TAG, "onDataReceived " + detail.getName());
                    Engine.INSTANCE.setYourPokeDetail(detail);
                    Config.IDLE_STATE = false;
                }
            }.start(this, yourPokeID);
        } else {
            Log.d(TAG, "already downloaded data, return from database");
            Engine.INSTANCE.setYourPokeDetail(DBManager.asPokeDetail(detail));
            Config.IDLE_STATE = false;
        }
        Engine.INSTANCE.allowMoveOpponent(context);
    }

    private void fetchPokemonDetails(String opponentName) {
        Log.d(TAG, "fetchPokemonDetails ");
        int pokemonID = PokeUtils.getPokemonIdFromName(this.getActivity(), opponentName);
        int yourPokeID = PokeUtils.getPokemonIdFromName(this.getActivity(), MainActivity.pokeName);
        RealmPokeDetail poke = Realm.getInstance(getActivity()).where(RealmPokeDetail.class)
                .equalTo("pkdxId", pokemonID).findFirst();
        if (poke == null) {
            Log.e(TAG, "poke detail " + pokemonID + " is null, fetching in progress...");
            new PokeDetailDownloader() {
                @Override
                public void onDataReceived(PokeDetail detail) {
                    Log.i(TAG, "onDataReceived " + detail.getName());
                    Engine.INSTANCE.setOpponentDetail(detail);
                }
            }.start(this, pokemonID);

            new PokeDetailDownloader() {
                @Override
                public void onDataReceived(PokeDetail detail) {
                    Log.i(TAG, "onDataReceived " + detail.getName());
                    Engine.INSTANCE.setYourPokeDetail(detail);
                }
            }.start(this, yourPokeID);

        } else {
            Engine.INSTANCE.setOpponentDetail(DBManager.asPokeDetail(poke));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_fight_running, container, false);
        action = (WhorlView) view.findViewById(R.id.action);
        yourPokemon = (ImageView) view.findViewById(R.id.your_pokemon_back);
        opponentPokemon = (ImageView) view.findViewById(R.id.opponent_front);
        TextView fight = (TextView) view.findViewById(R.id.fight);
        TextView bag = (TextView) view.findViewById(R.id.bag);
        TextView select = (TextView) view.findViewById(R.id.select_pokemon);
        message = (TextView) view.findViewById(R.id.announce);
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
        opponentHP.setProgress(100);

        opponentLevel.setText("Level " + Randy.from(10));
        yourPokeLevel.setText("Level 4");

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
                        switch (mode) {
                            case 0: {
                                if (!Config.IDLE_STATE) {
                                    view.setBackgroundColor(Color.LTGRAY);

                                    Log.d(TAG, "onTouch select attack");
                                    new SelectMenuEngine.FIGHT(FightRunningFragment.this) {

                                        @Override
                                        public void onAttackChosen(int position) {
                                            final PokeMove move = PokeAttacksAdapter.dataset.get(position);
                                            Log.d(TAG, "Player " + Engine.INSTANCE.getYourPoke().getName() + " used "
                                                    + move.getName() + "!!!");
                                            getActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    decreaseOpponent(move.getPower() / 3);
                                                }
                                            });
                                            Engine.INSTANCE.allowMoveOpponent(FightRunningFragment.this);
                                        }

                                        @Override
                                        public PokeDetail getPokeDetail() {
                                            Log.d(TAG, "getPokeDetail ");
                                            return Engine.INSTANCE.getYourPoke().getDetail();
                                        }

                                        @Override
                                        public int getCurrentPokemonLevel() {
                                            Log.d(TAG, "getCurrentPokemonLevel ");
                                            return Engine.INSTANCE.getYourPoke().getCurrentLevel();
                                        }
                                    };
                                    break;
                                }
                            }
                            case 1: {
                                Log.d(TAG, "BAG not supported yet");
                                view.setBackgroundColor(Color.LTGRAY);

                                break;
                            }
                            case 2: {
                                if (!Config.IDLE_STATE) {
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
                                                    fetchNewSelectedPokemonData(FightRunningFragment.this, newPokeName);
                                                }
                                            });
                                        }
                                    };
                                    view.setBackgroundColor(Color.LTGRAY);
                                    break;
                                }
                            }
                            case 3: {
                                if (!Config.IDLE_STATE)
                                    if (!strongPokemon) parentActivity.onBackPressed();
                                    else
                                        Snackbar.make(getView(), "Cannot run from this fight!", Snackbar.LENGTH_LONG).show();
                                view.setBackgroundColor(Color.LTGRAY);
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

    public void updateHpBar(boolean isOpponent, int value) {
        if (isOpponent) opponentHP.setProgress(value);
        else yourPokeHP.setProgress(value);
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

    @Override
    public void showProgressBar(boolean show) {
        Log.d(TAG, "showProgressBar " + show);
        int vis = show ? View.VISIBLE : View.INVISIBLE;
        if (action != null) {
            action.setVisibility(vis);
            if (!action.isCircling() && action.isShown())
                action.start();
            else if (!action.isShown())
                action.stop();
        }
    }

    @Override
    public void setText(CharSequence s) {
        Log.d(TAG, "setText ");
        message.setText(s);
    }

    @Override
    public void decreasePoke(int i) {
        Log.d(TAG, "decreasePoke " + i);
        int total = Engine.INSTANCE.getYourPoke().getHp() - i;
        Engine.INSTANCE.getYourPoke().setHp(total);
        if (total < 0) {
            switchToDeathPokeState();
            total = 0;
        }
        yourPokeHP.setProgress(total);

    }

    boolean nothingWasClicked = true;

    private void switchToDeathPokeState() {
        Log.d(TAG, "switchToDeathPokeState ");
        Config.deadPokemons.add(Engine.INSTANCE.getYourPoke().getName());
        Snackbar.make(getView(), "Oops! Your " + Engine.INSTANCE.getYourPoke().getName() + " fainted. Continue? ", Snackbar.LENGTH_LONG).setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick ");
                nothingWasClicked = false;
                new SelectMenuEngine.POKEMON(getActivity()) {
                    @Override
                    public void onPokemonChosen(int position) {
                        Log.d(TAG, "onPokemonChosen " + position);
                        String newPokeName = MyPokesAdapter.dataset.get(position).getName();
                        if (contains(Config.deadPokemons, newPokeName)) {
                            Log.i(TAG, "cannot choose this pokemon!!!");
                            Snackbar.make(getView(), "This pokemon is fainted!!!", Snackbar.LENGTH_SHORT).show();
                            return;
                        }
                        String newPokeResource = PokeSpritesManager
                                .getPokemonBackByName(newPokeName);
                        Picasso.with(getActivity()).load(newPokeResource).into(yourPokemon);
                        String prettyName = PokeUtils.getPrettyPokemonName(newPokeName);
                        yourPokeNameTV.setText(prettyName);
                        question.setText("What will \n" + prettyName + " do?");
                        fetchNewSelectedPokemonData(FightRunningFragment.this, newPokeName);
                    }
                };
            }
        }).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (nothingWasClicked)
                    getActivity().onBackPressed();
            }
        }, 5000);


    }

    private boolean contains(List<String> deadPokemons, String newPokeName) {
        for (String s : deadPokemons) {
            if (s.equals(newPokeName))
                return true;
        }
        return false;
    }

    @Override
    public void decreaseOpponent(int i) {
        Log.d(TAG, "decreaseOpponent " + i);
        int total = Engine.INSTANCE.getOpponentPoke().getHp() - i;
        Engine.INSTANCE.getOpponentPoke().setHp(total);
        if (total < 0) {
            switchToWinnerState();
            total = 0;
        }
        opponentHP.setProgress(total);
    }

    private void switchToWinnerState() {
        Log.d(TAG, "switchToWinnerState ");
        Snackbar.make(getView(), "Congratulations! You have beaten " + opponentName, Snackbar.LENGTH_LONG).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getActivity().onBackPressed();
            }
        }, 4000);
    }
}
