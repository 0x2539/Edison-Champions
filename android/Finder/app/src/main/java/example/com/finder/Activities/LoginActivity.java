package example.com.finder.Activities;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookActivity;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestBatch;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import example.com.finder.R;
import example.com.finder.Utils.SharedPreferencesUtils;


public class LoginActivity extends ActionBarActivity {

    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        initFacebookButton();
    }

    private void initFacebookButton()
    {
        callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.facebook_login_button);

        loginButton.setReadPermissions(Arrays.asList("public_profile", "user_friends", "user_about_me", "user_likes"));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        signIn();
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                }
        );

        final Activity context = this;

        try {
            if (AccessToken.getCurrentAccessToken() != null) {
                signIn();
            }
            else
            {
                Log.i("login", "logged out");
            }
        }
        catch (Exception e)
        {
            Log.i("login", "logged out");
            e.printStackTrace();
        }

//        GraphRequestBatch batch = new GraphRequestBatch(
//                GraphRequest.newMeRequest(
//                        AccessToken.getCurrentAccessToken(),
//                        new GraphRequest.GraphJSONObjectCallback() {
//                            @Override
//                            public void onCompleted(
//                                    JSONObject jsonObject,
//                                    GraphResponse response) {
//                                Log.i("myself", jsonObject + "");
//                                // Application code for user
//                            }
//                        }),
//                GraphRequest.newPostRequest(
//                        AccessToken.getCurrentAccessToken(),
//                        "/3003345?fields=context",
//                        null,
//                        new GraphRequest.Callback() {
//                            public void onCompleted(GraphResponse response) {
//                                Log.i("myself2", response + "");
//            /* handle the result */
//                            }
//                        }),
//                GraphRequest.newMyFriendsRequest(
//                        AccessToken.getCurrentAccessToken(),
//                        new GraphRequest.GraphJSONArrayCallback() {
//                            @Override
//                            public void onCompleted(
//                                    JSONArray jsonArray,
//                                    GraphResponse response) {
//                                // Application code for users friends
//                                Log.i("my friends", jsonArray + "");
//                            }
//                        })
//        );
//        batch.addCallback(new GraphRequestBatch.Callback() {
//            @Override
//            public void onBatchCompleted(GraphRequestBatch graphRequests) {
//                // Application code for when the batch finishes
//            }
//        });
//        batch.executeAsync();

        //Log.i("token", AccessToken.getCurrentAccessToken().getToken() + "");
//        loginButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                LoginManager.getInstance().logInWithReadPermissions(context, Arrays.asList("public_profile", "user_friends"));
//            }
//        });
    }

    private void signIn()
    {
        Log.i("login", "logged in");
        SharedPreferencesUtils.addFacebookData(this, AccessToken.getCurrentAccessToken().getUserId(), AccessToken.getCurrentAccessToken().getToken(), getBluetoothMacAddress());
        PostData();
        Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        LoginActivity.this.startActivity(myIntent);
    }

    private void PostData()
    {
        final Context context = this;
        new Thread(new Runnable() {
            @Override
            public void run() {

                final String POST_URL = "http://192.168.1.153:8000/user/";

                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(POST_URL);

                try {
                    // Add your data
                    List<NameValuePair> nameValuePairs = new ArrayList<>();
                    // TODO: add name, pic url etc here
                    nameValuePairs.add(new BasicNameValuePair("mac", SharedPreferencesUtils.getFacebookMacAddress(context)));
                    nameValuePairs.add(new BasicNameValuePair("access_token", SharedPreferencesUtils.getFacebookAccessToken(context)));
                    nameValuePairs.add(new BasicNameValuePair("facebook_id", SharedPreferencesUtils.getFacebookUserId(context)));
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


    public String getBluetoothMacAddress() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // if device does not support Bluetooth
        if(mBluetoothAdapter==null){
            Log.d("loginactivity","device does not support bluetooth");
            return null;
        }

        return mBluetoothAdapter.getAddress();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
