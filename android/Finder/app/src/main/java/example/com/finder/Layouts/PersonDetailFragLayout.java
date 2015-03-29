package example.com.finder.Layouts;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import example.com.finder.Adapters.LikesListViewAdapter;
import example.com.finder.R;
import example.com.finder.Utils.PeopleUtils;

/**
 * Created by Alexandru on 28-Mar-15.
 */
public class PersonDetailFragLayout extends BaseFragLayout {

    public interface OnPeopleListFragmentListener {
        public void onPersonClicked(int position);
    }

    private Activity context;

    private OnPeopleListFragmentListener listener;
    private View view;
    protected ListView peopleListView;

    private TextView noPeopleTextView;

    private LikesListViewAdapter likesListItemAdapter;

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        if (activity instanceof OnPeopleListFragmentListener)
        {
            listener = (OnPeopleListFragmentListener) activity;
        } else
        {
            throw new ClassCastException(activity.toString() + " must implement OnPeopleListFragmentListener");
        }
        context = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.frag_person_detail, container, false);
        initAll();

        return view;
    }

    private void initAll()
    {
        try {
            //noPeopleTextView = (TextView) view.findViewById(R.id.);
            peopleListView = (ListView) view.findViewById(R.id.person_detail_likes_listview);
            updateView();
        }
        catch (Exception e)
        {

        }
    }

    private void updateView()
    {
        if (likesListItemAdapter == null)
        {
            likesListItemAdapter = new LikesListViewAdapter(context,
                    PeopleUtils.getLikes());
            peopleListView.setAdapter(likesListItemAdapter);
        } else
        {
            likesListItemAdapter.setPeople(PeopleUtils.getLikes());
        }

        if(noPeopleTextView != null) {
            if (PeopleUtils.getPeople().size() == 0) {
                //noPeopleTextView.setVisibility(View.VISIBLE);
            } else {
                //noPeopleTextView.setVisibility(View.GONE);
            }
            likesListItemAdapter.notifyDataSetChanged();
        }
    }
}