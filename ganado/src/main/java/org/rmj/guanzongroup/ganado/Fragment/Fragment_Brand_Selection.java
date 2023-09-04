package org.rmj.guanzongroup.ganado.Fragment;

import static android.content.Intent.getIntent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageSwitcher;

import com.google.android.material.imageview.ShapeableImageView;

import org.rmj.guanzongroup.ganado.Activity.Activity_Brand_List;
import org.rmj.guanzongroup.ganado.Adapter.ModelGridAdapter;
import org.rmj.guanzongroup.ganado.Adapter.RecyclerViewAdapter_BrandSelection;
import org.rmj.guanzongroup.ganado.R;
import org.rmj.guanzongroup.ganado.ViewModel.VMBrandList;
import org.rmj.guanzongroup.ganado.ViewModel.VMBrandSelection;

public class Fragment_Brand_Selection extends AppCompatActivity {
    private RecyclerView rvBrand;
    private VMBrandSelection mViewModel;
    private RecyclerViewAdapter_BrandSelection rec_brandList;
    private ShapeableImageView brandcatimg;
    private RecyclerView poRVBrand, poRvCateg;
    public static Fragment_Brand_Selection newInstance() {
        return new Fragment_Brand_Selection();
    }

//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
//                             @Nullable Bundle savedInstanceState) {
//        mViewModel = new ViewModelProvider(requireActivity()).get(VMBrandSelection.class);
//        View view = inflater.inflate(R.layout.fragment_brand_selection, container, false);
//        initViews(view);
//        displayData();
//        return view;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_brand_selection);
//        initViews();
        mViewModel = new ViewModelProvider(this).get(VMBrandSelection.class);

        brandcatimg = findViewById(R.id.imagebrandtop);

        mViewModel.getBrandList().observe(Fragment_Brand_Selection.this, brandList -> {
            if (brandList.size() > 0) {
                rec_brandList = new RecyclerViewAdapter_BrandSelection(brandList, new RecyclerViewAdapter_BrandSelection.OnBrandSelectListener() {
                    @Override
                    public void OnSelect(String BrandID, String BrandName) {
                        Intent intent = new Intent(Fragment_Brand_Selection.this, Activity_Brand_List.class);
                        intent.putExtra("lsBrandID", BrandID);
                        intent.putExtra("lsBrandNm", BrandName);
                        startActivity(intent);
                        overridePendingTransition(R.anim.anim_intent_slide_in_right, R.anim.anim_intent_slide_out_left);
                    }
                });

                rvBrand.setAdapter(rec_brandList);
                rvBrand.setLayoutManager(new GridLayoutManager(Fragment_Brand_Selection.this, 2, RecyclerView.VERTICAL, false));

                rec_brandList.notifyDataSetChanged();
            }
        });
    }

}