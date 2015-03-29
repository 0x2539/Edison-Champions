package example.com.finder.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import example.com.finder.Layouts.RoundImageView;
import example.com.finder.POJO.Person;
import example.com.finder.R;
import example.com.finder.Utils.DownloadImageTask;

/**
 * Created by Alexandru on 28-Mar-15.
 */
public class PeopleListViewAdapter extends BaseAdapter {
    /**
     * The inflater.
     */
    protected LayoutInflater inflater;

    /**
     * The items list.
     */
    protected List<Person> items;

    /**
     * The context.
     */
    private Context context;

    /**
     * The Class ViewHolder.
     */
    private static class ViewHolder {
        TextView nameTextView;
        TextView mutualFriendsTextView;
        TextView mutualLikesTextView;
        RoundImageView profilePictureImageView;
        ImageView yoStatusImageView;
    }

    public PeopleListViewAdapter(Context context, List<Person> items) {
        this.context = context;
        this.items = items;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setPeople(List<Person> people)
    {
        items = people;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.people_item_layout, parent, false);

            holder = new ViewHolder();

            holder.nameTextView = (TextView) convertView.findViewById(R.id.people_item_layout_name_text_view);
            holder.mutualFriendsTextView = (TextView) convertView.findViewById(R.id.people_item_layout_mutual_friends_text_view);
            holder.mutualLikesTextView = (TextView) convertView.findViewById(R.id.people_item_layout_mutual_likes_text_view);
            holder.profilePictureImageView = (RoundImageView) convertView.findViewById(R.id.people_item_layout_profile_picture_image_view);
            holder.yoStatusImageView = (ImageView) convertView.findViewById(R.id.people_item_layout_yo_status_image_view);
//            holder.theSubtitleTextView = (TextView) convertView.findViewById(R.id.listview_adapter_subtitle_textview);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.nameTextView.setText(items.get(position).getName());
        holder.mutualFriendsTextView.setText(items.get(position).getMutualFriends() + "");
        holder.mutualLikesTextView.setText(items.get(position).getMutualLikes() + "");
        new Thread(new Runnable() {
            @Override
            public void run() {
                DownloadImageTask.getImage(context, holder.profilePictureImageView, items.get(position).getPictureUrl());
            }
        }).start();

        holder.yoStatusImageView.setVisibility(View.VISIBLE);
        if(items.get(position).isReceivedYo() && items.get(position).isSentYo())
        {
            holder.yoStatusImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.green));
        }
        else
        if(items.get(position).isReceivedYo())
        {
            holder.yoStatusImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.yellow));
        }
        else
        if(items.get(position).isSentYo())
        {
            holder.yoStatusImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.gray));
        }
        else {
            holder.yoStatusImageView.setVisibility(View.GONE);
        }
//        holder.theSubtitleTextView.setText("Item #" + position);

        return convertView;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

}