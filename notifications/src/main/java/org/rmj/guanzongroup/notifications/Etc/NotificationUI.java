package org.rmj.guanzongroup.notifications.Etc;

import android.content.Context;
import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;

import org.rmj.g3appdriver.dev.Database.Entities.ENotificationMaster;
import org.rmj.g3appdriver.lib.Notifications.RemoteMessageParser;
import org.rmj.guanzongroup.notifications.Obj.CustomerServiceNotification;
import org.rmj.guanzongroup.notifications.Obj.EventNotification;
import org.rmj.guanzongroup.notifications.Obj.InquiryNotification;
import org.rmj.guanzongroup.notifications.Obj.MPOrderNotification;
import org.rmj.guanzongroup.notifications.Obj.PanaloNotification;
import org.rmj.guanzongroup.notifications.Obj.PromoNotification;
import org.rmj.guanzongroup.notifications.Obj.RegularNotification;
import org.rmj.guanzongroup.notifications.Obj.ReviewNotification;

public class NotificationUI {
    private static final String TAG = NotificationUI.class.getSimpleName();

    private final Context mContext;

    public NotificationUI(Context context) {
        this.mContext = context;
    }

    public iNotificationUI getInstance(ENotificationMaster foVal){
        String lsSysMon = foVal.getMsgTypex();
        switch (lsSysMon){
                case "00000":
                    Log.d(TAG, "Regular message");
                    return new RegularNotification(mContext, foVal);
                case "00002":
                    Log.d(TAG, "Marketplace order status notification");
                    return new MPOrderNotification(mContext, foVal);
                case "00003":
                    Log.d(TAG, "Guanzon promotions");
                    return new PromoNotification(mContext, foVal);
                case "00004":
                    Log.d(TAG, "Event Notification");
                    return new EventNotification(mContext, foVal);
                case "00005":
                    Log.d(TAG, "Product inquiry notification");
                    return new InquiryNotification(mContext, foVal);
                case "00006":
                    Log.d(TAG, "Review notification");
                    return new ReviewNotification(mContext, foVal);
                case "00007":
                    Log.d(TAG, "Customer service notification");
                    return new CustomerServiceNotification(mContext, foVal);
                case "00008":
                    Log.d(TAG, "Guanzon panalo notification");
                    return new PanaloNotification(mContext, foVal);
                default:
                    Log.d(TAG, "Unable to identify notification");
                    return null;
        }
    }
}
