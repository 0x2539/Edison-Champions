package example.com.finder.Activities;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import example.com.finder.R;


public class WorkerActivity extends ActionBarActivity {

    private BluetoothAdapter mBluetoothAdapter;
    private final int REQUEST_ENABLE_BT = 1; // must be greater than zero
    private BroadcastReceiver mReceiver;
    private List<BluetoothDevice> devicesAround;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker);

        BluetoothInitialization();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    void BluetoothInitialization() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // Check whether the device has bluetooth capabilities
        if (mBluetoothAdapter == null)
            Log.e("Worker", "Bluetooth not supported!");

        else {
            // Check whether it is enabled
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            } else
                DiscoverNearbyDevices();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == REQUEST_ENABLE_BT) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK)
                DiscoverNearbyDevices();
            else
                BluetoothOff();
        }
    }

    void BluetoothOff() {
        //TODO: tell user to enable bl
        Log.e("Worker", "User won't turn bluetooth on!");
    }

    void DiscoverNearbyDevices() {
        devicesAround = new ArrayList<>();

        // Create a BroadcastReceiver for ACTION_FOUND
        mReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                // When discovery finds a device
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    // Get the BluetoothDevice object from the Intent
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    short rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);
                    devicesAround.add(device);

                    Log.d("Worker", "Found device " + device.getName() + ": " + device.getAddress() + ", rssi = " + rssi);

                }
            }
        };

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);

        Thread thread = new Thread(new DiscoveryRunnable());
        thread.start();

    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mReceiver);
    }


    class DiscoveryRunnable implements Runnable
    {
        final static long INTERVAL = 60 * 1000; // in ms
        long lastCall;

        public DiscoveryRunnable() {
            lastCall = 0;
        }

        @Override
        public void run() {
            while (true) {
                if (System.currentTimeMillis() - lastCall >= INTERVAL) {
                    lastCall = System.currentTimeMillis();
                    Discover();
                }
                try {
                    Thread.sleep(INTERVAL);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private void Discover() {

            devicesAround.clear();

            Log.d("Worker", "New discovery!");
            if (!mBluetoothAdapter.startDiscovery())
                Log.e("Worker", "Couldn't start discovery!");
        }

    }
}
