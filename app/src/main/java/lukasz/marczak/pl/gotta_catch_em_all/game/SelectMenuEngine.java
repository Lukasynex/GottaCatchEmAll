package lukasz.marczak.pl.gotta_catch_em_all.game;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import lukasz.marczak.pl.gotta_catch_em_all.R;
import lukasz.marczak.pl.gotta_catch_em_all.adapters.MyPokesAdapter;
import lukasz.marczak.pl.gotta_catch_em_all.adapters.PokeAttacksAdapter;
import lukasz.marczak.pl.gotta_catch_em_all.configuration.RecyclerUtils;
import lukasz.marczak.pl.gotta_catch_em_all.data.PokeDetail;
import lukasz.marczak.pl.gotta_catch_em_all.fragments.Progressable;

/**
 * Created by Lukasz Marczak on 2015-09-16.
 */
public class SelectMenuEngine {
    public static final SelectMenuEngine INSTANCE = new SelectMenuEngine();

    private SelectMenuEngine() {
    }

    public void RUN() {
        //go away, if opponent is too strong
    }

    public static abstract class POKEMON {

        public abstract void onPokemonChosen(final int position);

        public POKEMON(Context context) {
//        AlertDialog.Builder selectPokeWindow= new AlertDialog.Builder(context);
            final Dialog selectPokeWindow = new Dialog(context);

            //disables title
            selectPokeWindow.requestWindowFeature(Window.FEATURE_NO_TITLE);
            //this makes cardview look
            selectPokeWindow.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            selectPokeWindow.setContentView(R.layout.select_poke_window);
            TextView cancelButton = (TextView) selectPokeWindow.findViewById(R.id.cancel1);
            RecyclerView yourPokesAdapter = (RecyclerView) selectPokeWindow.findViewById(R.id.recycler_view1);
            yourPokesAdapter.setLayoutManager(new LinearLayoutManager(context));
            yourPokesAdapter.setAdapter(new MyPokesAdapter(context) {
                @Override
                public void onItemClick(int position) {
                    Log.d(TAG, "onItemClick ");
                    onPokemonChosen(position);
                    selectPokeWindow.dismiss();
                }
            });

            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectPokeWindow.dismiss();
                }
            });
            selectPokeWindow.show();
        }

    }

    public static abstract class FIGHT {

        public abstract void onAttackChosen(final int position);

        public abstract PokeDetail getPokeDetail();

        public abstract int getCurrentPokemonLevel();

        public FIGHT(final Progressable context) {

            final Dialog selectAttackWindow = new Dialog(context.getActivity());
            //disables title
            selectAttackWindow.requestWindowFeature(Window.FEATURE_NO_TITLE);
            //this makes cardview look
            selectAttackWindow.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            selectAttackWindow.setContentView(R.layout.select_poke_window);

            TextView cancelButton = (TextView) selectAttackWindow.findViewById(R.id.cancel1);
            TextView title1 = (TextView) selectAttackWindow.findViewById(R.id.title1);
            final RecyclerView attacksRecycler = (RecyclerView) selectAttackWindow.findViewById(R.id.recycler_view1);
            final ProgressBar bar = (ProgressBar) selectAttackWindow.findViewById(R.id.progressBarLayout1);
            attacksRecycler.setLayoutManager(new LinearLayoutManager(context.getActivity()));
            title1.setText("Select attack");
            attacksRecycler.setAdapter(new PokeAttacksAdapter(context, getPokeDetail(), getCurrentPokemonLevel()) {
                @Override
                public void showProgressBar(boolean show) {
                    Log.d(TAG, "showProgressBar ");
                    int visbl = show ? View.VISIBLE : View.INVISIBLE;
                    bar.setVisibility(visbl);
                    attacksRecycler.setOnTouchListener(RecyclerUtils.disableTouchEvents(show));
                }

                @Override
                public Activity getActivity() {
                    return context.getActivity();
                }

                @Override
                public void setText(CharSequence s) {

                }


                @Override
                public void onItemClick(int postion) {
                    Log.d(TAG, "onItemClick " + postion);
                    onAttackChosen(postion);
                    selectAttackWindow.dismiss();
                }
            });

            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectAttackWindow.dismiss();
                }
            });
            selectAttackWindow.show();
        }
    }

    public void BAG() {
        //open bag to select pokeBalls or potions or other stuff you have
    }

}
