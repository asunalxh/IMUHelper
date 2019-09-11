package com.example.imuhelper.utils;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;

import com.asunalxh.wheelview.WheelView;
import com.example.imuhelper.R;
import com.example.imuhelper.bean.CourseBean;

public class DateSelector {

    private WheelView[] views = new WheelView[3];
    private final static String[] week = {"第1周", "第2周", "第3周", "第4周", "第5周", "第6周", "第7周", "第8周", "第9周", "第10周", "第11周", "第12周", "第13周", "第14周", "第15周", "第16周", "第17周", "第18周", "第19周", "第20周"};
    private final static String[] type = {"全部", "单周", "双周"};
    private View view;

    public DateSelector(Context context) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.item_threeselector, null);

        views[0] = view.findViewById(R.id.threeSelector_First);
        views[1] = view.findViewById(R.id.threeSelector_Second);
        views[2] = view.findViewById(R.id.threeSelector_Third);

        for (int i = 0; i < 2; i++) {
            views[i].setList(week);
            views[i].setSelectBackgroundColor(Color.BLUE);
            views[i].setTextSize(60);
            views[i].setTextColor(0Xff333333);
            views[i].setSelectBackgroundColor(0xFF95f0e5);
        }
        views[2].setList(type);
        views[2].setSelectBackgroundColor(Color.BLUE);
        views[2].setTextSize(60);
        views[2].setTextColor(0Xff333333);
        views[2].setSelectBackgroundColor(0xFF95f0e5);

        views[0].setOnSelectChangeListener(new WheelView.onSelectChangeListener() {
            @Override
            public void onChange(int index) {
                views[1].setMinSelect(index);
            }
        });
    }

    public View getView() {
        return view;
    }

    public String getStartWeekString() {
        return views[0].getSelectString();
    }

    public String getEndWeekString() {
        return views[1].getSelectString();
    }

    public int getType() {
        switch (views[2].getSelectIndex()) {
            case 0:
                return CourseBean.TYPE_ALL;
            case 1:
                return CourseBean.TYPE_SINGLE;
            case 2:
                return CourseBean.TYPE_DOUBLE;
            default:
                return -1;
        }
    }

    public int getStartWeek() {
        return views[0].getSelectIndex() + 1;
    }

    public int getEndWeek() {
        return views[1].getSelectIndex() + 1;
    }

    public void setSelect(int startWeek, int endWeek, int type) {
        views[0].setSelect(startWeek - 1);
        views[1].setSelect(endWeek - 1);
        views[1].setMinSelect(startWeek - 1);
        switch (type) {
            case CourseBean.TYPE_ALL:
                views[2].setSelect(0);
                break;
            case CourseBean.TYPE_SINGLE:
                views[2].setSelect(1);
                break;
            case CourseBean.TYPE_DOUBLE:
                views[2].setSelect(2);
                break;
        }
    }
}
