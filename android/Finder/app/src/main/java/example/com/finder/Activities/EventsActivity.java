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

import com.fasterxml.jackson.core.type.TypeReference;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import example.com.finder.Layouts.EventsFragLayout;
import example.com.finder.Layouts.PeopleFragLayout;
import example.com.finder.POJO.Person;
import example.com.finder.R;
import example.com.finder.Utils.JSONUtils;
import example.com.finder.Utils.NetUtils;
import example.com.finder.Utils.PeopleUtils;
import example.com.finder.Utils.SharedPreferencesUtils;
import example.com.finder.Utils.ViewUtils;


public class EventsActivity extends ActionBarActivity implements EventsFragLayout.OnPeopleListFragmentListener {

    private EventsFragLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        layout = (EventsFragLayout) getSupportFragmentManager().findFragmentById(R.id.events_fragment);
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
        Intent myIntent = new Intent(EventsActivity.this, PersonDetailActivity.class);
        EventsActivity.this.startActivity(myIntent);
    }

}
