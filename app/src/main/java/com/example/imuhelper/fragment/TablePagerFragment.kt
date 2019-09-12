package com.example.imuhelper.fragment

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toolbar
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.imuhelper.R
import com.example.imuhelper.adapter.TablePagerAdapter
import com.example.imuhelper.bean.DateBean
import com.example.imuhelper.db.TermDBHelper
import com.example.imuhelper.utils.CalendarHelper
import com.example.imuhelper.utils.TermTool
import kotlin.collections.ArrayList

class TablePagerFragment : Fragment() {

    private lateinit var adapter: TablePagerAdapter
    private lateinit var viewPager: ViewPager
    private var handler = Handler()
    private var timeTableList = ArrayList<TimeTableFragment>()

    var onPageChangeFunction:OnPageChangeFunction ?= null


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_timepager, null)
        init(view)
        return view
    }


    fun loadTimeTable() {
        val termLength = TermTool.getTermLength()
        handler.post {
            for (i in 1..termLength) {
                val fragment = TimeTableFragment(i)
                timeTableList.add(fragment)
            }
            adapter.notifyDataSetChanged()
            reLocation()
        }
    }


    private fun init(view: View) {
        viewPager = view.findViewById(R.id.timePager_viewPager)
        adapter = TablePagerAdapter(fragmentManager!!, timeTableList)
        viewPager.adapter = adapter
        viewPager.addOnPageChangeListener(object :ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                onPageChangeFunction?.onPageChange(position)
            }
        })
        onPageChangeFunction?.onPageChange(0)

    }

    fun reLocation() {
        var calendarHelper = CalendarHelper()
        var day = DateBean(calendarHelper.year, calendarHelper.month, calendarHelper.day)
        var term = TermDBHelper(context, null).getTerm(TermTool.getSelectedId())
        val position = when (day.compareTo(term)) {
            -1 -> 0
            else -> if (term.getWeekNum(day) <= TermTool.getTermLength()) term.getWeekNum(day) else TermTool.getTermLength()
        }
        viewPager.currentItem = position
    }

    fun fresh() {
        handler.post {
            timeTableList.clear()
            adapter.notifyDataSetChanged()
            loadTimeTable()
        }
    }

    interface OnPageChangeFunction{
        fun onPageChange(position:Int)
    }
}