package com.hongdthaui.babysleep.view.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.hongdthaui.babysleep.R;
import com.hongdthaui.babysleep.view.fragment.NorthFragment;
import com.hongdthaui.babysleep.view.fragment.SouthFragment;
import com.hongdthaui.babysleep.view.fragment.WordlessFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {
    private Context context;
    public PagerAdapter(@NonNull FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new NorthFragment();
                break;
            case 1:
                fragment = new SouthFragment();
                break;
            case 2:
                fragment = new WordlessFragment();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position){
            case 0:
                title = context.getString(R.string.tab_1);
                break;
            case 1:
                title = context.getString(R.string.tab_2);
                break;
            case 2:
                title = context.getString(R.string.tab_3);
                break;
        }
        return title;
    }
}
