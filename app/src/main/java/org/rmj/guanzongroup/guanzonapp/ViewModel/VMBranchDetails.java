package org.rmj.guanzongroup.guanzonapp.ViewModel;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.rmj.g3appdriver.GConnect.room.DataAccessObject.DAddress;
import org.rmj.g3appdriver.GConnect.room.Entities.EBranchInfo;

import java.util.List;

public class VMBranchDetails extends AndroidViewModel {
//    private final RBranchInfo poBranchx;
//    private final RAddressMobile poAddress;
    private final Application instance;

    public VMBranchDetails(@NonNull Application application) {
        super(application);
        this.instance = application;
//        this.poBranchx = new RBranchInfo(application);
//        this.poAddress = new RAddressMobile(application);
    }

    public void DownloadBranches(){
        new DownloadBranchesTask().execute();
    }

    private class DownloadBranchesTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
//            try {
//            iGCardSystem loGcard = new GCardSystem(instance).getInstance(GCardSystem.CoreFunctions.EXTRAS);
//                loGcard.DownloadBranchesList(new GCardSystem.GCardSystemCallback() {
//                    @Override
//                    public void OnSuccess(String args) {
//
//                    }
//
//                    @Override
//                    public void OnFailed(String message) {
//
//                    }
//                });
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
            return null;
        }
    }

//    public LiveData<List<EBranchInfo>> getMotorBranches() {
//        return poBranchx.getMotorBranches();
//    }
//
//    public LiveData<List<EBranchInfo>> getMobileBranches() {
//        return poBranchx.getMobileBranches();
//    }
//
//    public LiveData<List<DAddress.oTownObj>> GetTownList(){
//        return poAddress.GetTownList();
//    }
}
