package com.example.imuhelper.utils;

import com.example.imuhelper.bean.DateBean;

import java.util.Calendar;

public class CalendarHelper {

    private Calendar calendar;

    public CalendarHelper(){
        calendar=Calendar.getInstance();
    }

    public CalendarHelper(int year,int month,int day){
        calendar=Calendar.getInstance();
        setDate(year,month,day);
    }

    public void setDate(int year,int month,int day){
        calendar.set(year,month-1,day);
    }

    public void setFirstDay_of_Week(int week){
        switch (week){
            case 1:
                calendar.setFirstDayOfWeek(Calendar.MONDAY);
            case 2:
                calendar.setFirstDayOfWeek(Calendar.TUESDAY);
            case 3:
                calendar.setFirstDayOfWeek(Calendar.WEDNESDAY);
            case 4:
                calendar.setFirstDayOfWeek(Calendar.THURSDAY);
            case 5:
                calendar.setFirstDayOfWeek(Calendar.FRIDAY);
            case 6:
                calendar.setFirstDayOfWeek(Calendar.SATURDAY);
            case 7:
                calendar.setFirstDayOfWeek(Calendar.SUNDAY);
                default:
                    break;
        }
    }

    public int getYear(){
        return calendar.get(Calendar.YEAR);
    }

    public int getMonth(){
        return calendar.get(Calendar.MONTH)+1;
    }

    public int getDay(){
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public int getDay_of_Week(){
        switch (calendar.get(Calendar.DAY_OF_WEEK)){
            case Calendar.MONDAY:
                return 1;
            case Calendar.TUESDAY:
                return 2;
            case Calendar.WEDNESDAY:
                return 3;
            case Calendar.THURSDAY:
                return 4;
            case Calendar.FRIDAY:
                return 5;
            case Calendar.SATURDAY:
                return 6;
            case Calendar.SUNDAY:
                return 7;
                default:
                    return 0;
        }
    }

    public DateBean getDate(){
        return new DateBean(getYear(),getMonth(),getDay(),getDay_of_Week());
    }
}
