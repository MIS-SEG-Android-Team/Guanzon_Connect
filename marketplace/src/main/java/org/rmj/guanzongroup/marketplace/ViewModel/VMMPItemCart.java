package org.rmj.guanzongroup.marketplace.ViewModel;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.rmj.g3appdriver.dev.Database.DataAccessObject.DItemCart;
import org.rmj.g3appdriver.dev.Database.DataAccessObject.DProduct;
import org.rmj.g3appdriver.dev.Repositories.RClientInfo;
import org.rmj.g3appdriver.dev.Repositories.ROrder;
import org.rmj.g3appdriver.etc.ConnectionUtil;
import org.rmj.g3appdriver.lib.GCardCore.GCardSystem;
import org.rmj.g3appdriver.lib.GCardCore.iGCardSystem;
import org.rmj.g3appdriver.dev.Repositories.RProduct;
import org.rmj.g3appdriver.etc.FilterType;
import org.rmj.guanzongroup.marketplace.Etc.AddUpdateCartTask;
import org.rmj.guanzongroup.marketplace.Etc.OnTransactionsCallback;
import org.rmj.guanzongroup.marketplace.Model.ItemCartModel;

import java.util.ArrayList;
import java.util.List;

public class VMMPItemCart extends AndroidViewModel {
    private static final String TAG = VMMPItemCart.class.getSimpleName();

    private final MutableLiveData<List<ItemCartModel>> poItemCart = new MutableLiveData<>();
    private final Application application;
    private final RClientInfo poClientx;
    private final iGCardSystem poGCard;
    private final ROrder poOrder;
    private final RProduct poProdct;

    public VMMPItemCart(@NonNull Application application) {
        super(application);
        this.application = application;
        this.poClientx = new RClientInfo(application);
        this.poGCard = new GCardSystem(application).getInstance(GCardSystem.CoreFunctions.REDEMPTION);
        this.poOrder = new ROrder(application);
        this.poProdct = new RProduct(application);
//        generateData();
    }


    public LiveData<List<DProduct.oProduct>> getProductList(int fnIndex) {
        return poProdct.GetProductsList(fnIndex, FilterType.DEFAULT, null, null);
    }


    public LiveData<List<DItemCart.oMarketplaceCartItem>> GetCartItemsList(){
        return poOrder.GetItemCartList();
    }

    public LiveData<Double> GetSelectedItemTotalPrice(){
        return poOrder.GetSelectedItemCartTotalPrice();
    }

    public LiveData<Integer> GetSelectedItemCartTotalCount(){
        return poOrder.GetSelectedItemCartTotalCount();
    }

    public LiveData<Integer> GetMpItemCartCount(){
        return poOrder.GetMartketplaceCartItemCount();
    }

    public void SelectAllItemOnCart(boolean isSelected){
        new SelectAllTask(poOrder).execute(isSelected);
    }

    public void DeleteAllSelected(OnTransactionsCallback foCallBck){
        new DeleteSelectedTask(foCallBck).execute();
    }

    public LiveData<List<ItemCartModel>> getMarketPlaceItemCart(){
        return poItemCart;
    }

    public List<ItemCartModel> ParseDataForAdapter(List<DItemCart.oMarketplaceCartItem> foVal) {
        ArrayList<ItemCartModel> list = new ArrayList<>();
        for(int x = 0; x < foVal.size(); x++){
            ItemCartModel loDetail = new ItemCartModel();
            loDetail.setMarket(true);
            loDetail.setListingId(foVal.get(x).sListIDxx);
            loDetail.setItemName(foVal.get(x).xModelNme);
            loDetail.setItemPrice(foVal.get(x).nUnitPrce);
            loDetail.setItemQty(foVal.get(x).nQuantity);
            loDetail.setItemImage(foVal.get(x).sImagesxx);
            loDetail.setcMktCheck(foVal.get(x).cCheckOut.equalsIgnoreCase("1"));
            list.add(loDetail);
        }
        return list;
    }

    public void addUpdateCart(String fsListId, int fnItemQty, boolean QtyUpdate, OnTransactionsCallback foCallBck) {
        new AddUpdateCartTask(application, fnItemQty, true, foCallBck).execute(fsListId);
    }

    public void forCheckOut(String fsListIdx) {
        new ForCheckoutTask(poOrder).execute(fsListIdx);
    }

    public void removeForCheckOut(String fsListIdx) {
        new RemoveForCheckoutTask(poOrder).execute(fsListIdx);
    }

    public void checkCartItemsForCheckOut(OnTransactionsCallback foCallBck) {
        new CheckCartItemsForCheckOutTask(poOrder, foCallBck).execute();
    }

    private static class SelectAllTask extends AsyncTask<Boolean, Void, Boolean>{

        private final ROrder loItmCart;

        private String message;

        private SelectAllTask(ROrder foItmCart) {
            this.loItmCart = foItmCart;
        }

        @Override
        protected Boolean doInBackground(Boolean... booleans) {
            if(!loItmCart.SelectAll(booleans[0])){
                message = loItmCart.getMessage();
            }
            return null;
        }
    }



    private class DeleteSelectedTask extends AsyncTask<String, Void, Boolean>{

        private final OnTransactionsCallback foCallBck;

        private String message;

        private DeleteSelectedTask(OnTransactionsCallback foCallBck) {
            this.foCallBck = foCallBck;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            foCallBck.onLoading();
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            if(!poOrder.DeleteAll()){
                message = poOrder.getMessage();
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean isSuccess) {
            super.onPostExecute(isSuccess);
            if(!isSuccess){
                foCallBck.onFailed(message);
            } else {
                foCallBck.onSuccess(message);
            }
        }
    }

    private static class ForCheckoutTask extends AsyncTask<String, Void, Void> {

        private final ROrder loItmCart;

        private ForCheckoutTask(ROrder foItmCart) {
            this.loItmCart = foItmCart;
        }

        @Override
        protected Void doInBackground(String... strings) {
            String lsListIdx = strings[0];
            loItmCart.ForCheckOut(lsListIdx);
            return null;
        }
    }

    private static class RemoveForCheckoutTask extends AsyncTask<String, Void, Void> {

        private final ROrder loItmCart;

        private RemoveForCheckoutTask(ROrder foItmCart) {
            this.loItmCart = foItmCart;
        }

        @Override
        protected Void doInBackground(String... strings) {
            String lsListIdx = strings[0];
            loItmCart.RemoveForCheckOut(lsListIdx);
            return null;
        }
    }

    private static class CheckCartItemsForCheckOutTask extends AsyncTask<Void, Void, Boolean> {

        private final ROrder loItmCart;
        private final OnTransactionsCallback loCallBck;
        private String lsMessage = "";

        private CheckCartItemsForCheckOutTask(ROrder foItmCart, OnTransactionsCallback foCallBck) {
            this.loItmCart = foItmCart;
            this.loCallBck = foCallBck;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loCallBck.onLoading();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            if(loItmCart.CheckCartItemsForCheckOut()) {
                return true;
            } else {
                lsMessage = loItmCart.getMessage();
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

    public void RemoveItemOnCart(String fsVal, OnTransactionsCallback callback){
        new RemoveItemTask(application, callback).execute(fsVal);
    }

    private static class RemoveItemTask extends AsyncTask<String, Void, Boolean>{

        private final Context mContext;
        private final OnTransactionsCallback callback;

        private String message;

        public RemoveItemTask(Context mContext, OnTransactionsCallback callback) {
            this.mContext = mContext;
            this.callback = callback;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            callback.onLoading();
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            ROrder loOrder = new ROrder(mContext);
            ConnectionUtil loConn = new ConnectionUtil(mContext);
            String lsListID = strings[0];
            if(loConn.isDeviceConnected()) {
                if (loOrder.RemoveCartItem(lsListID)) {
                    return true;
                } else {
                    message = loOrder.getMessage();
                    return false;
                }
            } else {
                message = "Unable to connect.";
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(aBoolean){
                callback.onSuccess("Item removed!");
            } else {
                callback.onFailed(message);
            }
        }
    }
}