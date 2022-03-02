package org.rmj.g3appdriver.dev.Database.DataAccessObject;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RoomWarnings;
import androidx.room.Update;

import org.rmj.g3appdriver.dev.Database.Entities.ERedeemItemInfo;
import org.rmj.g3appdriver.dev.Database.Entities.ERedeemablesInfo;

import java.util.List;

@Dao
public interface DRedeemablesInfo {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ERedeemablesInfo redeemablesInfo);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertBulkData(List<ERedeemablesInfo> redeemablesInfoList);

    @Update
    void update(ERedeemablesInfo redeemablesInfo);

    @Query("SELECT COUNT(sTransNox) FROM Redeemables")
    LiveData<Integer> countRedeemables();

    @Query("SELECT COUNT(sPromoIDx) FROM Redeem_Item WHERE sGcardNox =:GCardNox AND cTranStat != 0 GROUP BY sReferNox")
    LiveData<Integer> getOrdersCount(String GCardNox);


    @Query("SELECT * FROM Redeem_Item WHERE sGcardNox =:GCardNox AND cTranStat != 0 GROUP BY sReferNox")
    LiveData<List<ERedeemItemInfo>> getOrdersList(String GCardNox);

    @Query("SELECT * FROM Redeemables")
    LiveData<List<ERedeemablesInfo>> getRedeemablesList();

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT c.sReferNox, c.dOrderedx, b.sBranchNm, b.sAddressx, SUM(c.nPointsxx) as TotAmnt " +
            "FROM Redeem_Item c " +
            "LEFT JOIN BranchInfo b ON c.sBranchCd = b.sBranchCd " +
            "LEFT JOIN Redeemables a ON a.sTransNox = c.sPromoIDx " +
            "WHERE c.sGCardNox =:GCardNox " +
            "AND c.cPlcOrder = '1' " +
            "AND c.cTranStat = '1' " +
            "GROUP BY c.sReferNox")
    LiveData<List<TransactionOrder>> getTransactionOrderList(String GCardNox);


    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT " +
            "a.sPromoIDx as itemID, " +
            "b.sPromoDsc as itemName, " +
            "a.nItemQtyx as itemQtyx, " +
            "a.nPointsxx as itemPnts " +
            "FROM Redeem_Item a " +
            "LEFT JOIN Redeemables b " +
            "ON a.sPromoIDx = b.sTransNox " +
            "WHERE a.cTranStat = '1' " +
            "AND a.cPlcOrder = '1' " +
            "AND a.sGCardNox =:GCardNox " +
            "AND a.sReferNox =:ReferNox ")
    LiveData<List<OrderItems>> getOrderItems(String ReferNox, String GCardNox);

    class OrderItems {
        public String itemID;
        public String itemName;
        public String itemQtyx;
        public String itemPnts;
    }
    class TransactionOrder {
        public String TransNox;
        public String TotAmnt;
        public String Branchx;
        public String Address;
        public String dOrderx;

    }
}
