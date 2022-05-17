package org.rmj.g3appdriver.dev.Database.Entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(tableName = "MarketPlace_Cart", primaryKeys = {"sUserIDxx", "sListIDxx", "cBuyNowxx"})
public class EItemCart {
    @NonNull
    @ColumnInfo(name = "sUserIDxx")
    private String UserIDxx;
    @NonNull
    @ColumnInfo(name = "sListIDxx")
    private String ListIDxx;
    @NonNull
    @ColumnInfo(name = "cBuyNowxx")
    private String BuyNowxx = "0";

    @ColumnInfo(name = "nQuantity")
    private String Quantity;
    @ColumnInfo(name = "nAvlQtyxx")
    private String AvlQtyxx;
    @ColumnInfo(name = "dCreatedx")
    private String Createdx;
    @ColumnInfo(name = "cTranStat")
    private String TranStat = "0";
    @ColumnInfo(name = "dTimeStmp")
    private String TimeStmp;

    public EItemCart() {
    }

    @NonNull
    public String getUserIDxx() {
        return UserIDxx;
    }

    public void setUserIDxx(@NonNull String userIDxx) {
        UserIDxx = userIDxx;
    }

    @NonNull
    public String getListIDxx() {
        return ListIDxx;
    }

    public void setListIDxx(@NonNull String listIDxx) {
        ListIDxx = listIDxx;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getAvlQtyxx() {
        return AvlQtyxx;
    }

    public void setAvlQtyxx(String avlQtyxx) {
        AvlQtyxx = avlQtyxx;
    }

    public String getCreatedx() {
        return Createdx;
    }

    public void setCreatedx(String createdx) {
        Createdx = createdx;
    }

    public String getTranStat() {
        return TranStat;
    }

    public void setTranStat(String tranStat) {
        TranStat = tranStat;
    }

    public String getTimeStmp() {
        return TimeStmp;
    }

    public void setTimeStmp(String timeStmp) {
        TimeStmp = timeStmp;
    }

    public String getBuyNowxx() {
        return BuyNowxx;
    }

    public void setBuyNowxx(String buyNowxx) {
        BuyNowxx = buyNowxx;
    }
}
