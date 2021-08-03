/*
 * Created by Android Team MIS-SEG Year 2021
 * Copyright (c) 2021. Guanzon Central Office
 * Guanzon Bldg., Perez Blvd., Dagupan City, Pangasinan 2400
 * Project name : GhostRider_Android
 * Module : GhostRider_Android.g3appdriver
 * Electronic Personnel Access Control Security System
 * project file created : 4/24/21 3:19 PM
 * project file last modified : 4/24/21 3:17 PM
 */

package org.rmj.g3appdriver.etc;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import org.rmj.g3appdriver.R;

public class GAppMessageBox {
    private AlertDialog poDialogx;
    private MaterialButton btnPositive;
    private MaterialButton btnNegative;
    private TextView lblTitle;
    private TextView lblMsgxx;
    private View midBorder;

    private final Context context;

    public GAppMessageBox(Context context){
        this.context = context;
    }

    private static boolean isDialogShown;

    public void initDialog(){
        AlertDialog.Builder poBuilder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_gapp_message_box, null);
        poBuilder.setCancelable(false)
                .setView(view);
        poDialogx = poBuilder.create();
        poDialogx.setCancelable(false);

        lblTitle = view.findViewById(R.id.lbl_dialogTitle);
        lblMsgxx = view.findViewById(R.id.lbl_dialogMessage);
        midBorder = view.findViewById(R.id.view_midBorder);
        btnPositive = view.findViewById(R.id.btn_dialogPositive);

        btnNegative = view.findViewById(R.id.btn_dialogNegative);
        midBorder.setVisibility(View.GONE);
    }

    public void setMessage(String psMessage) {
        lblMsgxx.setText(psMessage);
    }

    public void setTitle(String psTitlexx) {
        lblTitle.setText(psTitlexx);
    }

    public void setPositiveButton(String psBtnPost, final DialogButton listener) {
        btnPositive.setVisibility(View.VISIBLE);
        btnPositive.setText(psBtnPost);
        btnPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.OnButtonClick(view, poDialogx);
                isDialogShown = false;
            }
        });
    }

    public void setNegativeButton(String psBtnNegt, final DialogButton listener) {
        midBorder.setVisibility(View.VISIBLE);
        btnNegative.setVisibility(View.VISIBLE);
        btnNegative.setText(psBtnNegt);
        btnNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.OnButtonClick(view, poDialogx);
                isDialogShown = false;
            }
        });
    }

    public void show() {
        if(isDialogShown) {
            return;
        }

        if(!poDialogx.isShowing()) {
            poDialogx.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            poDialogx.getWindow().getAttributes().windowAnimations = R.style.PopupAnimation;
            poDialogx.show();
            isDialogShown = true;
        }
    }

    public interface DialogButton{
        void OnButtonClick(View view, AlertDialog dialog);
    }
}

