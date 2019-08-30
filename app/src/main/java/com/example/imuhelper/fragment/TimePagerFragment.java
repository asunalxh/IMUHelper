package com.example.imuhelper.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.timetable.R;
import com.example.timetable.adapter.TimePagerAdapter;
import com.example.timetable.bean.DateBean;
import com.example.timetable.bean.TermBean;
import com.example.timetable.utils.CalendarHelper;
import com.example.timetable.utils.TermTool;

import java.util.ArrayList;
import java.util.List;


/**
 * 课程表滑动碎片
 */
public class TimePagerFragment extends Fragment {

    private TimePagerAdapter adapter;
    private List<TimetableFragment> list = new ArrayList<>();
    private ViewPager viewPager;
    private int positions;//当前课程表显示位置
    private Handler handler = new Handler();
    private boolean isLoaded = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timepager, container, false);

        init(view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    /**
     * 初始化控件
     *
     * @param view
     */
    private void init(View view) {
        viewPager = view.findViewById(R.id.timePager_viewPager);
        adapter = new TimePagerAdapter(getFragmentManager(), list);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                positions = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    /**
     * 加载课程表
     */
    public void loadTimetable() {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                TermBean selectedTerm = TermTool.getSelectedTerm(getContext());

                if (isLoaded)
                    reFlesh();
                else {
                    for (int i = 1; i <= TermTool.getTermLength(); i++)
                        list.add(new TimetableFragment(i));
                    adapter.notifyDataSetChanged();
                    isLoaded = true;
                }

                CalendarHelper calendarHelper = new CalendarHelper();
                DateBean now = new DateBean(calendarHelper.getYear(), calendarHelper.getMonth(), calendarHelper.getDay(), calendarHelper.getDay_of_Week());

                //确定课程表初始定位
                if (now.compareTo(selectedTerm) < 0)
                    positions = 0;
                else {
                    int weekNumber = now.getWeekNum(selectedTerm);
                    positions = weekNumber <= (list.size() - 1) ? weekNumber : (list.size() - 1);
                }
                viewPager.setCurrentItem(positions);

            }
        };
        handler.post(runnable);
    }


    /**
     * 更新课程表信息
     */
    public void reFlesh() {
        TermTool.reFreshTerm(getContext());



        for (int i = positions - 1; i <= positions + 1; i++)
            if (i >= 0 && i < list.size())
                list.get(i).reFresh();

        //学期变短的情况
        while (list.size() > TermTool.getTermLength()) {
            if (positions > TermTool.getTermLength()) {
                positions = TermTool.getTermLength();
                viewPager.setCurrentItem(positions);
            }
            list.remove(list.size() - 1);
            adapter.notifyDataSetChanged();
        }

        //学期变长的情况
        while (list.size() < TermTool.getTermLength()) {
            list.add(new TimetableFragment(list.size() + 1));
            adapter.notifyDataSetChanged();
        }


    }


}
