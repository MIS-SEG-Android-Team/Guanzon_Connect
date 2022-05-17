package org.rmj.guanzongroup.marketplace.Dialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import org.rmj.g3appdriver.dev.Database.Entities.EBranchInfo;
import org.rmj.g3appdriver.utils.Dialogs.Dialog_DoubleButton;
import org.rmj.guanzongroup.marketplace.Adapter.Adapter_BranchSelection;
import org.rmj.guanzongroup.marketplace.R;

import java.util.List;

public class Dialog_BranchSelection {
    private Context mContext;

    private View view;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;

    private RecyclerView recyclerView;
    private TextInputEditText txtSearch;
    private Adapter_BranchSelection adapter;
    private ImageButton btnClose;
    private Dialog_DoubleButton confimDialog;
    public Dialog_BranchSelection(Context context){
        this.mContext = context;
        this.builder = new AlertDialog.Builder(mContext);
        this.confimDialog = new Dialog_DoubleButton(mContext);

    }

    public void showDialog(){
        dialog.show();
    }
    public void cancelable(boolean val){
        dialog.setCancelable(val);
    }
    @SuppressLint("InflateParams")
    public void createDialog(List<EBranchInfo> branchInfo, onConfirmBranch callback){
        view = LayoutInflater.from(mContext).inflate(R.layout.dialog_branch_selection, null, false);
        builder.setView(view)
                .setCancelable(true);

        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        recyclerView = view.findViewById(R.id.recycler_view_dialog_branch_selection);
        txtSearch = view.findViewById(R.id.tie_dialog_branch_selection_search);
        btnClose = view.findViewById(R.id.btn_branch_selection_close);
        txtSearch.addTextChangedListener(new searchFilterer());

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        adapter = new Adapter_BranchSelection(branchInfo, new Adapter_BranchSelection.onBranchContentClickListener() {
            @Override
            public void onClick(EBranchInfo eBranchInfo) {

                confimDialog.setButtonText("Confirm", "Cancel");
                confimDialog.initDialog("Branch Selection", "Are you sure you want to select " + eBranchInfo.getBranchNm() + "?", new Dialog_DoubleButton.OnDialogConfirmation() {
                    @Override
                    public void onConfirm(AlertDialog dialogS) {
                        callback.onConfirm(eBranchInfo,dialog);
                        dialogS.dismiss();

                    }

                    @Override
                    public void onCancel(AlertDialog dialog) {
                        dialog.dismiss();
                    }
                });
                confimDialog.show();
//                dialog.dismiss();
            }

//            @Override
//            public void onClick(String BranchCode, String BranchName, String BranchAddress) {
//                confimDialog.setButtonText("Confirm", "Cancel");
//                confimDialog.initDialog("Branch Selection", "Are you sure you want to select " + BranchName + "?", new Dialog_DoubleButton.OnDialogConfirmation() {
//                    @Override
//                    public void onConfirm(AlertDialog dialog) {
//                        callback.onConfirm();
//                    }
//
//                    @Override
//                    public void onCancel(AlertDialog dialog) {
//
//                    }
//                });
//                dialog.dismiss();
//            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        adapter.notifyDataSetChanged();
        btnClose.setOnClickListener(v->{
            dialog.dismiss();
        });
    }

    public interface onConfirmBranch {
        void onConfirm(EBranchInfo branchInfo, AlertDialog dialog);
    }

    class searchFilterer implements TextWatcher{

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            adapter.getBranchFilter().filter(s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}
