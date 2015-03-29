package example.com.finder.Activities;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Layout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.fasterxml.jackson.core.type.TypeReference;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import example.com.finder.Layouts.PeopleFragLayout;
import example.com.finder.POJO.Person;
import example.com.finder.R;
import example.com.finder.Utils.JSONUtils;
import example.com.finder.Utils.NetUtils;
import example.com.finder.Utils.PeopleUtils;
import example.com.finder.Utils.SharedPreferencesUtils;
import example.com.finder.Utils.ViewUtils;


public class PeopleActivity extends ActionBarActivity implements PeopleFragLayout.OnPeopleListFragmentListener {

    private PeopleFragLayout layout;
    private BluetoothAdapter mBluetoothAdapter;
    private final int REQUEST_ENABLE_BT = 1; // must be greater than zero
    private BroadcastReceiver mReceiver;
    private List<BluetoothDevice> devicesAround;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people);

        layout = (PeopleFragLayout) getSupportFragmentManager().findFragmentById(R.id.people_fragment);
        BluetoothInitialization();
        Log.i("BLE", "passed");

        ViewUtils.launchRingDialog(this, new Runnable() {
            @Override
            public void run() {

                final String GET_URL = "http://192.168.1.153:8000/users_nearby/?mac=";
                String mac = SharedPreferencesUtils.getFacebookMacAddress(getApplicationContext());//"98:fe:94:4c:a3:ec"; // TODO: get this
                String json = "";
                while(json.equals(""))
                {
                    json = getJSON(GET_URL, mac);
                }

                PeopleUtils.setPeople(JSONUtils.fromJSON(json, new TypeReference<List<Person>>() {
                }));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        layout.updateView();
                    }
                });
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                String json = "";
                while (true)
                {
                    final String GET_URL = "http://192.168.1.153:8000/users_nearby/?mac=" + SharedPreferencesUtils.getFacebookMacAddress(getApplicationContext());
                    json = "";
                    while (json.equals(""))
                    {
                        json = NetUtils.GetData(GET_URL);//"http://192.168.1.153:8000/yo?facebook_id="+SharedPreferencesUtils.getFacebookUserId(getApplicationContext()));
                    }

                    PeopleUtils.setPeopleYoedBy(JSONUtils.fromJSON(json, new TypeReference<List<Person>>() {
                    }));
                    PeopleUtils.updateYoedPeople(PeopleUtils.getPeopleYoedBy());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            layout.updateView();
                        }
                    });
                    List<String> keys = new ArrayList<String>();
                    List<String> values = new ArrayList<String>();

                    String macs = "";

                    keys.add("mac");
                    values.add(SharedPreferencesUtils.getFacebookMacAddress(getApplicationContext()));

                    keys.add("nearby");
                    for(BluetoothDevice ble : devicesAround)
                    {
//                        values.add(ble.getAddress());
                        macs += ble.getAddress() + ",";
                    }
                    if(macs.length() > 0)
                    {
                        macs = macs.substring(0, macs.length() - 1);
                    }
                    values.add(macs);
                    NetUtils.PostData(keys, values, "http://192.168.1.153:8000/user/");

                    try {
                        Thread.sleep(10 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
//        layout = new PeopleFragLayout();
//        setContentView(layout);
    }

    private String getJSON(String GET_URL, String mac) {
        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(GET_URL + mac);

        try {
            HttpResponse response = client.execute(get);

            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            StringBuilder builder = new StringBuilder();
            for (String line; (line = reader.readLine()) != null; )
                builder.append(line).append("\n");

            return builder.toString();
//            JSONTokener tokener = new JSONTokener(builder.toString());
//            try {
//                JSONArray finalResult = new JSONArray(tokener);
//                for (int i = 0; i < finalResult.length(); i++)
//                    Log.d("Server", finalResult.get(i).toString());

        } catch (Exception e) {
//                Log.e("Server", "JSON parsing error");
            e.printStackTrace();
        }
        return "";
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

    @Override
    public void onPersonClicked(int position) {
        PeopleUtils.setCurrentPersonIndex(position);
        Intent myIntent = new Intent(PeopleActivity.this, PersonDetailActivity.class);
        PeopleActivity.this.startActivity(myIntent);
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
        Log.e("Worker", "User won't turn bluetooth on!");//what a son of a bitch
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
        super.onDestroy();
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
