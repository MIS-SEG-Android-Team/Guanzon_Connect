package org.rmj.guanzongroup.ghostrider.Fragment;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textview.MaterialTextView;

import org.rmj.g3appdriver.etc.MessageBox;
import org.rmj.g3appdriver.lib.Panalo.model.PanaloRewards;
import org.rmj.guanzongroup.ghostrider.Adapter.AdapterRewardsClaimed;
import org.rmj.guanzongroup.ghostrider.Adapter.AdapterRewardsEarned;
import org.rmj.guanzongroup.ghostrider.Dialog.DialogPanaloRedeem;
import org.rmj.guanzongroup.ghostrider.R;
import org.rmj.guanzongroup.ghostrider.ViewModel.VMPanaloRewards;

import java.util.List;

public class Fragment_Rewards extends Fragment  {
    private VMPanaloRewards mViewModel;
    private MessageBox loMessage;
    private LinearLayout lnEarned, lnClaimed;
    private RecyclerView rvEarned, rvClaimed;
    private MaterialTextView lblNoRewards;
    private CircularProgressIndicator progress;

    public static Fragment_Rewards newInstance() {
        return new Fragment_Rewards();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(requireActivity()).get(VMPanaloRewards.class);
        View view = inflater.inflate(R.layout.fragment_rewards, container, false);
        initWidgets(view);
        retrieveRewards();

        lblNoRewards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lblNoRewards.setVisibility(View.GONE);
                retrieveRewards();
            }
        });
        return view;
    }

    private void initWidgets(View v) {
        lnEarned = v.findViewById(R.id.linearEarned);
        lnClaimed = v.findViewById(R.id.linearClaimed);
        rvEarned = v.findViewById(R.id.rvEarned);
        rvClaimed = v.findViewById(R.id.rvClaimed);
        lblNoRewards = v.findViewById(R.id.lblNoRewards);
        progress = v.findViewById(R.id.progress_circular);
    }

    private void retrieveRewards(){
        mViewModel.GetRewards(0, new VMPanaloRewards.OnRetrieveRewardsListener() {
            @Override
            public void OnLoad(String title, String message) {
                progress.setVisibility(View.VISIBLE);
            }

            @Override
            public void OnSuccess(List<PanaloRewards> earned, List<PanaloRewards> claimed) {
                progress.setVisibility(View.GONE);
                if(earned == null || claimed == null){
                    lblNoRewards.setVisibility(View.VISIBLE);
                    return;
                }

                if(earned.size() == 0 && claimed.size() == 0){
                    lblNoRewards.setVisibility(View.VISIBLE);
                    return;
                }

                if (earned.size() != 0){
                    initRewardsEarned(earned);
                }

                if (claimed.size() != 0){
                    initRewardsClaimed(claimed);
                }
            }

            @Override
            public void OnFailed(String message) {
                progress.setVisibility(View.GONE);
                lblNoRewards.setVisibility(View.VISIBLE);
            }
        });
    }

    private void initRewardsEarned(List<PanaloRewards> args){
        AdapterRewardsEarned loAdapter = new AdapterRewardsEarned(args, new AdapterRewardsEarned.OnClickListener() {
            @Override
            public void OnUseButtonClick(String args) {
                //to display dialog here
//                DialogPanaloRedeem dialogPanaloRedeem = new DialogPanaloRedeem(getActivity(), args);
//                dialogPanaloRedeem.show();
            }
        });
        LinearLayoutManager loManager = new LinearLayoutManager(requireActivity());
        loManager.setOrientation(RecyclerView.VERTICAL);
        rvEarned.setLayoutManager(loManager);
        rvEarned.setAdapter(loAdapter);
        lnEarned.setVisibility(View.VISIBLE);
    }

    private void initRewardsClaimed(List<PanaloRewards> args){
        AdapterRewardsClaimed loAdapter = new AdapterRewardsClaimed(args, new AdapterRewardsClaimed.OnClickListener() {
            @Override
            public void OnClick(PanaloRewards args) {
                //to display dialog here
                DialogPanaloRedeem dialogPanaloRedeem = new DialogPanaloRedeem(getActivity(), args);
                dialogPanaloRedeem.show();
            }
            @Override
            public void OnUseButtonClick(PanaloRewards args) {
                //to display dialog here
                DialogPanaloRedeem dialogPanaloRedeem = new DialogPanaloRedeem(getActivity(), args);
                dialogPanaloRedeem.show();
            }
        });
        LinearLayoutManager loManager = new LinearLayoutManager(requireActivity());
        loManager.setOrientation(RecyclerView.VERTICAL);
        rvClaimed.setLayoutManager(loManager);
        rvClaimed.setAdapter(loAdapter);
        lnClaimed.setVisibility(View.VISIBLE);
    }
}