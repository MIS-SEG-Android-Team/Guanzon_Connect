package org.rmj.guanzongroup.panalo.Dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import org.rmj.guanzongroup.panalo.R;

public class DialogPanaloRedeem implements iDialog {
    private static final String TAG = DialogPanaloRedeem.class.getSimpleName();

    private final Context mContext;

    private final AlertDialog.Builder poBuilders;
    private AlertDialog poDialog;

    private TextView txt_GPHeader, txt_GPHeader2;
    private ImageView img_QRCode;
    private MaterialButton button;


    public DialogPanaloRedeem(Context mContext) {
        this.mContext = mContext;
        this.poBuilders = new AlertDialog.Builder(mContext);

    }

    @Override
    public void initDialog(Object foVal, PanaloDialogClickListener listener) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_panalo_redeem, null, false);
        poBuilders.setView(view).setCancelable(false);
        poDialog = poBuilders.create();

        txt_GPHeader = view.findViewById(R.id.lbl_dialogTitle);
        txt_GPHeader2 = view.findViewById(R.id.lbl_dialogHeader2);

        img_QRCode = view.findViewById(R.id.img_QRCode);

        button = view.findViewById(R.id.btn_dialogClose);

        button.setOnClickListener(v -> listener.OnClose(poDialog));
    }

    @Override
    public void show() {

        poDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        poDialog.getWindow().getAttributes().windowAnimations = org.rmj.g3appdriver.R.style.PopupAnimation;
        poDialog.show();

    }
}
