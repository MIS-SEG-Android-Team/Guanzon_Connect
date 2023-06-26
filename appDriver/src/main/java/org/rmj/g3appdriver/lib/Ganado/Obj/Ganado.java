package org.rmj.g3appdriver.lib.Ganado.Obj;

import static org.rmj.g3appdriver.dev.Api.ApiResult.SERVER_NO_RESPONSE;
import static org.rmj.g3appdriver.dev.Api.ApiResult.getErrorMessage;
import static org.rmj.g3appdriver.etc.AppConstants.getLocalMessage;

import android.app.Application;
import android.util.Log;

import org.json.JSONObject;
import org.rmj.g3appdriver.GCircle.Account.EmployeeSession;
import org.rmj.g3appdriver.GCircle.room.DataAccessObject.DGanadoOnline;
import org.rmj.g3appdriver.GCircle.room.Entities.EGanadoOnline;
import org.rmj.g3appdriver.GCircle.room.GGC_GCircleDB;
import org.rmj.g3appdriver.dev.Api.HttpHeaders;
import org.rmj.g3appdriver.dev.Api.WebClient;
import org.rmj.g3appdriver.etc.AppConstants;
import org.rmj.g3appdriver.lib.Ganado.pojo.ClientInfo;
import org.rmj.g3appdriver.lib.Ganado.pojo.InquiryInfo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Ganado {
    private static final String TAG = Ganado.class.getSimpleName();

    private final Application instance;
    private final DGanadoOnline poDao;
    private final EmployeeSession poSession;
    private final HttpHeaders poHeaders;

    private String message;

    public Ganado(Application instance) {
        this.instance = instance;
        this.poDao = GGC_GCircleDB.getInstance(instance).ganadoDao();
        this.poSession = EmployeeSession.getInstance(instance);
        this.poHeaders = HttpHeaders.getInstance(instance);
    }

    public String getMessage() {
        return message;
    }

    public String CreateInquiry(InquiryInfo loInfo){
        try{
            if(!loInfo.isDataValid()){
                message = loInfo.getMessage();
                return null;
            }

            EGanadoOnline loDetail = new EGanadoOnline();
            String lsTransNo = CreateUniqueID();
            loDetail.setTransNox(lsTransNo);

            loDetail.setTransact(AppConstants.CURRENT_DATE());
            loDetail.setGanadoTp(loInfo.getGanadoTp());
            loDetail.setPaymForm(loInfo.getPaymForm());

            JSONObject joProdct = new JSONObject();
            joProdct.put("sInvTypCd", "");
            joProdct.put("sBrandIDx", "");
            joProdct.put("sModelIDx", "");
            joProdct.put("sColorIDx", "");
            loDetail.setProdInfo(joProdct.toString());

            JSONObject joPayment = new JSONObject();
            joPayment.put("sTermIDxx", "");
            joPayment.put("nDownPaym", "");
            loDetail.setPaymInfo(joPayment.toString());

            loDetail.setTargetxx("");
            loDetail.setFollowUp("");
            loDetail.setRemarksx("");
            loDetail.setReferdBy(poSession.getUserID());
            loDetail.setRelatnID("");
            loDetail.setCreatedx(AppConstants.DATE_MODIFIED());
            loDetail.setTranStat("0");
            loDetail.setSendStat("");
            loDetail.setModified(AppConstants.DATE_MODIFIED());
            loDetail.setLastUpdt("");
            loDetail.setBranchCD(poSession.getBranchCode());
            poDao.Save(loDetail);

            return lsTransNo;
        } catch (Exception e){
            e.printStackTrace();
            message = getLocalMessage(e);
            return null;
        }
    }

    public boolean SaveClientInfo(ClientInfo loInfo){
        try {
            if(!loInfo.isDataValid()){
                message = loInfo.getMessage();
                return false;
            }

            EGanadoOnline loDetail = poDao.GetInquiry(loInfo.getsTransNox());

            if(loDetail == null){
                message = "Unable to find record to update.";
                return false;
            }

            JSONObject joClient = new JSONObject();
            joClient.put("sLastName", loInfo.getLastName());
            joClient.put("sFrstName", loInfo.getFrstName());
            joClient.put("sMiddName", loInfo.getMiddName());
            joClient.put("sSuffixNm", loInfo.getSuffixNm());
            joClient.put("sMaidenNm", loInfo.getMaidenNm());
            joClient.put("cGenderCd", loInfo.getGenderCd());
            joClient.put("dBirthDte", loInfo.getBirthDte());
            joClient.put("sBirthPlc", loInfo.getBirthPlc());
            joClient.put("sHouseNox", loInfo.getHouseNox());
            joClient.put("sAddressx", loInfo.getAddressx());
            joClient.put("sTownIDxx", loInfo.getTownIDxx());
            joClient.put("sMobileNo", loInfo.getMobileNo());
            joClient.put("sEmailAdd", loInfo.getEmailAdd());

            loDetail.setClntInfo(joClient.toString());
            poDao.Update(loDetail);

            return true;
        } catch (Exception e){
            e.printStackTrace();
            message = getLocalMessage(e);
            return false;
        }
    }

    public boolean SaveInquiry(String TransNox){
        try{
            EGanadoOnline loDetail = poDao.GetInquiry(TransNox);

            if(loDetail == null){
                message = "Unable to find record to update.";
                return false;
            }

            JSONObject params = new JSONObject();
            params.put("dTransact", loDetail.getTransact());
            params.put("cGanadoTp", loDetail.getGanadoTp());
            params.put("cPaymForm", loDetail.getPaymForm());
            params.put("sClntInfo", loDetail.getClntInfo());
            params.put("sProdInfo", loDetail.getProdInfo());
            params.put("sPaymInfo", loDetail.getPaymInfo());
            params.put("sReferdBy", loDetail.getReferdBy());
            params.put("dCreatedx", loDetail.getCreatedx());

            String lsResponse = WebClient.sendRequest(
                    "",
                    params.toString(),
                    poHeaders.getHeaders());

            if(lsResponse == null){
                message = SERVER_NO_RESPONSE;
                return false;
            }

            JSONObject loResponse = new JSONObject();
            String lsResult = loResponse.getString("result");
            if(lsResult.equalsIgnoreCase("error")){
                JSONObject loError = loResponse.getJSONObject("error");
                message = getErrorMessage(loError);
                return false;
            }

            String lsTrasNox = loResponse.getString("sTransNox");
            poDao.UpdateSentInquiry(
                    loDetail.getTransNox(),
                    lsTrasNox);

            return true;
        } catch (Exception e){
            e.printStackTrace();
            message = getLocalMessage(e);
            return false;
        }
    }

    private String CreateUniqueID(){
        String lsUniqIDx = "";
        try{
            String lsBranchCd = "MX01";
            String lsCrrYear = new SimpleDateFormat("yy", Locale.getDefault()).format(new Date());
            StringBuilder loBuilder = new StringBuilder(lsBranchCd);
            loBuilder.append(lsCrrYear);

            int lnLocalID = poDao.GetRowsCountForID() + 1;
            String lsPadNumx = String.format("%05d", lnLocalID);
            loBuilder.append(lsPadNumx);
            lsUniqIDx = loBuilder.toString();
        } catch (Exception e){
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
        Log.d(TAG, lsUniqIDx);
        return lsUniqIDx;
    }
}
