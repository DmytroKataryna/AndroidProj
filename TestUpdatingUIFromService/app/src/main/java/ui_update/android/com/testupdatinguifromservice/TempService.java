package ui_update.android.com.testupdatinguifromservice;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;


public class TempService extends Service {

    public static final String BROADCAST_ACTION = "com.example.broadcast.action";
    public static final String BROADCAST_DATA = "data";
    public static final String BROADCAST_ON_PAUSE_TIME = "saved.pause.time";
    public static final String BROADCAST_TIME = "time";
    public static final String BROADCAST_KEY = "key";
    private final Handler handler = new Handler();

    //Time when notif/dialog starts
    private String[] possibleTime = {"0:0:15", "0:0:30", "0:0:45", "0:1:0", "0:1:15", "0:1:30", "0:1:45", "0:2:0"};
    private long startTime = 0L;
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;

    AlarmManager alarmManager;
    NotificationManager nm;
    Intent i;

    @Override
    public void onCreate() {
        super.onCreate();
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        i = new Intent(BROADCAST_ACTION);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //starts when User press Pause button . So I save current time (for future restoration)
        //and destroy service
        if (intent.getBooleanExtra(WorkBreakFragment.SERVICE_PAUSE_BOOL, false)) {
            i.putExtra(BROADCAST_ON_PAUSE_TIME, timeSwapBuff += timeInMilliseconds);
            sendBroadcast(i);
            stopSelf();
        } else {
            //stars when Start Button is pressed
            handler.removeCallbacks(sendUpdatesToUI);
            handler.postDelayed(sendUpdatesToUI, 1000);
            //отримати дані з активності , які добавляються при відновленні
            timeSwapBuff = intent.getLongExtra(WorkBreakFragment.SERVICE_START_DATA, 0L);
            startTime = SystemClock.uptimeMillis();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(sendUpdatesToUI);
    }

    Runnable sendUpdatesToUI = new Runnable() {
        @Override
        public void run() {
            //Створюємо секундомір
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updatedTime = timeSwapBuff + timeInMilliseconds;

            int sec = (int) (updatedTime / 1000);
            int min = sec / 60;
            sec = sec % 60;
            int hour = min / 60;
            min = min % 60;

            String mTimeCurr = hour + ":" + min + ":" + sec;
            DisplayLoggingInfo(mTimeCurr);

            //If time match possible time intent ( which creat Notofication) start
            for (String time : possibleTime) {
                if (mTimeCurr.equals(time)) {
                    Intent intentRec = new Intent(getApplicationContext(), NotifyReceiver.class);
                    intentRec.putExtra(BROADCAST_KEY, true).putExtra(BROADCAST_TIME, mTimeCurr);
                    sendBroadcast(intentRec);
                }
            }
            handler.postDelayed(this, 1000);
        }

        private void DisplayLoggingInfo(String s) {
            //Відправка в фрагмент поточного часу
            i.putExtra(BROADCAST_DATA, s);
            sendBroadcast(i);
        }
    };


}
