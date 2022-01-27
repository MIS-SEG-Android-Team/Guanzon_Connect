package org.rmj.guanzongroup.guanzonapp.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.rmj.guanzongroup.appcore.Etc.AppConstants;
import org.rmj.guanzongroup.digitalgcard.ViewModel.VMAccountSettings;
import org.rmj.guanzongroup.guanzonapp.Adapter.Adapter_AccountSettings;
import org.rmj.guanzongroup.guanzonapp.R;

import java.util.ArrayList;

public class Fragment_AccountSettings extends Fragment {

    private VMAccountSettings mViewModel;
    private Adapter_AccountSettings poAdapter;
    private RecyclerView recyclerView;

    public static Fragment_AccountSettings newInstance() {
        return new Fragment_AccountSettings();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_account_settings, container, false);
        setUpViews(v);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(VMAccountSettings.class);
        // TODO: Use the ViewModel

        poAdapter = new Adapter_AccountSettings(getMenuList());
        recyclerView.setAdapter(poAdapter);
        poAdapter.notifyDataSetChanged();
    }

    private void setUpViews(View v) {
        recyclerView = v.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
    }

    private ArrayList<String[]> getMenuList() {
        ArrayList<String[]> loMenuLst = new ArrayList<>();
        for(int i = 0; i < AppConstants.ACCOUNT_SETTINGS_MENU.length; i++) {
            loMenuLst.add(AppConstants.ACCOUNT_SETTINGS_MENU[i]);
        }
        return loMenuLst;
    }

}