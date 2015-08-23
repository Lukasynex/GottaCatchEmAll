package lukasz.marczak.pl.gotta_catch_em_all.adapters;

import android.content.Context;
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
import java.util.List;

import lukasz.marczak.pl.gotta_catch_em_all.R;
import lukasz.marczak.pl.gotta_catch_em_all.data.NetPoke;
import lukasz.marczak.pl.gotta_catch_em_all.fragments.PokedexFragment;

/**
 * Created by Lukasz Marczak on 2015-08-23.
 */
public abstract class PokedexAdapter extends RecyclerView.Adapter<PokedexAdapter.ViewHolder> {
    public static final String TAG = PokedexAdapter.class.getSimpleName();
    private List<NetPoke> dataset = Collections.synchronizedList(new ArrayList<NetPoke>());
    private Context context = null;
    private PokedexFragment parent;
    private WhorlView progressBar;
    private RecyclerView thisRecyclerView;
    private static boolean allowTouchEvents = true;

    public void notifyDataset() {

        notifyDataSetChanged();
        notifyItemRangeChanged(0, dataset.size());
    }

    public List<NetPoke> getDataset() {
        return dataset;
    }

    public void dismissProgressBar() {
        parent.dismissProgressBar();
    }

    public void showProgressBar() {
        parent.showProgressBar();
    }

    public void unLockTouchEvents() {
        allowTouchEvents = true;
    }


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

    public PokedexAdapter(PokedexFragment parent, List<NetPoke> pokes, RecyclerView recycler) {
        this.context = parent.getActivity();
        this.parent = parent;
        this.thisRecyclerView = recycler;
        dataset = pokes;
        thisRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (allowTouchEvents)
                    return false;
                else
                    return true;
            }
        });
    }

    @Override
    public PokedexAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {
        // create a new view
        View v = android.view.LayoutInflater.from(parent.getContext())
                .inflate(R.layout.net_poke_item, parent, false);
        ViewHolder vh = new ViewHolder(v);

        setupClickListeners(vh);
        return vh;
    }

    private void setupClickListeners(final ViewHolder holder) {

    }

    private static boolean bindingRunning = false;
    public static boolean downloadDone = false;

    @Override
    public void onBindViewHolder(final ViewHolder vh, final int position) {
        Log.d(TAG, "onBindViewHolder");
        if (bindingRunning || dataset == null || dataset.size() <= position
                || dataset.get(position) == null)
            return;
        if ( position != 149 && dataset.get(position).getName().equals("")) {
            Log.d(TAG, "new items!!!");
            allowTouchEvents = false;

            loadNewNetPokes(position, this);
            return;
        }

        vh.id.setText(String.valueOf(1 + position));
        String name = dataset.get(position).getName();
//        if (name.length() == 0)
//            downloadDone = false;

        vh.name.setText(name);


    }

    @Override
    public int getItemCount() {
        if (dataset == null) {
            Log.e(TAG, "getItemCount: mDataset is null!");
            return 0;
        }
        return dataset.size();
    }

    public abstract void loadNewNetPokes(int position, PokedexAdapter adapter);

}