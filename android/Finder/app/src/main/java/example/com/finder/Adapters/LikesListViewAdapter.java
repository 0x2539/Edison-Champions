package example.com.finder.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import example.com.finder.Layouts.RoundImageView;
import example.com.finder.POJO.Like;
import example.com.finder.R;
import example.com.finder.Utils.DownloadImageTask;

/**
 * Created by Alexandru on 28-Mar-15.
 */
public class LikesListViewAdapter extends BaseAdapter {
    /**
     * The inflater.
     */
    protected LayoutInflater inflater;

    /**
     * The items list.
     */
    protected List<Like> items;

    /**
     * The context.
     */
    private Context context;

    /**
     * The Class ViewHolder.
     */
    private static class ViewHolder {
        TextView nameTextView;
        TextView categoryTextView;
    }

    public LikesListViewAdapter(Context context, List<Like> items) {
        this.context = context;
        this.items = items;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setPeople(List<Like> people)
    {
        items = people;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.like_item_layout, parent, false);

            holder = new ViewHolder();

            holder.nameTextView = (TextView) convertView.findViewById(R.id.like_item_layout_name_text_view);
            holder.categoryTextView = (TextView) convertView.findViewById(R.id.like_item_layout_category_text_view);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.nameTextView.setText(items.get(position).getName());
        holder.categoryTextView.setText(items.get(position).getCategory());
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