package com.example.imuhelper.utils;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;

import com.asunalxh.wheelview.WheelView;
import com.example.timetable.R;

public class DateSelector {

    private WheelView[] views=new WheelView[2];
    private final static String[] week ={"自动","第1周","第2周","第3周","第4周","第5周","第6周","第7周","第8周","第9周","第10周","第11周","第12周","第13周","第14周","第15周","第16周","第17周","第18周","第19周","第20周"};
    private View view;

    public DateSelector(Context context){

        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view=inflater.inflate(R.layout.item_dateselector,null);

        views[0]=view.findViewById(R.id.dateSelector_startWeek);
        views[1]=view.findViewById(R.id.dateSelector_endWeek);
        for(int i=0;i<2;i++){
            views[i].setList(week);
            views[i].setSelectBackgroundColor(Color.BLUE);
            views[i].setTextSize(60);
        }

        views[0].setOnSelectChangeListener(new WheelView.onSelectChangeListener() {
            @Override
            public void onChange(int index) {
                views[1].setMinSelect(index);
                if(index==0){
                    views[1].setStaticSelect(true);
                    views[1].setSelect(0);
                }else {
                    views[1].setStaticSelect(false);
                }
            }
        });
    }

    public View getView(){
        return view;
    }

    public String getStartWeekString(){
        return views[0].getSelectString();
    }

    public String getEndWeekString(){
        return views[1].getSelectString();
    }

    public int getStartWeek(){return views[0].getSelectIndex();}

    public int getEndWeek(){return views[1].getSelectIndex();}

    public void setSelect(int startWeek,int endWeek){
        views[0].setSelect(startWeek);
        views[1].setSelect(endWeek);
        views[1].setMinSelect(startWeek);
    }
}
