package org.rmj.guanzongroup.panalo.Fragment;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.rmj.guanzongroup.panalo.R;
import org.rmj.guanzongroup.panalo.ViewModel.VMPanaloDashboard;

public class Fragment_PanaloDashboard extends Fragment {

    private VMPanaloDashboard mViewModel;

    public static Fragment_PanaloDashboard newInstance() {
        return new Fragment_PanaloDashboard();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_panalo_dashboard, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(VMPanaloDashboard.class);
        // TODO: Use the ViewModel
    }

}