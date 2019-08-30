package com.example.imuhelper.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.imuhelper.fragment.TimetableFragment;

import java.util.List;

public class TimePagerAdapter extends FragmentPagerAdapter {

    List<TimetableFragment> list;

    public TimePagerAdapter(FragmentManager fm, List<TimetableFragment> list) {
        super(fm);
        this.list=list;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.destroyItem(container, position, object);
    }

    @Override
    public int getCount() {
        return list.size();
    }


    //删除碎片功能的必须，虽然我也不知道为什么
    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}
