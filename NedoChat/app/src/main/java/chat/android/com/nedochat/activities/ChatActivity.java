package chat.android.com.nedochat.activities;

import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import java.util.List;

import chat.android.com.nedochat.AppResultsReceiver;
import chat.android.com.nedochat.DBUtils.MsgDataSource;
import chat.android.com.nedochat.DBUtils.Utils;
import chat.android.com.nedochat.MessagesListAdapter;
import chat.android.com.nedochat.MyService;
import chat.android.com.nedochat.POJO.Message;
import chat.android.com.nedochat.R;


public class ChatActivity extends ActionBarActivity implements AppResultsReceiver.Receiver {

    private static final String LOG_SERVICE_ERROR = "com.android.nedochat.service_error";
    private EditText inputMsg;

    // Chat messages list adapter
    private List<Message> listMessages;
    private MessagesListAdapter adapter;

    private Utils utils;
    private MsgDataSource dataSource;

    private AppResultsReceiver mReceiver;
    public Intent serviceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_main);

        inputMsg = (EditText) findViewById(R.id.inputMsg);
        ListView listViewMessages = (ListView) findViewById(R.id.list_view_messages);

        utils = Utils.get(getApplicationContext());
        dataSource = MsgDataSource.get(getApplicationContext());
        dataSource.open();

        // Getting the person name from previous screen and saving to SharedPreferences
        utils.storeSessionUser(getIntent().getStringExtra("name"));

        //Getting current user messages from DB and inflating ListView
        listMessages = dataSource.getAllSessionMessage(utils.getSessionUser());
        adapter = new MessagesListAdapter(this, listMessages);
        listViewMessages.setAdapter(adapter);
    }

    public void sendButtonClick(View view) {
        //Get MSG from EditText and to DB and adapter ( ListView)
        appendMessage(utils.getSessionUser(), inputMsg.getText().toString());
        inputMsg.setText("");
    }

    private void appendMessage(final String userName, final String msg) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Message m = dataSource.createMSG(userName, msg);
                listMessages.add(m);
                adapter.notifyDataSetChanged();

                // Playing device's notification
                playBeep();
            }
        });
    }

    public void playBeep() {
         //beep sound ))
        try {
            Uri notification = RingtoneManager
                    .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(),
                    notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //creating and enabling ResultReceiver
        mReceiver = new AppResultsReceiver(new Handler());
        mReceiver.setReceiver(this);
    }

    @Override
    protected void onDestroy() {
        System.out.println("DESTROY CHAT");
        try {
            stopService(serviceIntent);
        } catch (Exception e) {
            Log.e(LOG_SERVICE_ERROR, "Service dead", e);
        }
        mReceiver.setReceiver(null);
        dataSource.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chat_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //Delete all current user messages
            case R.id.shortcut:
                dataSource.deleteSessionMsg(utils.getSessionUser());
                listMessages.clear();
                adapter.notifyDataSetChanged();
                return true;
            //Logout ( deleting current user name from sharedPreferences) and move to StartActivity
            case R.id.logout:
                try {
                    stopService(serviceIntent);
                } catch (Exception e) {
                    Log.e(LOG_SERVICE_ERROR, "Service dead", e);
                }
                mReceiver.setReceiver(null);
                utils.logoutSessionUser();
                startActivity(new Intent(this, StartActivity.class));
                return true;
            case R.id.start_service:
                serviceIntent = new Intent(this, MyService.class);
                serviceIntent.putExtra(MyService.CONSTANT_RECEIVER, mReceiver);
                startService(serviceIntent);
                return true;
            case R.id.stop_service:
                try {
                    stopService(serviceIntent);
                } catch (Exception e) {
                    Log.e(LOG_SERVICE_ERROR, "Service dead", e);
                }
                return true;
            //delete ALL messages
            case R.id.delete_all:
                dataSource.delete();
                listMessages.clear();
                adapter.notifyDataSetChanged();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle data) {
        //getting data from IntentService
        if (resultCode == MyService.CONSTANT_STATUS_FINISHED)
            appendMessage(data.getString(MyService.CONSTANT_RECEIVER_DATA, "Default Name"),
                    data.getString(MyService.CONSTANT_RECEIVER_MSG, "Def Msg"));
    }
}
