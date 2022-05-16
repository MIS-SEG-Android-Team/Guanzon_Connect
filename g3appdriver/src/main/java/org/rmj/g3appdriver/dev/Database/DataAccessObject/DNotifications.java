/*
 * Created by Android Team MIS-SEG Year 2021
 * Copyright (c) 2021. Guanzon Central Office
 * Guanzon Bldg., Perez Blvd., Dagupan City, Pangasinan 2400
 * Project name : GhostRider_Android
 * Module : GhostRider_Android.g3appdriver
 * Electronic Personnel Access Control Security System
 * project file created : 4/24/21 3:19 PM
 * project file last modified : 4/24/21 3:18 PM
 */

package org.rmj.g3appdriver.dev.Database.DataAccessObject;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import org.rmj.g3appdriver.dev.Database.Entities.ENotificationMaster;
import org.rmj.g3appdriver.dev.Database.Entities.ENotificationRecipient;
import org.rmj.g3appdriver.dev.Database.Entities.ENotificationUser;

import java.util.List;

@Dao
public interface DNotifications {

    @Insert
    void insert(ENotificationMaster notificationMaster);

    @Insert
    void insert(ENotificationRecipient notificationRecipient);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ENotificationUser notificationUser);

    @Update
    void update(ENotificationMaster notificationMaster);

    @Update
    void update(ENotificationRecipient notificationRecipient);

    @Update
    void update(ENotificationUser notificationUser);

    @Query("UPDATE Notification_Info_Recepient SET cMesgStat =:status WHERE sTransNox =:MessageID")
    void updateNotificationStatusFromOtherDevice(String MessageID, String status);

    @Query("SELECT COUNT(*) FROM Notification_Info_Master WHERE sMesgIDxx=:TransNox")
    int CheckNotificationIfExist(String TransNox);

    @Query("UPDATE Notification_Info_Recepient SET " +
            "dLastUpdt =:DateTime, " +
            "cMesgStat = '2', " +
            "cStatSent = '0' " +
            "WHERE sTransNox =:MessageID")
    void updateRecipientRecievedStatus(String MessageID, String DateTime);

    @Query("SELECT a.sMesgIDxx AS MesgIDxx," +
            "a.sAppSrcex AS AppSrcex," +
            "b.dReceived AS Received," +
            "a.sMessagex AS Messagex," +
            "a.sCreatrID AS CreatrID," +
            "a.sCreatrNm AS CreatrNm," +
            "b.cMesgStat AS MesgStat," +
            "a.sMsgTitle AS MsgTitle," +
            "a.sMsgTypex AS MsgTypex " +
            "FROM Notification_Info_Master a " +
            "LEFT JOIN Notification_Info_Recepient b " +
            "ON a.sMesgIDxx = b.sTransNox " +
            "WHERE b.cMesgStat <> '5' " +
            "AND a.sMsgTypex <> '00000' " +
            "AND b.sRecpntID = (SELECT sUserIDxx FROM Client_Info_Master)")
    LiveData<List<ClientNotificationInfo>> getClientNotificationList();

    @Query("SELECT a.sMesgIDxx AS MesgIDxx, " +
            "a.sMsgTitle AS MsgTitle, " +
            "a.sCreatrID AS CreatrID, " +
            "a.sCreatrNm AS CreatrNm, " +
            "a.sMessagex AS Messagex, " +
            "b.dReceived AS Received " +
            "FROM Notification_Info_Master a " +
            "LEFT JOIN Notification_Info_Recepient b " +
            "ON a.sMesgIDxx = b.sTransNox " +
            "WHERE b.cMesgStat <> '5' " +
            "AND a.sMsgTypex == '00000' " +
            "AND b.sRecpntID = (SELECT sUserIDxx FROM Client_Info_Master)")
    LiveData<List<UserNotificationInfo>> getUserMessageList();

    @Query("SELECT a.sMesgIDxx AS MesgIDxx, " +
            "a.sMsgTitle AS MsgTitle, " +
            "a.sCreatrID AS CreatrID, " +
            "a.sCreatrNm AS CreatrNm, " +
            "a.sMessagex AS Messagex, " +
            "b.dReceived AS Received " +
            "FROM Notification_Info_Master a " +
            "LEFT JOIN Notification_Info_Recepient b " +
            "ON a.sMesgIDxx = b.sTransNox " +
            "WHERE b.cMesgStat <> '5' " +
            "AND a.sMsgTypex == '00000' " +
            "AND b.sRecpntID = (SELECT sUserIDxx FROM Client_Info_Master) " +
            "AND a.sCreatrID=:SenderID")
    LiveData<List<UserNotificationInfo>> getUserMessageListFromSender(String SenderID);

    @Query("SELECT a.sMesgIDxx AS MesgIDxx, " +
            "a.sMsgTitle AS MsgTitle, " +
            "a.sCreatrID AS CreatrID, " +
            "a.sCreatrNm AS CreatrNm, " +
            "a.sMessagex AS Messagex, " +
            "b.dReceived AS Received " +
            "FROM Notification_Info_Master a " +
            "LEFT JOIN Notification_Info_Recepient b " +
            "ON a.sMesgIDxx = b.sTransNox " +
            "WHERE b.cMesgStat <> '5'" +
            "AND a.sMsgTypex == '00000' " +
            "AND a.sMsgTypex == '00000' " +
            "AND b.sRecpntID = (SELECT sUserIDxx FROM Client_Info_Master) " +
            "GROUP BY a.sCreatrID")
    LiveData<List<UserNotificationInfo>> getUserMessageListGroupByUser();

    @Query("SELECT COUNT(*) FROM Notification_Info_Recepient a " +
            "LEFT JOIN Notification_Info_Master b " +
            "ON a.sTransNox = b.sMesgIDxx " +
            "WHERE a.cMesgStat = '2' AND a.sRecpntID = (" +
            "SELECT sUserIDxx FROM Client_Info_Master) " +
            "AND b.sMsgTypex == '00000'")
    LiveData<Integer> getUnreadMessagesCount();

    @Query("SELECT COUNT(*) FROM Notification_Info_Recepient a " +
            "LEFT JOIN Notification_Info_Master b " +
            "ON a.sTransNox = b.sMesgIDxx " +
            "WHERE a.cMesgStat = '2' AND a.sRecpntID = (" +
            "SELECT sUserIDxx FROM Client_Info_Master) " +
            "AND b.sMsgTypex <> '00000'")
    LiveData<Integer> getUnreadNotificationsCount();

    @Query("SELECT a.sMesgIDxx AS MesgIDxx, " +
            "a.sMsgTitle AS MsgTitle, " +
            "a.sCreatrID AS CreatrID, " +
            "a.sCreatrNm AS CreatrNm, " +
            "a.sMessagex AS Messagex, " +
            "b.dReceived AS Received " +
            "FROM Notification_Info_Master a " +
            "LEFT JOIN Notification_Info_Recepient b " +
            "ON a.sMesgIDxx = b.sTransNox " +
            "WHERE b.cMesgStat <> '5' " +
            "AND a.sMsgTypex <> '00000' " +
            "AND b.sRecpntID = (SELECT sUserIDxx FROM Client_Info_Master)")
    LiveData<List<UserNotificationInfo>> getUserNotificationList();

    @Query("SELECT a.sMesgIDxx FROM Notification_Info_Master a " +
            "LEFT JOIN Notification_Info_Recepient b " +
            "ON a.sMesgIDxx = b.sTransNox WHERE a.sCreatrID =:SenderID " +
            "AND b.cMesgStat == 2 AND a.sMsgTypex == '00000'")
    List<String> getReadMessagesIDFromSender(String SenderID);

    @Query("UPDATE Notification_Info_Recepient SET " +
            "dReadxxxx =:DateTime, " +
            "cMesgStat = '3', " +
            "cStatSent = '0' " +
            "WHERE sTransNox =:MessageID")
    void updateRecipientReadStatus(String MessageID, String DateTime);

    @Query("SELECT dReadxxxx FROM Notification_Info_Recepient WHERE sTransNox =:MessageID")
    String getReadMessageTimeStamp(String MessageID);

    @Query("UPDATE Notification_Info_Recepient SET " +
            "dReadxxxx =:DateTime, " +
            "cMesgStat = '3', " +
            "cStatSent = '0' " +
            "WHERE sTransNox =(SELECT sMesgIDxx FROM Notification_Info_Master WHERE sCreatrID=:SenderID) " +
            "AND cMesgStat == '2'")
    void updateMessageReadStatus(String SenderID, String DateTime);

    class ClientNotificationInfo{
        public String MesgIDxx;
        public String AppSrcex;
        public String Received;
        public String Messagex;
        public String CreatrID;
        public String CreatrNm;
        public String MesgStat;
        public String MsgTitle;
        public String MsgTypex;
    }

    class UserNotificationInfo{
        public String MesgIDxx;
        public String MsgTitle;
        public String CreatrID;
        public String CreatrNm;
        public String Messagex;
        public String Received;
    }
}


