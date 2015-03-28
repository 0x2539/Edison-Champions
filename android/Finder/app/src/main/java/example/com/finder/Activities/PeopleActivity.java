package example.com.finder.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;

import example.com.finder.Layouts.PeopleFragLayout;
import example.com.finder.POJO.Person;
import example.com.finder.R;
import example.com.finder.Utils.JSONUtils;
import example.com.finder.Utils.PeopleUtils;


public class PeopleActivity extends ActionBarActivity implements PeopleFragLayout.OnPeopleListFragmentListener {

    private PeopleFragLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people);

        layout = (PeopleFragLayout) getSupportFragmentManager().findFragmentById(R.id.people_fragment);

        String json = "";

        PeopleUtils.setPeople(JSONUtils.fromJSON(json, new TypeReference<List<Person>>() { }));
//        layout = new PeopleFragLayout();
//        setContentView(layout);
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

    }
}
