package com.android.chargemaster.mainactivity.adapters;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.android.chargemaster.mainactivity.fragments.ChargerFragment;
import com.android.chargemaster.mainactivity.fragments.CleanFragment;
import com.android.chargemaster.mainactivity.fragments.OtherFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private final int PAGE_NUMBER = 3;

    public ChargerFragment chargerFragment;
    public CleanFragment cleanFragment;
    public OtherFragment otherFragment;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                chargerFragment = new ChargerFragment();
                return chargerFragment;
            case 1:
                cleanFragment = new CleanFragment();
                return cleanFragment;
            case 2:
                otherFragment = new OtherFragment();
                return otherFragment;
            default:
                chargerFragment = new ChargerFragment();
                return chargerFragment;
        }

    }

    @Override
    public int getCount() {
        return PAGE_NUMBER;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return String.valueOf(position);
    }
}
