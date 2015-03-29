package example.com.finder.Layouts;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import example.com.finder.Adapters.LikesListViewAdapter;
import example.com.finder.POJO.Person;
import example.com.finder.R;
import example.com.finder.Utils.DownloadImageTask;
import example.com.finder.Utils.NetUtils;
import example.com.finder.Utils.PeopleUtils;
import example.com.finder.Utils.SharedPreferencesUtils;

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
    private Button yoButton;
    private TextView gotYOedTextView;
    private ImageView profilePictureImageView;

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
            profilePictureImageView = (ImageView) view.findViewById(R.id.person_detail_profile_picture_imageview);
            gotYOedTextView = (TextView) view.findViewById(R.id.person_detail_you_got_yoed_textview);
            yoButton = (Button) view.findViewById(R.id.person_detail_yo_button);
            yoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String POST_URL = "http://192.168.1.153:8000/yo/";
                    List<String> keys = new ArrayList<>();
                    List<String> values = new ArrayList<>();
                    keys.add("facebook_id");
                    keys.add("target_facebook_id");
                    values.add(SharedPreferencesUtils.getFacebookUserId(context));
                    values.add(PeopleUtils.getPeople().get(PeopleUtils.getCurrentPersonIndex()).getId());
                    NetUtils.PostData(keys, values, POST_URL);

                    Person p = PeopleUtils.getPeople().get(PeopleUtils.getCurrentPersonIndex());
                    p.setSentYo(true);

                    PeopleUtils.getPeople().set(PeopleUtils.getCurrentPersonIndex(), p);
                    yoButton.setVisibility(View.GONE);
                }
            });

            if(PeopleUtils.getPeople().get(PeopleUtils.getCurrentPersonIndex()).isSentYo())
            {
                yoButton.setVisibility(View.GONE);
            }
            else
            {
                yoButton.setVisibility(View.VISIBLE);
            }
            if(PeopleUtils.getPeople().get(PeopleUtils.getCurrentPersonIndex()).isReceivedYo())
            {
                gotYOedTextView.setVisibility(View.VISIBLE);
            }
            else
            {
                gotYOedTextView.setVisibility(View.GONE);
            }

            profilePictureImageView.requestFocus();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    DownloadImageTask.getImage(context, profilePictureImageView, PeopleUtils.getPeople().get(PeopleUtils.getCurrentPersonIndex()).getPictureUrl());
                }
            }).start();
            peopleListView.setFocusable(false);
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