package ui_update.android.com.testupdatinguifromservice;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class NotifyReceiver extends BroadcastReceiver {

    //вказує , чи показувався вже Notification чи ще ні
    public static final String NOTIFICATION_START_BOOL = "notification.flag";
    //час Notification , березть з possibleTime
    public static final String NOTIFICATION_TIME_DATA = "notification.time";

    //в потрібний час (possibleTime), сюди прилітають інтенти . Звідси відсилаються Notification з extras
    @Override
    public void onReceive(Context context, Intent intent) {

        String currentTime = intent.getStringExtra(TempService.BROADCAST_TIME);
        if (intent.getBooleanExtra(TempService.BROADCAST_KEY, false))
            for (int i = 0; i < WorkBreakFragment.mTimeList.size(); i++) {
                if (WorkBreakFragment.mTimeList.get(i).equals(currentTime) & !WorkBreakFragment.mBoolList.get(i)) {
                    WorkBreakFragment.mBoolList.set(i, true);
                    sendNotification(context, currentTime);
                }
            }
    }

    public void sendNotification(Context context, String time) {
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent x = new Intent(context, WorkBreakReminder.class).putExtra(NOTIFICATION_START_BOOL, true).putExtra(NOTIFICATION_TIME_DATA, time);
        Notification notification = new Notification.Builder(context)
                .setContentIntent(PendingIntent.getActivity(context, 0, x, PendingIntent.FLAG_UPDATE_CURRENT))
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true).setWhen(System.currentTimeMillis())
                .setContentTitle("It's time to make a break ")
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentText("You are working more than " + time)
                .build();
        notification.flags |= Notification.FLAG_INSISTENT;
        nm.notify(0, notification);
    }
}
