package lukasz.marczak.pl.gotta_catch_em_all.fragments;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tt.whorlviewlibrary.WhorlView;


import lukasz.marczak.pl.gotta_catch_em_all.R;
import lukasz.marczak.pl.gotta_catch_em_all.activities.FightActivity;
import lukasz.marczak.pl.gotta_catch_em_all.adapters.BeaconAdapter;
import lukasz.marczak.pl.gotta_catch_em_all.bluetooth.BLEScanner;
import lukasz.marczak.pl.gotta_catch_em_all.bluetooth.BluetoothUtils;
import lukasz.marczak.pl.gotta_catch_em_all.configuration.Config;
import lukasz.marczak.pl.gotta_catch_em_all.data.BeaconsInfo;
import lukasz.marczak.pl.gotta_catch_em_all.data.DetectedBeacons;
import uk.co.alt236.bluetoothlelib.device.BluetoothLeDevice;
import uk.co.alt236.bluetoothlelib.device.beacon.ibeacon.IBeaconDevice;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * <p/>
 * to handle interaction events.
 * Use the {@link RangeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RangeFragment extends Fragment {
    private static final String TAG = RangeFragment.class.getSimpleName();
    private WhorlView whorlView;
    private static BLEScanner beaconScanner;
    private static BluetoothUtils beaconUtils;
    private static RecyclerView recyclerView;

    private DetectedBeacons detectedBeacons = new DetectedBeacons(getActivity());
    private BeaconAdapter beaconAdapter;
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
            Log.d(TAG, "onLeScan()");
            final BluetoothLeDevice deviceLe = new BluetoothLeDevice(device, rssi, scanRecord,
                    System.currentTimeMillis());

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "Detected beacon!");
                    Log.d(TAG, "MAC address: " + deviceLe.getAddress());
                    Log.d(TAG, "name: " + deviceLe.getName());
                    Log.d(TAG, "bond state: " + deviceLe.getBluetoothDeviceBondState());
                    Log.d(TAG, "bt device class name: " + deviceLe.getBluetoothDeviceClassName());

                    if (BeaconsInfo.isBeacon(deviceLe)) {
                        final IBeaconDevice iBeacon = new IBeaconDevice(deviceLe);
                        Log.d(TAG, "UUID: " + iBeacon.getUUID());
                        Log.d(TAG, "accuracy: " + iBeacon.getAccuracy());
                        Log.d(TAG, "calibrated power: " + iBeacon.getCalibratedTxPower());
                        Log.d(TAG, "company identifier: " + iBeacon.getCompanyIdentifier());
                        Log.d(TAG, "distance descriptor: " + iBeacon.getDistanceDescriptor());
                        Log.d(TAG, "IBeaconData: " + iBeacon.getIBeaconData().getIBeaconAdvertisement());
                        Log.d(TAG, "Minor: " + iBeacon.getMinor());
                        Log.d(TAG, "Major: " + iBeacon.getMajor());

                        if (iBeacon.getDistanceDescriptor().toString().equals("IMMEDIATE")) {
                            whorlView.stop();
                            whorlView.setVisibility(View.GONE);

                            BeaconsInfo.FORCE_STOP_SCAN = true;
                            Intent fightIntent = new Intent(getActivity(), FightActivity.class);
                            fightIntent.putExtra(BeaconsInfo.Bundler.MAC, deviceLe.getAddress());
                            fightIntent.putExtra(BeaconsInfo.Bundler.MINOR, iBeacon.getMinor());
                            fightIntent.putExtra(BeaconsInfo.Bundler.MAJOR, iBeacon.getMajor());
                            fightIntent.putExtra(BeaconsInfo.Bundler.CALIBRATED_POWER, iBeacon.getCalibratedTxPower());
                            fightIntent.putExtra(BeaconsInfo.Bundler.ACCURACY, iBeacon.getAccuracy());
                            startActivityForResult(fightIntent, Config.IntentCode.START_WILD_FIGHT);
                        }
                    }


//                    detectedBeacons.addBeacon(deviceLe,beaconAdapter);
//                    if (beaconAdapter != null)
//                        beaconAdapter.notifyData(detectedBeacons);
                }
            });
        }
    };


//    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RangeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RangeFragment newInstance() {
        RangeFragment fragment = new RangeFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    public RangeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate()");
        Loop.start(this);
    }


    public void restartScan() {
        if (BeaconsInfo.FORCE_STOP_SCAN)
            return;
        Log.d(TAG, "resetScan()");

        if (beaconScanner != null && beaconScanner.isScanning()) {
            beaconScanner.forceStopScan();
        }

        beaconUtils = new BluetoothUtils(getActivity());
        beaconScanner = new BLEScanner(mLeScanCallback, beaconUtils);

        beaconUtils.askUserToEnableBluetoothIfNeeded();

        if (beaconUtils.isBluetoothOn() && beaconUtils.isBluetoothLeSupported()) {
            beaconScanner.scanLeDevice(-1, true);
            beaconScanner.startWithWhorl(true);
        }
    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart()");
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView()");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.template_recyclerview, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        whorlView = (WhorlView) view.findViewById(R.id.whorl2);
        if (beaconScanner != null)
            beaconScanner.setWhorlView(whorlView);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);

        beaconAdapter = new BeaconAdapter(getActivity(), detectedBeacons);
        recyclerView.setAdapter(beaconAdapter);
        recyclerView.setVisibility(View.GONE);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated");
        super.onViewCreated(view, savedInstanceState);


    }
    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

    @Override
    public void onAttach(Activity activity) {
        Log.d(TAG, "onAttach()");
        super.onAttach(activity);
//        try {
//            mListener = (OnFragmentInteractionListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        Log.d(TAG, "onDetach()");
        super.onDetach();
//        mListener = null;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy()");
        super.onDestroy();

    }
}

class Loop {
    private static int times = 30;
    private static String TAG = Loop.class.getSimpleName();
    private static boolean isStarted = false;
    private static Handler handler = new Handler();
    private static RangeFragment parent;
    public static boolean isActive = true;

    private static Runnable stepTimer = new Runnable() {
        @Override
        public void run() {
            if (isActive) {
                parent.restartScan();
                times--;
                handler.postDelayed(this, 2000); //LoopAction() is invoked every 2 seconds
                if (times < 0 || BeaconsInfo.FORCE_STOP_SCAN)
                    isActive = false;
            } else {
                resetLoop();
            }
        }

    };

    private static void resetLoop() {
        Log.d(TAG, "resetLoop()");
        handler.removeCallbacks(stepTimer);
        isStarted = false;
        times = 30;
    }

    public static void start(RangeFragment rangeFragment) {
        if (!isStarted) {
            handler.postDelayed(stepTimer, 100); //start looping after 100 ms of delay
            isStarted = true;
        } else return;

        parent = rangeFragment;
    }
}