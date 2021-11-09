package ru.tutazaim.webview;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    static final String TAG = "Firebase";

    @Override
    public void onCreate() {
        super.onCreate();
        Notification.createChannel(this, new Notification.Info(
                getString(R.string.default_channel_id),
                getString(R.string.default_channel_name),
                getString(R.string.default_channel_description)
        ));
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.d(TAG, "New firebase token " + s);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String title = null;
        String body = null;

        Map<String, String> data = remoteMessage.getData();
        if (data != null && data.size() > 0) {
            title = data.get("title");
            body = data.get("body");
        }

        // Check if message contains a notification payload.
        RemoteMessage.Notification primary = remoteMessage.getNotification();
        if (primary != null) {
            title = primary.getTitle();
            body = primary.getBody();
        }

        if (title != null && body != null) {
            Log.d("Firebase", "Notification title " + title);
            Log.d("Firebase", "Notification text " + body);

            NotificationCompat.Builder notification = Notification.create(
                    this,
                    getString(R.string.default_channel_id),
                    new Notification.NotificationInfo(
                            getString(R.string.default_notification_id),
                            title,
                            body,
                            R.drawable.ic_launcher_foreground

                    )
            );
            Notification.show(
                    getApplicationContext(),
                    Integer.valueOf(getString(R.string.default_notification_id)),
                    notification.build());
        }
    }
}
