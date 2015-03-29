package example.com.finder.Utils;

import android.content.Context;
import android.util.Log;

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

/**
 * Created by Alexandru on 29-Mar-15.
 */
public class NetUtils {

    public static void PostData(final List<String> keys, final List<String> values, final String POST_URL)
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
                    for(int i = 0; i < keys.size(); i++)
                    {
                        nameValuePairs.add(new BasicNameValuePair(keys.get(i), values.get(i)));
                    }
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

    public static String GetData(String GET_URL) {
        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(GET_URL);

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

}
