package com.example.imuhelper.utils;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;

import com.asunalxh.wheelview.WheelView;
import com.example.imuhelper.R;

import java.util.ArrayList;
import java.util.List;

public class TimeSelector {
    private View view;
    private WheelView[] wheelView=new WheelView[3];

    private final static String[] leftList={"周一","周二","周三","周四","周五","周六","周日"};
    private int maxCourseNumber=20;
    public TimeSelector(Context context){
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view=inflater.inflate(R.layout.item_threeselector,null);

        wheelView[0]=view.findViewById(R.id.threeSelector_First);
        wheelView[1]=view.findViewById(R.id.threeSelector_Second);
        wheelView[2]=view.findViewById(R.id.threeSelector_Third);

        for(int i=0;i<3;i++){
            wheelView[i].setSelectBackgroundColor(Color.BLUE);
            wheelView[i].setTextSize(60);
            wheelView[i].setTextColor(0Xff333333);
            wheelView[i].setSelectBackgroundColor(0xFF95f0e5);
        }

        wheelView[0].setList(leftList);
        List<String> list=new ArrayList<>();
        for(int i=1;i<=maxCourseNumber;i++)
            list.add(String.valueOf(i));

        wheelView[1].setList(list);
        wheelView[2].setList(list);


        wheelView[1].setOnSelectChangeListener(new WheelView.onSelectChangeListener() {
            @Override
            public void onChange(int index) {
                wheelView[2].setMinSelect(index);
            }
        });
    }

    public void setSelect(int dayOfWeek,int startCourse,int endCourse){
        wheelView[0].setSelect(dayOfWeek-1);
        wheelView[1].setSelect(startCourse-1);
        wheelView[2].setSelect(endCourse-1);
        wheelView[2].setMinSelect(startCourse-1);
    }

    public int getDayOfWeek(){
        return wheelView[0].getSelectIndex()+1;
    }

    public int getStartCourse(){
        return Integer.valueOf(wheelView[1].getSelectString());
    }

    public int getEndCourse(){
        return Integer.valueOf(wheelView[2].getSelectString());
    }

    public View getView() {
        return view;
    }
}
