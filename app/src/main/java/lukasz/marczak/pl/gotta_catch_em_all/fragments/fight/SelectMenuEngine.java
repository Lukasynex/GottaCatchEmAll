package lukasz.marczak.pl.gotta_catch_em_all.fragments.fight;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import lukasz.marczak.pl.gotta_catch_em_all.R;
import lukasz.marczak.pl.gotta_catch_em_all.adapters.MyPokesAdapter;

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

    public void BAG() {
        //open bag to select pokeballs or potions or other stuff
    }

    public void FIGHT() {
        //select attack
    }

}
