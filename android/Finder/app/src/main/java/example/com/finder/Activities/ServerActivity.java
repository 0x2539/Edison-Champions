package example.com.finder.Activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import example.com.finder.R;


public class ServerActivity extends ActionBarActivity {

    final String POST_URL = "http://192.168.1.153:8000/user/";
    final String GET_URL = "http://192.168.1.153:8000/users_nearby/?mac=";
    String mac = "98:fe:94:4c:a3:ec"; // TODO: get this

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);

//        PostData();
        GetData();
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

    private void PostData()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {

                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(POST_URL);

                try {
                    // Add your data
                    List<NameValuePair> nameValuePairs = new ArrayList<>();
                    // TODO: add name, pic url etc here
                    nameValuePairs.add(new BasicNameValuePair("mac", mac));
                    post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    // Execute HTTP Post Request
                    HttpResponse response = client.execute(post);

                    //TODO: check if response is good

                } catch (ClientProtocolException e) {
                    Log.e("server", "Client protocol ex");
                } catch (IOException e) {
                    Log.e("server", "io ex");
                }

            }
        }).start();
    }

    private void GetData ()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpClient client = new DefaultHttpClient();
                HttpGet get = new HttpGet(GET_URL + mac);

                try {
                    HttpResponse response = client.execute(get);

                    BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                    StringBuilder builder = new StringBuilder();
                    for (String line; (line = reader.readLine()) != null;)
                        builder.append(line).append("\n");

                    JSONTokener tokener = new JSONTokener(builder.toString());
                    try {
                        JSONArray finalResult = new JSONArray(tokener);
                        for (int i = 0; i < finalResult.length(); i++)
                            Log.d("Server", finalResult.get(i).toString());
                    } catch (JSONException e) {
                        Log.e("Server", "JSON parsing error");
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
