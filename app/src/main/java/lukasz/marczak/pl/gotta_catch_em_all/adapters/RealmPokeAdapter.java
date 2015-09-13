package lukasz.marczak.pl.gotta_catch_em_all.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tt.whorlviewlibrary.WhorlView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.realm.Realm;
import lukasz.marczak.pl.gotta_catch_em_all.R;
import lukasz.marczak.pl.gotta_catch_em_all.activities.PokeInfoActivity;
import lukasz.marczak.pl.gotta_catch_em_all.configuration.PokeConstants;
import lukasz.marczak.pl.gotta_catch_em_all.configuration.PokeUtils;
import lukasz.marczak.pl.gotta_catch_em_all.data.NetPoke;
import lukasz.marczak.pl.gotta_catch_em_all.data.RealmPoke;
import lukasz.marczak.pl.gotta_catch_em_all.fragments.main.PokedexFragment;
import lukasz.marczak.pl.gotta_catch_em_all.fragments.main.RealmPokeFragment;

/**
 * Created by Lukasz Marczak on 2015-08-23.
 */
public class RealmPokeAdapter extends RecyclerView.Adapter<RealmPokeAdapter.ViewHolder> {
    public static final String TAG = RealmPokeAdapter.class.getSimpleName();
    private static List<NetPoke> dataset = new ArrayList<>();// = Collections.synchronizedList(new ArrayList<NetPoke>());
    private Context context = null;
    private RealmPokeFragment parent;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View v;
        public TextView name, id;
        public RelativeLayout dataParent;

        public ViewHolder(View v) {
            super(v);
            this.v = v;

            dataParent = (RelativeLayout) v.findViewById(R.id.parent);
            name = (TextView) v.findViewById(R.id.pokemon_name);
            id = (TextView) v.findViewById(R.id.pokemon_id);
        }
    }

    public RealmPokeAdapter(RealmPokeFragment parent) {
        this.context = parent.getActivity();
        this.parent = parent;
        List<RealmPoke> pokesUnSorted = Realm.getInstance(parent.getActivity()).where(RealmPoke.class)
                .findAll();
        for (RealmPoke poke : pokesUnSorted) {
//            if (!contains(dataset, poke))
                dataset.add(new NetPoke(Integer.valueOf(poke.getId()), poke.getName()));
        }
        Collections.sort(dataset, new Comparator<NetPoke>() {
            @Override
            public int compare(NetPoke lhs, NetPoke rhs) {
                return lhs.getID() < rhs.getID() ? -1 : 1;
            }
        });
        dataset = dataset.subList(1,dataset.size());

    }

    private boolean contains(List<NetPoke> dataset, RealmPoke poke) {

        for (NetPoke netPoke : dataset) {
            if (netPoke.getName() .equals(poke.getName()))
                return true;
        }
        return false;
    }

    @Override
    public RealmPokeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        // create a new view
        View v = android.view.LayoutInflater.from(parent.getContext())
                .inflate(R.layout.net_poke_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(final ViewHolder vh, final int position) {
        Log.d(TAG, "onBindViewHolder");
        if (dataset == null || dataset.size() <= position
                || dataset.get(position) == null) return;

        NetPoke poke = dataset.get(position);
        vh.id.setText(String.valueOf(poke.getID()));
        vh.name.setText(poke.getName());
        vh.dataParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick " + position);
                if (position < 0)
                    return;
                NetPoke poke = dataset.get(position);
                Log.i(TAG, "clicked item " + position);
                Intent intent = new Intent(context, PokeInfoActivity.class);
                intent.putExtra(PokeConstants.ID, poke.getID());
                intent.putExtra(PokeConstants.NAME, poke.getName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (dataset == null) {
            Log.e(TAG, "getItemCount: data set is null!");
            return 0;
        }
        return dataset.size();
    }
}