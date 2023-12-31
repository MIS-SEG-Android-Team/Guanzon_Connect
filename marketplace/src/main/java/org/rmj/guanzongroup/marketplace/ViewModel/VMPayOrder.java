package org.rmj.guanzongroup.marketplace.ViewModel;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.rmj.g3appdriver.dev.Database.DataAccessObject.DOrderMaster;
import org.rmj.g3appdriver.dev.Database.Entities.EOrderMaster;
import org.rmj.g3appdriver.dev.Repositories.ROrder;
import org.rmj.g3appdriver.etc.ConnectionUtil;
import org.rmj.g3appdriver.etc.PaymentMethod;
import org.rmj.guanzongroup.marketplace.Etc.OnTransactionsCallback;

public class VMPayOrder extends AndroidViewModel {

    private final Application application;
    private final ROrder poOrder;

    private final MutableLiveData<PaymentMethod> poPayMeth = new MutableLiveData<>();

    public VMPayOrder(@NonNull Application application) {
        super(application);
        this.application = application;
        this.poOrder = new ROrder(application);
    }

    public LiveData<DOrderMaster.DetailedOrderHistory> getOrderDetail(String args){
        return poOrder.GetDetailOrderHistory(args);
    }

    public LiveData<Double> GetSelectedItemCartTotalPrice(){
        return poOrder.GetSelectedItemCartTotalPrice();
    }

    public LiveData<EOrderMaster> GetOrderMaster(String args){
        return poOrder.GetOrderMasterInfo(args);
    }

    public LiveData<String> GetOrderAmount(String args){
        return poOrder.GetOrderAmount(args);
    }

    public void setPaymentMethod(PaymentMethod foPayMeth) {
        this.poPayMeth.setValue(foPayMeth);
    }

    public LiveData<PaymentMethod> getPaymentMethod() {
        return poPayMeth;
    }

    public void payOrder(String fsTransNo, PaymentMethod foPayMeth, String fsReferNo,
                         OnTransactionsCallback foCallBck) {
        new PayOrderTask(application, foPayMeth, fsReferNo, foCallBck).execute(fsTransNo);
    }

    private static class PayOrderTask extends AsyncTask<String, Void, Boolean> {

        private final ConnectionUtil loConnect;
        private final ROrder loItmCart;
        private final OnTransactionsCallback loCallBck;
        private final PaymentMethod loPayMeth;
        private final String lsReferNo;
        private String lsMessage = "";

        private PayOrderTask(Application application, PaymentMethod foPayMeth, String fsReferNo,
                             OnTransactionsCallback foCallBck) {
            this.loConnect = new ConnectionUtil(application);
            this.loItmCart = new ROrder(application);
            this.loPayMeth = foPayMeth;
            this.lsReferNo = fsReferNo;
            this.loCallBck = foCallBck;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loCallBck.onLoading();
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            try {
                String lsTransNo = strings[0];
                if(loConnect.isDeviceConnected()) {
                    boolean isSuccess = loItmCart.PayOrder(lsTransNo, loPayMeth, lsReferNo);
                    lsMessage = (isSuccess) ? "Order Completed. \nThank you for your purchase." : loItmCart.getMessage();
                    return isSuccess;
                } else {
                    lsMessage = "Unable to connect.";
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                lsMessage = e.getMessage();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(aBoolean) {
                loCallBck.onSuccess(lsMessage);
            } else {
                loCallBck.onFailed(lsMessage);
            }
        }

    }

    public void CheckPaymentMethods(OnTransactionsCallback callback){
        new CheckPaymentMethodTask(application, callback).execute();
    }

    private static class CheckPaymentMethodTask extends AsyncTask<String, Void, Boolean>{

        private final OnTransactionsCallback callback;
        private final ROrder poOrder;

        private String message;

        public CheckPaymentMethodTask(Context context, OnTransactionsCallback callback) {
            this.poOrder = new ROrder(context);
            this.callback = callback;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            callback.onLoading();
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            try{
                if(poOrder.DownloadBankAccounts()){
                    message = poOrder.getData().toString();
                    return true;
                } else {
                    message = poOrder.getMessage();
                    return false;
                }
            } catch (Exception e){
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(aBoolean){
                callback.onSuccess(message);
            } else {
                callback.onFailed(message);
            }
        }
    }
}
