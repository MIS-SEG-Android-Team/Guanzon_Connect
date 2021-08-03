/*
 * Created by Android Team MIS-SEG Year 2021
 * Copyright (c) 2021. Guanzon Central Office
 * Guanzon Bldg., Perez Blvd., Dagupan City, Pangasinan 2400
 * Project name : GhostRider_Android
 * Module : GhostRider_Android.notifications
 * Electronic Personnel Access Control Security System
 * project file created : 5/22/21 11:35 AM
 * project file last modified : 5/22/21 11:35 AM
 */

package org.rmj.guanzongroup.g3msg_notifylib.Function;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;
import org.rmj.appdriver.base.GConnection;
import org.rmj.emm.EMM;
import org.rmj.emm.EMMFactory;
import org.rmj.g3appdriver.Database.DbConnection;
import org.rmj.g3appdriver.Database.Entities.ENotificationMaster;
import org.rmj.g3appdriver.Database.Entities.ENotificationRecipient;
import org.rmj.g3appdriver.Database.Entities.ENotificationUser;
import org.rmj.g3appdriver.Database.Repositories.RNotificationInfo;
import org.rmj.g3appdriver.Http.HttpHeaders;
import org.rmj.g3appdriver.etc.AppConstants;
import org.rmj.g3appdriver.utils.ConnectionUtil;
import org.rmj.g3appdriver.utils.WebApi;
import org.rmj.g3appdriver.utils.WebClient;
import org.rmj.guanzongroup.g3msg_notifylib.Etc.GNotifBuilder;
import org.rmj.guanzongroup.g3msg_notifylib.Etc.NotificationAssets;
import org.rmj.guanzongroup.g3msg_notifylib.Etc.RemoteMessageParser;

import java.util.Date;
import java.util.Objects;


public class AndroidNotificationManager {
    private static final String TAG  = AndroidNotificationManager.class.getSimpleName();

    private final Application instance;

    public AndroidNotificationManager(Application application) {
        this.instance = application;
    }

    public void DoAction(RemoteMessage foMessage){
        new SendResponseTask(instance, foMessage).execute();
    }

    private static class SendResponseTask extends AsyncTask<Void, Void, String> {
        private final Application instance;
        private final RemoteMessageParser loParser;
        private final ConnectionUtil loConn;
        private final HttpHeaders poHeaders;
        private final RNotificationInfo poNotification;
        private final GConnection loDbConn;
        private final WebApi webApi;

        public SendResponseTask(Application application, RemoteMessage remoteMessage) {
            this.instance = application;
            this.loParser = new RemoteMessageParser(remoteMessage);
            this.loConn = new ConnectionUtil(instance);
            this.poHeaders = HttpHeaders.getInstance(instance);
            this.poNotification = new RNotificationInfo(instance);
            this.webApi = new WebApi(instance);
            this.loDbConn = DbConnection.doConnect(instance);
        }

        @SuppressLint("NewApi")
        @Override
        protected String doInBackground(Void... voids) {
            String lsResponse = "";
            try{
                JSONObject data = new JSONObject();
                data.put("title", NotificationAssets.getAppTitle(loParser.getValueOf("msgmon"),
                        loParser.getDataValueOf("title"), loParser.getValueOf("srcenm")));
                data.put("message", loParser.getDataValueOf("message"));
                data.put("appsrce", loParser.getValueOf("appsrce"));
                data.put("msgtype", loParser.getValueOf("msgmon"));

                ENotificationMaster loMaster = new ENotificationMaster();
                loMaster.setTransNox(poNotification.getClientNextMasterCode());
                loMaster.setMesgIDxx(loParser.getValueOf("transno"));
                loMaster.setParentxx(loParser.getValueOf("parent"));
                loMaster.setCreatedx(loParser.getValueOf("stamp"));
                loMaster.setAppSrcex(loParser.getValueOf("appsrce"));
                loMaster.setCreatrID(loParser.getValueOf("srceid"));
                loMaster.setCreatrNm(loParser.getValueOf("srcenm"));
                loMaster.setDataSndx(loParser.getValueOf("infox"));
                loMaster.setMsgTitle(loParser.getDataValueOf("title"));
                loMaster.setMessagex(loParser.getDataValueOf("message"));
                loMaster.setMsgTypex(loParser.getValueOf("msgmon"));

                ENotificationRecipient loRecpnt = new ENotificationRecipient();
                loRecpnt.setTransNox(loParser.getValueOf("transno"));
                loRecpnt.setAppRcptx(loParser.getValueOf("apprcpt"));
                loRecpnt.setRecpntID(loParser.getValueOf("rcptid"));
                loRecpnt.setRecpntNm(loParser.getValueOf("rcptnm"));
                loRecpnt.setMesgStat(loParser.getValueOf("status"));
                loRecpnt.setReceived(AppConstants.DATE_MODIFIED);
                loRecpnt.setTimeStmp(AppConstants.DATE_MODIFIED);

                ENotificationUser loUser = new ENotificationUser();
                loUser.setUserIDxx(loParser.getValueOf("srceid"));
                loUser.setUserName(loParser.getValueOf("srcenm"));

                poNotification.insertNotificationInfo(loMaster, loRecpnt, loUser);

                String lsValue = loParser.getValueOf("infox");
                JSONObject loJSON = new JSONObject(lsValue);

                if ("00001".equalsIgnoreCase(loJSON.getString("module"))) { //table update
                    EMM instance = EMMFactory.make(EMMFactory.NMM_SysMon_Type.TABLE_UPDATE);
                    instance.setConnection(loDbConn); //pass the iGConnection here
                    instance.setData(lsValue);
                    if (!instance.execute()) System.err.println(instance.getMessage());
                }

                if(loConn.isDeviceConnected()){
                    String lsMessageID = loMaster.getMesgIDxx();
                    JSONObject params = new JSONObject();
                    params.put("transno", lsMessageID);
                    params.put("status", "2");
                    params.put("stamp", AppConstants.DATE_MODIFIED);
                    params.put("infox", "");

                    String response = WebClient.httpsPostJSon(webApi.SEND_RESPONSE, data.toString(), poHeaders.getHeaders());
                    JSONObject loJson = new JSONObject(Objects.requireNonNull(response));
                    Log.e(TAG, loJson.getString("result"));
                    String lsResult = loJson.getString("result");
                    if(lsResult.equalsIgnoreCase("success")){
                        poNotification.updateRecipientRecievedStat(lsMessageID,AppConstants.DATE_MODIFIED);
                    }
                } else {
                    lsResponse = AppConstants.NO_INTERNET();
                }
            } catch (Exception e){
                e.printStackTrace();
            }
            return lsResponse;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            int lnChannelID = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
            GNotifBuilder.createNotification(instance,
                    loParser.getDataValueOf("title"),
                    loParser.getDataValueOf("message"),
                    lnChannelID).show();
        }
    }
}
