package org.rmj.guanzongroup.appcore.GCardCore;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.IOException;

public class GCardSystem {
    private static final String TAG = GCardSystem.class.getSimpleName();

    private Context mContext;

    public interface OnValidateCallback{
        void OnValidate(boolean isValid, String message);
    }

    public interface GCardSystemCallback{
        void OnSuccess(String args);
        void OnFailed(String message);
    }

    public enum CoreFunctions{
        GCARD,
        REDEEMPTION
    }

    private final GCardManager poGcard;
    private final ItemRedemptionManager poRedeem;

    public GCardSystem(Context context) {
        this.mContext = context;
        this.poGcard = new GCardManager(mContext);
        this.poRedeem = new ItemRedemptionManager(mContext);
    }

    public iGCardSystem getInstance(CoreFunctions core){
        if(core == CoreFunctions.GCARD){
            return new GCardManager(mContext);
        } else {
            return new ItemRedemptionManager(mContext);
        }
    }
}
