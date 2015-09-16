package lukasz.marczak.pl.gotta_catch_em_all.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import lukasz.marczak.pl.gotta_catch_em_all.R;
import lukasz.marczak.pl.gotta_catch_em_all.connection.PokeSpritesManager;
import lukasz.marczak.pl.gotta_catch_em_all.data.TrainedPoke;

/**
 * Created by Lukasz Marczak on 2015-08-23.
 */
public abstract class MyPokesAdapter extends RecyclerView.Adapter<MyPokesAdapter.ViewHolder> {
    public static final String TAG = MyPokesAdapter.class.getSimpleName();
    public static List<TrainedPoke> dataset;
    private Context context = null;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View v;
        public TextView name;
        public RelativeLayout dataParent;
        public ImageView image;

        public ViewHolder(View v) {
            super(v);
            this.v = v;

            dataParent = (RelativeLayout) v.findViewById(R.id.parent);
            name = (TextView) v.findViewById(R.id.pokeName);
            image = (ImageView) v.findViewById(R.id.pokeImage);
        }
    }

    public MyPokesAdapter(Context context) {
        this.context = context;
        dataset = new ArrayList<>();
        dataset.add(new TrainedPoke("caterpie"));
        dataset.add(new TrainedPoke("mew"));
        dataset.add(new TrainedPoke("voltorb"));
        dataset.add(new TrainedPoke("eevee"));
        dataset.add(new TrainedPoke("onix"));
        dataset.add(new TrainedPoke("jolteon"));

    }

    @Override
    public MyPokesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        // create a new view
        View v = android.view.LayoutInflater.from(parent.getContext())
                .inflate(R.layout.your_poke_item, parent, false);
        ViewHolder vh = new ViewHolder(v);


        setupClickListeners(vh);
        return vh;
    }

    private void setupClickListeners(final ViewHolder holder) {

    }


    @Override
    public void onBindViewHolder(final ViewHolder vh, final int position) {
        Log.d(TAG, "onBindViewHolder");
        if ( dataset == null || dataset.size() <= position || dataset.get(position) == null)
            return;
        TrainedPoke poke = dataset.get(position);
        String pokeName = poke.getName();
        vh.name.setText(pokeName);
        Picasso.with(context).load(PokeSpritesManager.getPokemonFrontByName(pokeName)).into(vh.image);
        vh.dataParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (dataset == null) {
            Log.e(TAG, "getItemCount: mDataset is null!");
            return 0;
        }
        return dataset.size();
    }
    public abstract void onItemClick(int postion);
}