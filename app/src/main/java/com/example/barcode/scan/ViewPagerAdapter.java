package com.example.barcode.scan;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.barcode.create.CreateFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ScanFragment();

            case 1:
                return new CreateFragment();

            case 2:
                return new HistoryFragment();

            default:
                return new ScanFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
