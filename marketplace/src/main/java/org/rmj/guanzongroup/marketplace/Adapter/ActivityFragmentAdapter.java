package org.rmj.guanzongroup.marketplace.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ActivityFragmentAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> mFragmentList = new ArrayList<>();

    private List<String>  mTitle = new ArrayList<>();
    public ActivityFragmentAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    public String getPageTitle(int position) {
        return mTitle.get(position);
    }
    public void addFragment(Fragment fragment){
        this.mFragmentList.add(fragment);
    }
    public void addTitle(String title){
        this.mTitle.add(title);
    }
}
