package com.example.imuhelper.adapter

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.example.imuhelper.fragment.TimeTableFragment
import java.security.Policy

class TablePagerAdapter(fm:FragmentManager,list:ArrayList<TimeTableFragment>) : FragmentPagerAdapter(fm){

    private var list:ArrayList<TimeTableFragment> = list



    override fun getItem(position: Int): Fragment {
        return list[position]
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        super.destroyItem(container, position, `object`)
    }


}