package lukasz.marczak.pl.gotta_catch_em_all.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.tt.whorlviewlibrary.WhorlView;

/**
 * Created by Lukasz Marczak on 2015-08-23.
 */
public class BLEScanner {
    private final Handler mHandler;
    private final BluetoothAdapter.LeScanCallback mLeScanCallback;
    private final BluetoothUtils mBluetoothUtils;
    private boolean mScanning;
    private boolean scanWithWhorl;
    private WhorlView whorlView;


    public BLEScanner(final BluetoothAdapter.LeScanCallback leScanCallback, final BluetoothUtils bluetoothUtils) {
        mHandler = new Handler();
        mLeScanCallback = leScanCallback;
        mBluetoothUtils = bluetoothUtils;
    }

    public boolean isScanning() {
        return mScanning;
    }

    public void forceStopScan() {
        mScanning = false;
        mBluetoothUtils.getBluetoothAdapter().stopLeScan(mLeScanCallback);
    }

    /**
     *
     *
     * */
    public void scanLeDevice(final int duration, final boolean enable) {
        if (enable) {
            if (mScanning) {
                return;
            }
            Log.d("TAG", "~ Starting Scan");
            // Stops scanning after a pre-defined scan period.
            if (duration > 0) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("TAG", "~ Stopping Scan (timeout)");
//                        hideWhorlView();
                        mScanning = false;
                        mBluetoothUtils.getBluetoothAdapter().stopLeScan(mLeScanCallback);
                    }
                }, duration);
            }
            mScanning = true;
            mBluetoothUtils.getBluetoothAdapter().startLeScan(mLeScanCallback);
        } else {
            Log.d("TAG", "~ Stopping Scan");
            //hideWhorlView();
            mScanning = false;
            mBluetoothUtils.getBluetoothAdapter().stopLeScan(mLeScanCallback);
        }
    }

    private void hideWhorlView() {
        if (whorlView != null) {
            whorlView.stop();
            whorlView.setVisibility(View.GONE);
        }
    }

    public void startWithWhorl(boolean ifStart) {

    }

    public void setWhorlView(WhorlView view) {
        whorlView = view;
    }
}