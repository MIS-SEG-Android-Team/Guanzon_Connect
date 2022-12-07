package org.rmj.guanzongroup.notifications.Obj;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.google.firebase.messaging.RemoteMessage;

import org.rmj.g3appdriver.lib.Notifications.RemoteMessageParser;
import org.rmj.guanzongroup.notifications.Etc.iNotificationUI;
import org.rmj.guanzongroup.notifications.R;

import java.util.Date;

public class RegularNotification implements iNotificationUI {
    private static final String TAG = RegularNotification.class.getSimpleName();

    private final Context mContext;
    private final RemoteMessage poMessage;
    private final NotificationManager loManager;
    private final RemoteMessageParser poParser;

    public static final String NotificationID = "org.rmj.guanconnect.regularmessage";
    private static final String CHANNEL_NAME = "Regular Message";
    private static final String CHANNEL_DESC = "Guanzon connect notice";

    public RegularNotification(Context mContext, RemoteMessage message) {
        this.mContext = mContext;
        this.poMessage = message;
        this.loManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        this.poParser = new RemoteMessageParser(poMessage);
    }

    @Override
    public void CreateNotification() {
        try{
            Intent loIntent = new Intent();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel channel = new NotificationChannel(NotificationID, CHANNEL_NAME, importance);
                channel.setDescription(CHANNEL_DESC);
                // Register the channel with the system; you can't change the importance
                // or other notification behaviors after this
                NotificationManager notificationManager = mContext.getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }

            PendingIntent notifyPendingIntent;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                notifyPendingIntent = PendingIntent.getActivity(
                        mContext, 0, loIntent, PendingIntent.FLAG_MUTABLE);
            } else {
                notifyPendingIntent = PendingIntent.getActivity(
                        mContext, 0, loIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            }

            String lsTitlexx = poParser.getDataValueOf("title");
            String lsMessage = poParser.getDataValueOf("message");

            Notification.Builder loBuilder = new Notification.Builder(mContext)
                    .setContentIntent(notifyPendingIntent)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.ic_guanzon_logo)
                    .setContentTitle(lsTitlexx)
                    .setContentText(lsMessage);

            int lnChannelID = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);

            loManager.notify(lnChannelID, loBuilder.build());
        } catch (Exception e){
            e.printStackTrace();
        }

    }
}
