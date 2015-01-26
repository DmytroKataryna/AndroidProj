package kat.android.com.readerrss;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class RssAdapter extends BaseAdapter {

    private final List<RssItem> items;
    private final Context context;

    public RssAdapter(Context context, List<RssItem> items) {
        this.items = items;
        this.context = context;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.rss_item, null);
            holder = new ViewHolder();
            holder.itemTitle = (TextView) convertView.findViewById(R.id.itemTitle);
            holder.reportedDateView = (TextView) convertView.findViewById(R.id.date);
            holder.imageView = (ImageView) convertView.findViewById(R.id.thumbImage);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        RssItem newsItem = items.get(position);
        holder.itemTitle.setText(newsItem.getTitle());
        //Substring date because in yahoo , by default date are like "Tue, 28 Oct 2014 13:45:23 -0400"
        //So i cutting of "-0400""
        holder.reportedDateView.setText(newsItem.getDate().substring(0, 25));
        holder.imageView.setImageBitmap(newsItem.getAttachmentUrl());
        return convertView;
    }

    static class ViewHolder {
        TextView itemTitle;
        TextView reportedDateView;
        ImageView imageView;
    }
}
