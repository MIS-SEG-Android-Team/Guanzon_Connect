package org.rmj.guanzongroup.guanzonapp.Fragment;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import org.rmj.guanzongroup.guanzonapp.R;
import org.rmj.guanzongroup.guanzonapp.ViewModel.VMMoblieBranch;

public class Fragment_Moblie extends Fragment {

    private VMMoblieBranch mViewModel;
    private View view;
    private SearchView searchView;
    private ImageButton btn_filter;
    private TextView txt_filter;
    private LinearLayout ln_Loading;
    private RecyclerView recyclerView;

    public static Fragment_Moblie newInstance() {
        return new Fragment_Moblie();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(VMMoblieBranch.class);
        view = inflater.inflate(R.layout.fragment_moblie, container, false);
        initViews();

        return view;
    }

    private void initViews() {

        searchView = view.findViewById(R.id.txtSearch);
        btn_filter = view.findViewById(R.id.btn_Filter);
        txt_filter = view.findViewById(R.id.textView);
        recyclerView = view.findViewById(R.id.recyclerview);
        ln_Loading = view.findViewById(R.id.lnLoading);

    }
}