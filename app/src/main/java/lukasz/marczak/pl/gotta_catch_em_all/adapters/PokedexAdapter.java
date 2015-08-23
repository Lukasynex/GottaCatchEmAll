package lukasz.marczak.pl.gotta_catch_em_all.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import lukasz.marczak.pl.gotta_catch_em_all.R;
import lukasz.marczak.pl.gotta_catch_em_all.data.DetectedBeacons;
import lukasz.marczak.pl.gotta_catch_em_all.data.NetPoke;
import lukasz.marczak.pl.gotta_catch_em_all.fragments.PokedexFragment;
import uk.co.alt236.bluetoothlelib.device.BluetoothLeDevice;
import uk.co.alt236.bluetoothlelib.device.beacon.ibeacon.IBeaconDevice;

/**
 * Created by Lukasz Marczak on 2015-08-23.
 */
public class PokedexAdapter extends RecyclerView.Adapter<PokedexAdapter.ViewHolder> {
    public static final String TAG = PokedexAdapter.class.getSimpleName();
    private List<NetPoke> dataset;
    private Context context = null;


    public void notifyData(DetectedBeacons beacons) {
//        dataset.clear();
////        dataset = beacons;
//        dataset.addAll(beacons);
        Log.d(TAG, "notifyDataSetChanged");
        notifyDataSetChanged();
        Log.d(TAG, "notifyItemRangeChanged");
        notifyItemRangeChanged(0, dataset.size());
        //    instance = this;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View v;
        public TextView name, mac, rssi,near;
        public RelativeLayout dataParent;
        public ImageView image;

        public ViewHolder(View v) {
            super(v);
            this.v = v;

            dataParent = (RelativeLayout) v.findViewById(R.id.parent);
            name = (TextView) v.findViewById(R.id.name);
            rssi = (TextView) v.findViewById(R.id.rssi);
            near = (TextView) v.findViewById(R.id.near);
            mac = (TextView) v.findViewById(R.id.mac_address);
            image = (ImageView) v.findViewById(R.id.image);
        }
    }

    public PokedexAdapter(PokedexFragment context, List<NetPoke> pokes) {
        this.context = context.getActivity();
        this.dataset = pokes;
    }

    @Override
    public PokedexAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        // create a new view
        View v = android.view.LayoutInflater.from(parent.getContext())
                .inflate(R.layout.beacon_item, parent, false);
        ViewHolder vh = new ViewHolder(v);


        setupClickListeners(vh);
        return vh;
    }

    private void setupClickListeners(final ViewHolder holder) {

    }

    private static boolean bindingRunning = false;

    @Override
    public void onBindViewHolder(final ViewHolder vh, final int position) {
        Log.d(TAG, "onBindViewHolder");
        if (bindingRunning || dataset == null || dataset.size() <= position
                || dataset.get(position) == null)
            return;
        bindingRunning = true;
        //todo BIND HERE
        bindingRunning = false;
    }

    @Override
    public int getItemCount() {
        if (dataset == null) {
            Log.e(TAG, "getItemCount: mDataset is null!");
            return 0;
        }
        return dataset.size();
    }

}