package org.rmj.guanzongroup.useraccount.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.MenuItem;

import org.json.JSONObject;
import org.rmj.g3appdriver.etc.FragmentAdapter;
import org.rmj.g3appdriver.lib.Account.Obj.UserIdentification;
import org.rmj.g3appdriver.utils.Dialogs.Dialog_Loading;
import org.rmj.g3appdriver.utils.Dialogs.Dialog_SingleButton;
import org.rmj.guanzongroup.useraccount.Fragment.Fragment_ID1;
import org.rmj.guanzongroup.useraccount.Fragment.Fragment_ID2;
import org.rmj.guanzongroup.useraccount.R;
import org.rmj.guanzongroup.useraccount.ViewModel.OnImportIDTypeListener;
import org.rmj.guanzongroup.useraccount.ViewModel.VMIDVerification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Activity_IDVerification extends AppCompatActivity {
    private static final String TAG = Activity_IDVerification.class.getSimpleName();
    private static Activity_IDVerification instance;
    private VMIDVerification mViewModel;

    private ViewPager viewPager;

    private List<UserIdentification> poList = new ArrayList<>();

    private Dialog_SingleButton poDialogx;
    private Dialog_Loading poLoad;

    private String sIDCodexx = "";

    public String getsIDCodexx() {
        return sIDCodexx;
    }

    public void setsIDCodexx(String sIDCodexx) {
        this.sIDCodexx = sIDCodexx;
    }

    public List<UserIdentification> getIDList(){
        return poList;
    }

    public Activity_IDVerification() {

    }

    public static Activity_IDVerification getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        mViewModel = new ViewModelProvider(Activity_IDVerification.this).get(VMIDVerification.class);
        setContentView(R.layout.activity_id_verification);
        poDialogx = new Dialog_SingleButton(Activity_IDVerification.this);
        poLoad = new Dialog_Loading(Activity_IDVerification.this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("ID Verification");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        viewPager = findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(1);
        mViewModel.ImportIDTypes(new OnImportIDTypeListener() {
            @Override
            public void OnImportIDType(String title, String message) {
                poLoad.initDialog(title, message);
                poLoad.show();
            }

            @Override
            public void OnSuccess(List<UserIdentification> args) {
                poLoad.dismiss();
                poList = args;

                viewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(), new Fragment[]{new Fragment_ID1(), new Fragment_ID2()}));
            }

            @Override
            public void OnFailed(String message) {
                poLoad.dismiss();
                poDialogx.setButtonText("Okay");
                poDialogx.initDialog("Loan Application", message, () -> poDialogx.dismiss());
                poDialogx.show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            if(viewPager.getCurrentItem() == 1){
                viewPager.setCurrentItem(0);
            } else {
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void moveToNext(int fnPageNum){
        viewPager.setCurrentItem(fnPageNum);
    }
}