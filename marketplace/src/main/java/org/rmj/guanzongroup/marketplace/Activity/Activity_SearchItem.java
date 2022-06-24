package org.rmj.guanzongroup.marketplace.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import org.rmj.g3appdriver.dev.Database.DataAccessObject.DProduct;
import org.rmj.guanzongroup.digitalgcard.Adapter.Adapter_Advertisement;
import org.rmj.guanzongroup.digitalgcard.Model.AdvertisementInfo;
import org.rmj.guanzongroup.marketplace.Adapter.Adapter_ProductList;
import org.rmj.guanzongroup.marketplace.ViewModel.VMSearchItem;
import org.rmj.guanzongroup.marketplace.databinding.ActivitySearchItemBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Activity_SearchItem extends AppCompatActivity {
    private static final String TAG = Activity_SearchItem.class.getSimpleName();
    private VMSearchItem mViewModel;
    private ActivitySearchItemBinding mBinding;

    private Adapter_ProductList loAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(Activity_SearchItem.this).get(VMSearchItem.class);
        mBinding = ActivitySearchItemBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
// Get the intent, verify the action and get the query
        setUpToolbar();
        showAds();
        mBinding.searchview.requestFocus();
        mBinding.searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                displayResult(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        mBinding.recyclrVw.setLayoutManager(new GridLayoutManager(Activity_SearchItem.this,
                2, RecyclerView.VERTICAL, false));
        mBinding.recyclrVw.setHasFixedSize(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void setUpToolbar() {
        setSupportActionBar(mBinding.toolBarxx);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Search Item");
    }

    private void showAds() {
        showPromos();
        showSuggestions();
    }

    private void showPromos() {
        mBinding.recyclrVw.setVisibility(View.GONE);
        mBinding.lnAdvetse.setVisibility(View.VISIBLE);
        List<AdvertisementInfo> loList = new ArrayList<>();
        AdvertisementInfo poData = new AdvertisementInfo();
        poData.setPromoId("1");
        poData.setImageUrl("https://res.cloudinary.com/dxxbciuwf/image/fetch/f_auto/https://smack.agency/wp-content/uploads/2020/12/how-to-create-a-marketing-campaign-for-your-business-1-scaled.jpg");
        AdvertisementInfo poData2 = new AdvertisementInfo();
        poData2.setPromoId("2");
        poData2.setImageUrl("https://s2.best-wallpaper.net/wallpaper/2560x1920/1108/Waterfalls-and-streams_2560x1920.jpg");
        loList.add(poData);
        loList.add(poData2);
        Adapter_Advertisement loAdapter = new Adapter_Advertisement(loList, loList.size(), fsId -> {

        });
        mBinding.rvAdvrtse.setAdapter(loAdapter);
        mBinding.rvAdvrtse.setLayoutManager(new LinearLayoutManager(Activity_SearchItem.this));
        loAdapter.notifyDataSetChanged();
    }

    private void showSuggestions() {
        mViewModel.getProductList(0).observe(this, products -> {
            try {
                if(products.size() > 0) {
                    Adapter_ProductList loAdapter = new Adapter_ProductList(products, listingId -> {
                        Intent loIntent = new Intent(this, Activity_ProductOverview.class);
                        loIntent.putExtra("sListingId", listingId);
                        startActivity(loIntent);
                    });
                    mBinding.rvSuggest.setLayoutManager(new GridLayoutManager(this,
                            2, RecyclerView.VERTICAL, false));
                    mBinding.rvSuggest.setHasFixedSize(true);
                    mBinding.rvSuggest.setAdapter(loAdapter);
                    loAdapter.notifyDataSetChanged();
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        });
    }

    private void displayResult(String fsQueryxx) {
        mBinding.lnAdvetse.setVisibility(View.GONE);
        mBinding.recyclrVw.setVisibility(View.VISIBLE);
        mViewModel.RequestProductSearch(fsQueryxx, () -> {
            mViewModel.GetSearchProductList(fsQueryxx).observe(Activity_SearchItem.this, oProducts -> {
                try{
                    if(oProducts.size() > 0) {
                        loAdapter = new Adapter_ProductList(oProducts, fsListIdx -> {
                            Intent loIntent = new Intent(Activity_SearchItem.this, Activity_ProductOverview.class);
                            loIntent.putExtra("sListingId", fsListIdx);
                            startActivity(loIntent);
                        });
                        mBinding.recyclrVw.setAdapter(loAdapter);
                        loAdapter.notifyDataSetChanged();
                    } else {

                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            });
        });
    }
}