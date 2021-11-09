package ru.tutazaim.webview;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationCompat.Builder;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.tasks.Task;
import com.google.firebase.installations.FirebaseInstallations;
import com.google.firebase.messaging.FirebaseMessaging;

public class Notification {
    static void show(Context context, Integer id, android.app.Notification notification){
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(id, notification);
    }
    static Builder create(Context context, String channel_id, NotificationInfo info){
        return new Builder(context, channel_id)
                .setSmallIcon(info.icon)
                .setContentTitle(info.name)
                .setContentText(info.description)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
    }
    static void createChannel(Context context, Info channel_info){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = channel_info.name;
            String description = channel_info.description;
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(channel_info.id, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    static class Info {
        String id;
        String name;
        String description;
        public Info(String id, String name, String description){
            this.id = id;
            this.name = name;
            this.description = description;
        }
    }
    static class NotificationInfo extends Info {
        Integer icon;
        public NotificationInfo(String id, String name, String description, Integer icon){
            super(id, name, description);
            this.icon = icon;
        }
    }
    public static Task<String> getToken(){
        return FirebaseMessaging.getInstance().getToken();
    }
    public static Task<String> getDeviceId(){
        return FirebaseInstallations.getInstance().getId();
    }
}
