package chat.android.com.nedochat;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

import java.util.concurrent.TimeUnit;

public class MyService extends IntentService {

    public static final String CONSTANT_RECEIVER = "receiver";
    public static final int CONSTANT_STATUS_RUNNING = 8878;
    public static final String CONSTANT_RECEIVER_DATA = "service_data_name";
    public static final String CONSTANT_RECEIVER_MSG = "service_message";
    public static final int CONSTANT_STATUS_FINISHED = 8888;

    private boolean status = true;
    int counter = 0;

    public MyService() {
        super("MyService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //Send every 3 sec some msg to ChatActivity ( to ResultReceiver which is placed in Chat Activity)
        while (status) {
            final ResultReceiver receiver = intent.getParcelableExtra(CONSTANT_RECEIVER);
            final Bundle data = new Bundle();
            try {
                TimeUnit.SECONDS.sleep(3);
                data.putString(CONSTANT_RECEIVER_DATA, "Service");
                data.putString(CONSTANT_RECEIVER_MSG, "This is msg from service #" + counter++);
            } catch (InterruptedException e) {
                data.putString(CONSTANT_RECEIVER_DATA, "Error");
            }
            receiver.send(CONSTANT_STATUS_FINISHED, data);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        status = false;

    }
}