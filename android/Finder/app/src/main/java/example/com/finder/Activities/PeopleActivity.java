package example.com.finder.Activities;

import android.content.Intent;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import example.com.finder.Layouts.PeopleFragLayout;
import example.com.finder.POJO.Person;
import example.com.finder.R;
import example.com.finder.Utils.JSONUtils;
import example.com.finder.Utils.PeopleUtils;
import example.com.finder.Utils.ViewUtils;


public class PeopleActivity extends ActionBarActivity implements PeopleFragLayout.OnPeopleListFragmentListener {

    private PeopleFragLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people);

        layout = (PeopleFragLayout) getSupportFragmentManager().findFragmentById(R.id.people_fragment);

        ViewUtils.launchRingDialog(this, new Runnable() {
            @Override
            public void run() {

                final String GET_URL = "http://192.168.1.153:8000/users_nearby/?mac=";
                String mac = "98:fe:94:4c:a3:ec"; // TODO: get this
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
        Intent myIntent = new Intent(PeopleActivity.this, PersonDetailActivity.class);
        PeopleActivity.this.startActivity(myIntent);
    }
}
