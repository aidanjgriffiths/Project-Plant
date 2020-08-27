package com.example.plantproject.Controller;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.plantproject.R;

import static com.example.plantproject.App.CHANNEL_ID;

public class Receiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        showNotification(context);
    }

    public void showNotification(Context context) {

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle("Plantiful")
                .setContentText("Time to check your plants!")
                .setSmallIcon(R.drawable.plant)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setAutoCancel(true)
                .build();

        notificationManager.notify(888, notification);
    }
}
