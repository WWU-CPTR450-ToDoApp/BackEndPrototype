package edu.wallawalla.dailytodolist.broadcast_receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import edu.wallawalla.dailytodolist.notifications.NotificationIntentService;

public class NotificationEventReceiver extends WakefulBroadcastReceiver {

    private static final String ACTION_START_NOTIFICATION_SERVICE = "ACTION_START_NOTIFICATION_SERVICE";
    private static final String ACTION_DELETE_NOTIFICATION = "ACTION_DELETE_NOTIFICATION";

    public static void setupAlarm(Context context,String name) {
        Log.d("N",name);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent alarmIntent = getStartPendingIntent(context,name);
        alarmIntent.getIntentSender();
        long time= System.currentTimeMillis() + 1000;
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, alarmIntent);
    }

    private static PendingIntent getStartPendingIntent(Context context, String name) {
        Intent intent = new Intent(context, NotificationEventReceiver.class);
        intent.setAction(ACTION_START_NOTIFICATION_SERVICE);
        intent.putExtra("d",name);
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("N","received");
        String action = intent.getAction();
        String b = intent.getStringExtra("d");
        String a = "msg: ";
        Log.d("N",a+b);
        Intent serviceIntent = null;
        //serviceIntent = NotificationIntentService.createIntentStartNotificationService(context);
        if (ACTION_START_NOTIFICATION_SERVICE.equals(action)) {
            Log.i(getClass().getSimpleName(), "onReceive from alarm, starting notification service");
            serviceIntent = NotificationIntentService.createIntentStartNotificationService(context);
            serviceIntent.putExtra("Name",intent.getStringExtra("d"));
        } else if (ACTION_DELETE_NOTIFICATION.equals(action)) {
            Log.i(getClass().getSimpleName(), "onReceive delete notification action, starting notification service to handle delete");
            serviceIntent = NotificationIntentService.createIntentDeleteNotification(context);
            serviceIntent.putExtra("Name",intent.getStringExtra("d"));
        }

        if (serviceIntent != null) {
            // Start the service, keeping the device awake while it is launching.
            startWakefulService(context, serviceIntent);
        }
    }
}
