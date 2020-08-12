package com.example.tracc_it;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class ReminderBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "Notify Lemubit")
                .setSmallIcon(R.drawable.diet)
                .setContentTitle("Here ")
                .setContentText(" Hiiii ")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(100,builder.build());
    }
}
