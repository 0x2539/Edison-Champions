package example.com.finder.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import example.com.finder.R;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GoToWorkerActivity();
        GoToPeopleActivity();
        GoToFacebookActivity();
        GoToServerActivity();
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


    public void GoToWorkerActivity ()
    {
        Button button = (Button)findViewById(R.id.worker_activity_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, WorkerActivity.class);
                MainActivity.this.startActivity(myIntent);
            }
        });

    }

    public void GoToPeopleActivity ()
    {
        Button button = (Button)findViewById(R.id.people_activity_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, PeopleActivity.class);
                MainActivity.this.startActivity(myIntent);
            }
        });

    }

    public void GoToFacebookActivity ()
    {
        Button button = (Button)findViewById(R.id.facebook_activity_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, LoginActivity.class);
                MainActivity.this.startActivity(myIntent);
            }
        });

    }

    public void GoToServerActivity ()
    {
        Button button = (Button)findViewById(R.id.facebook_activity_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, ServerActivity.class);
                MainActivity.this.startActivity(myIntent);
            }
        });

    }


}
