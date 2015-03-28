package example.com.finder.Layouts;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import example.com.finder.Adapters.PeopleListViewAdapter;
import example.com.finder.R;
import example.com.finder.Utils.PeopleUtils;

/**
 * Created by Alexandru on 28-Mar-15.
 */
public class PeopleFragLayout extends BaseFragLayout {

    public interface OnPeopleListFragmentListener {
        public void onPersonClicked(int position);
    }

    private Activity context;

    private OnPeopleListFragmentListener listener;
    private View view;
    protected ListView peopleListView;

    private TextView noPeopleTextView;

    private PeopleListViewAdapter peopleListItemAdapter;

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        if (activity instanceof OnPeopleListFragmentListener)
        {
            listener = (OnPeopleListFragmentListener) activity;
        } else
        {
            throw new ClassCastException(activity.toString() + " must implemenet OnPeopleListFragmentListener");
        }
        context = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.frag_people, container, false);
        initAll();

        return view;
    }

    private void initAll()
    {
        try {
            noPeopleTextView = (TextView) view.findViewById(R.id.frag_people_empty_text_view);
            peopleListView = (ListView) view.findViewById(R.id.frag_people_list_view);
            updateView();
        }
        catch (Exception e)
        {

        }
    }

    private void updateView()
    {
        if (peopleListItemAdapter == null)
        {
            peopleListItemAdapter = new PeopleListViewAdapter(context,
                    PeopleUtils.getPeople());
            peopleListView.setAdapter(peopleListItemAdapter);
        } else
        {
            peopleListItemAdapter.setPeople(PeopleUtils.getPeople());
        }

        if(noPeopleTextView != null) {
            if (PeopleUtils.getPeople().size() == 0) {
                noPeopleTextView.setVisibility(View.VISIBLE);
            } else {
                noPeopleTextView.setVisibility(View.GONE);
            }
            peopleListItemAdapter.notifyDataSetChanged();
        }
    }
}