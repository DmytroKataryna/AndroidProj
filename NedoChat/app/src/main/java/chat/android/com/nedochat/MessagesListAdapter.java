package chat.android.com.nedochat;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import chat.android.com.nedochat.POJO.Message;


public class MessagesListAdapter extends BaseAdapter {

    private Context context;
    private List<Message> messagesItems;

    public MessagesListAdapter(Context context, List<Message> msgItems) {
        this.context = context;
        this.messagesItems = msgItems;
    }


    @Override
    public int getCount() {
        return messagesItems.size();
    }

    @Override
    public Object getItem(int position) {
        return messagesItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Message m = messagesItems.get(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        // Identifying the message owner
        if (messagesItems.get(position).isSelf()) {
            // message belongs to you, so load the right aligned layout
            convertView = mInflater.inflate(R.layout.list_item_message_right,
                    null);
        } else {
            // message belongs to other person, load the left aligned layout
            convertView = mInflater.inflate(R.layout.list_item_message_left,
                    null);
        }

        TextView lblFrom = (TextView) convertView.findViewById(R.id.lblMsgFrom);
        TextView txtMsg = (TextView) convertView.findViewById(R.id.txtMsg);

        lblFrom.setText(m.getUser());
        txtMsg.setText(Html.fromHtml(strangeHexMethod(m.getMessage())));

        return convertView;
    }


    public String strangeHexMethod(String z) {
        //Split string by space and find string which start by # ( HEX )
        String[] arr = z.split(" ");
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < arr.length; i++) {
            if (arr[i].startsWith("#") && arr[i].length() > 1) {
                //if string start with # = make her color red
                arr[i] = "<font color=#ff0000>".concat(arr[i]).concat("</font>");
            }
            result.append(arr[i]).append(" ");
        }

        return result.toString();
    }
}
