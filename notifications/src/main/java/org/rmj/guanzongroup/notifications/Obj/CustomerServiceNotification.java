package org.rmj.guanzongroup.notifications.Obj;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.google.firebase.messaging.RemoteMessage;

import org.rmj.g3appdriver.dev.Database.Entities.ENotificationMaster;
import org.rmj.g3appdriver.lib.Notifications.RemoteMessageParser;
import org.rmj.guanzongroup.notifications.Etc.iNotificationUI;
import org.rmj.guanzongroup.notifications.R;

import java.util.Date;

public class CustomerServiceNotification implements iNotificationUI {
    private static final String TAG = CustomerServiceNotification.class.getSimpleName();

    private final Context mContext;
    private final ENotificationMaster poMessage;
    private final NotificationManager loManager;

    public static final String NotificationID = "org.rmj.guanconnect.customerservice";
    private static final String CHANNEL_NAME = "Customer Service";
    private static final String CHANNEL_DESC = "Guanzon connect rewards notification for panalo participants.";

    public CustomerServiceNotification(Context mContext, ENotificationMaster message) {
        this.mContext = mContext;
        this.poMessage = message;
        this.loManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
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

            String lsTitlexx = poMessage.getMsgTitle();
            String lsMessage = poMessage.getMessagex();

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
