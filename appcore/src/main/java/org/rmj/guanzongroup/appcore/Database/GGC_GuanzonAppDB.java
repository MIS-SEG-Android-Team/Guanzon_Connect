package org.rmj.guanzongroup.appcore.Database;


import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import org.rmj.guanzongroup.appcore.Database.DataAccessObject.DBranchInfo;
import org.rmj.guanzongroup.appcore.Database.DataAccessObject.DClientInfo;
import org.rmj.guanzongroup.appcore.Database.DataAccessObject.DEmployeeInfo;
import org.rmj.guanzongroup.appcore.Database.DataAccessObject.DEvents;
import org.rmj.guanzongroup.appcore.Database.DataAccessObject.DGCardTransactionLedger;
import org.rmj.guanzongroup.appcore.Database.DataAccessObject.DGcardApp;
import org.rmj.guanzongroup.appcore.Database.DataAccessObject.DMCSerialRegistration;
import org.rmj.guanzongroup.appcore.Database.DataAccessObject.DNotifications;
import org.rmj.guanzongroup.appcore.Database.DataAccessObject.DPromo;
import org.rmj.guanzongroup.appcore.Database.DataAccessObject.DRawDao;
import org.rmj.guanzongroup.appcore.Database.DataAccessObject.DRedeemItemInfo;
import org.rmj.guanzongroup.appcore.Database.DataAccessObject.DRedeemablesInfo;
import org.rmj.guanzongroup.appcore.Database.DataAccessObject.DServiceInfo;
import org.rmj.guanzongroup.appcore.Database.DataAccessObject.DUserInfo;
import org.rmj.guanzongroup.appcore.Database.Entities.EBranchInfo;
import org.rmj.guanzongroup.appcore.Database.Entities.EClientInfo;
import org.rmj.guanzongroup.appcore.Database.Entities.EEmployeeInfo;
import org.rmj.guanzongroup.appcore.Database.Entities.EEvents;
import org.rmj.guanzongroup.appcore.Database.Entities.EGCardTransactionLedger;
import org.rmj.guanzongroup.appcore.Database.Entities.EGcardApp;
import org.rmj.guanzongroup.appcore.Database.Entities.EMCSerialRegistration;
import org.rmj.guanzongroup.appcore.Database.Entities.ENotificationMaster;
import org.rmj.guanzongroup.appcore.Database.Entities.ENotificationRecipient;
import org.rmj.guanzongroup.appcore.Database.Entities.ENotificationUser;
import org.rmj.guanzongroup.appcore.Database.Entities.EPromo;
import org.rmj.guanzongroup.appcore.Database.Entities.ERedeemItemInfo;
import org.rmj.guanzongroup.appcore.Database.Entities.ERedeemablesInfo;
import org.rmj.guanzongroup.appcore.Database.Entities.EServiceInfo;
import org.rmj.guanzongroup.appcore.Database.Entities.ETokenInfo;
import org.rmj.guanzongroup.appcore.Database.Entities.EUserInfo;

@Database(entities = {
        EEvents.class,
        EBranchInfo.class,
        EClientInfo.class,
        EGcardApp.class,
        EGCardTransactionLedger.class,
        EMCSerialRegistration.class,
        EPromo.class,
        ENotificationMaster.class,
        ENotificationRecipient.class,
        ENotificationUser.class,
        ERedeemablesInfo.class,
        ERedeemItemInfo.class,
        EServiceInfo.class,
        EEmployeeInfo.class,
        EUserInfo.class,
        ETokenInfo.class}, version = 2, exportSchema = false)
public abstract class GGC_GuanzonAppDB extends RoomDatabase {
    private static final String TAG = "GuanzonApp_DB_Manager";
    private static GGC_GuanzonAppDB instance;


//    public abstract DAppEventInfo EAppEventDao();
    public abstract DBranchInfo EBranchDao();
    public abstract DClientInfo EClientDao();
    public abstract DGcardApp EGcardAppDao();
    public abstract DGCardTransactionLedger EGCardTransactionLedgerDao();
    public abstract DMCSerialRegistration EMCSerialRegistrationDao();
    public abstract DPromo EPromoDao();
    public abstract DRedeemablesInfo ERedeemablesDao();
    public abstract DRedeemItemInfo ERedeemItemDao();
    public abstract DServiceInfo EServiceDao();
    public abstract DUserInfo EUserInfoDao();
    public abstract DRawDao RawDao();
    public abstract DEmployeeInfo EmployeeDao();
    public abstract DNotifications NotificationDao();
    public abstract DEvents EventDao();

    public static synchronized GGC_GuanzonAppDB getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    GGC_GuanzonAppDB.class, "GGC_ISysDBF.db")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .addCallback(roomCallBack)
                    .build();
        }
        return instance;
    }

    private static final Callback roomCallBack = new Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            Log.e(TAG, "Local database has been created.");
        }
    };

}