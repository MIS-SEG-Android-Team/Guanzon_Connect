package org.rmj.guanzongroup.useraccount.ViewModel;

import android.app.Application;
import android.os.AsyncTask;
import android.os.PowerManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.rmj.g3appdriver.dev.Database.DataAccessObject.DAddress;
import org.rmj.g3appdriver.dev.Database.Entities.EBarangayInfo;
import org.rmj.g3appdriver.dev.Database.Entities.EClientInfo;
import org.rmj.g3appdriver.dev.Database.Entities.ECountryInfo;
import org.rmj.g3appdriver.dev.Repositories.RAddressMobile;
import org.rmj.g3appdriver.dev.Repositories.RClientInfo;
import org.rmj.g3appdriver.etc.AppConstants;
import org.rmj.g3appdriver.etc.ConnectionUtil;
import org.rmj.guanzongroup.useraccount.Model.AccountDetailsInfo;

import java.util.ArrayList;
import java.util.List;

public class VMAccountDetails extends AndroidViewModel {
    private static final String TAG = VMAccountDetails.class.getSimpleName();
    private final ConnectionUtil poConnect;
    private final RClientInfo poClientx;
    private final RAddressMobile poAddress;
    private final MutableLiveData<List<AccountDetailsInfo>> poAcctInf = new MutableLiveData<>();

    private final String[] psLstHead = new String[] {
            "Personal Information",
            "Present Address",
            "Account Information"
    };

    public VMAccountDetails(@NonNull Application application) {
        super(application);
        this.poConnect = new ConnectionUtil(application);
        this.poClientx = new RClientInfo(application);
        this.poAddress = new RAddressMobile(application);
        setAccountDetailsList();
    }

    public LiveData<EClientInfo> getClientInfo(){
        return poClientx.getClientInfo();
    }

    public ArrayList<String> getGenderList() {
        return poClientx.getGenderList();
    }

    public ArrayList<String> getCivilStatusList() {
        return poClientx.getCivilStatusList();
    }

    public LiveData<List<EBarangayInfo>> getBarangayList(String fsTownID){
        return poAddress.GetBarangayList(fsTownID);
    }

    public LiveData<List<DAddress.oTownObj>> getTownCityList(){
        return poAddress.GetTownList();
    }

    public LiveData<List<ECountryInfo>> getCountryList(){
        return poAddress.GetCountryList();
    }

    public ArrayList<String> getBarangayForInput(List<EBarangayInfo> foList) {
        return poAddress.getBarangayForInput(foList);
    }

    public ArrayList<String> getTownCityForInput(List<DAddress.oTownObj> foList) {
        return poAddress.getTownCityForInput(foList);
    }

    public ArrayList<String> getCountryForInput(List<ECountryInfo> foList) {
        return poAddress.getCountryForInput(foList);
    }

    public LiveData<List<AccountDetailsInfo>> getAccountDetailsList() {
        return poAcctInf;
    }

    public void importAccountInfo(OnTransactionCallBack foCallBck) {
        new ImportAccountInfoTask(poConnect, poClientx, foCallBck).execute();
    }

    public void completeClientInfo(OnTransactionCallBack foCallBck) {
        new CompleteClientInfoTask(poConnect, poClientx, foCallBck).execute();
    }

    private void setAccountDetailsList() {
        List<AccountDetailsInfo> loAcctInf = new ArrayList<>();
        loAcctInf.add(new AccountDetailsInfo(true, psLstHead[0], "",""));
        loAcctInf.add(new AccountDetailsInfo(false,"","Full Name", ""));
        loAcctInf.add(new AccountDetailsInfo(false, "", "Gender", ""));
        loAcctInf.add(new AccountDetailsInfo(false, "", "Birth Date", ""));
        loAcctInf.add(new AccountDetailsInfo(false, "", "Birth Place", ""));
        loAcctInf.add(new AccountDetailsInfo(false, "", "Citizen", ""));
        loAcctInf.add(new AccountDetailsInfo(false, "", "Civil Status", ""));
        loAcctInf.add(new AccountDetailsInfo(false, "", "Tax ID", ""));

        loAcctInf.add(new AccountDetailsInfo(true, psLstHead[1], "",""));
        loAcctInf.add(new AccountDetailsInfo(false,"","Address", ""));

        loAcctInf.add(new AccountDetailsInfo(true, psLstHead[2], "",""));
        loAcctInf.add(new AccountDetailsInfo(false,"","Email Address", ""));
        loAcctInf.add(new AccountDetailsInfo(false,"","Mobile Number", ""));
        loAcctInf.add(new AccountDetailsInfo(false,"","Password", ""));

        poAcctInf.setValue(loAcctInf);
    }

    public String[] getListHeaders() {
        return psLstHead;
    }

    private static class ImportAccountInfoTask extends AsyncTask<Void, Void, String> {

        private final ConnectionUtil loConnect;
        private final RClientInfo loClientx;
        private final OnTransactionCallBack loCallBck;
        private boolean isSuccess = false;

        private ImportAccountInfoTask(ConnectionUtil foConnect, RClientInfo foClientx, OnTransactionCallBack foCallBck) {
            this.loConnect = foConnect;
            this.loClientx = foClientx;
            this.loCallBck = foCallBck;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loCallBck.onLoading();
        }

        @Override
        protected String doInBackground(Void... voids) {
            String lsResultx = "";

            try {
                if(loConnect.isDeviceConnected()) {
                    if(loClientx.ImportAccountInfo()) {
                        lsResultx = "";
                        isSuccess = true;
                    } else {
                        lsResultx = "";
                    }
                } else {
                    lsResultx = AppConstants.SERVER_NO_RESPONSE();
                }
            } catch (Exception e) {
                e.printStackTrace();
                lsResultx = e.getMessage();
            }

            return lsResultx;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(isSuccess) {
                loCallBck.onSuccess(s);
            } else {
                loCallBck.onFailed(s);
            }
        }

    }

    private static class CompleteClientInfoTask extends AsyncTask<EClientInfo, Void, String> {

        private final ConnectionUtil loConnect;
        private final RClientInfo loClientx;
        private final OnTransactionCallBack loCallBck;
        private boolean isSuccess = false;

        private CompleteClientInfoTask(ConnectionUtil foConnect, RClientInfo foClientx, OnTransactionCallBack foCallBck) {
            this.loConnect = foConnect;
            this.loClientx = foClientx;
            this.loCallBck = foCallBck;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loCallBck.onLoading();
        }

        @Override
        protected String doInBackground(EClientInfo... eClientInfos) {
            String lsResultx = "";
            EClientInfo loInfo = eClientInfos[0];

            try {
                if(loConnect.isDeviceConnected()) {
                    if(loClientx.CompleteClientInfo(loInfo)) {
                        lsResultx = "Client info completion success";
                        isSuccess = true;
                    } else {
                        lsResultx = "Client info completion failed.";
                    }
                } else {
                    lsResultx = AppConstants.SERVER_NO_RESPONSE();
                }
            } catch (Exception e) {
                e.printStackTrace();
                lsResultx = e.getMessage();
            }

            return lsResultx;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(isSuccess) {
                loCallBck.onSuccess(s);
            } else {
                loCallBck.onFailed(s);
            }
        }
    }

    public interface OnTransactionCallBack {
        void onLoading();
        void onSuccess(String fsMessage);
        void onFailed(String fsMessage);
    }


}