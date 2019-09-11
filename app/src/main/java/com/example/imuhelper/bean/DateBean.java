package com.example.imuhelper.bean;

import com.example.imuhelper.utils.CalendarHelper;

public class DateBean {
    private int year;
    private int month;
    private int day;
    private int day_of_week;
    private static final int[] daynum = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    private static final int[] daynum_leep = {0, 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    public DateBean(){}

    public DateBean(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
        CalendarHelper calendarHelper=new CalendarHelper();
        calendarHelper.setDate(year,month,day);
        this.day_of_week=calendarHelper.getDay_of_Week();
    }

    public DateBean(int year, int month, int day, int day_of_week) {
        this.year = year;
        this.day_of_week = day_of_week;
        this.month = month;
        this.day = day;
    }

    public void setDay_of_week(int day_of_week) {
        this.day_of_week = day_of_week;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public int getDay_of_week() {
        return day_of_week;
    }

    //1大于，-1小于，0等于
    public int compareTo(DateBean x) {
        if (year != x.year) {
            if (year > x.year)
                return 1;
            else return -1;
        } else if (month != x.month) {
            if (month > x.month)
                return 1;
            else return -1;
        } else if (day != x.day) {
            if (day > x.day)
                return 1;
            else return -1;
        } else return 0;
    }

    public int getDayNum(DateBean x) {
        DateBean a, b;
        if (this.compareTo(x) <= 0) {
            a = this;
            b = x;
        } else {
            b = this;
            a = x;
        }
        int sum = 0;

        if (a.month <= b.month) {
            for (int i = a.year; i < b.year; i++) {
                if (i % 400 == 0 || i % 100 != 0 && i % 4 == 0) {
                    sum += 366;
                } else sum += 365;
            }

            if (b.year % 400 == 0 || b.year % 100 != 0 && b.year % 4 == 0) {
                if (a.day <= b.day) {
                    for (int i = a.month; i < b.month; i++)
                        sum += daynum_leep[i];
                    sum += b.day - a.day;
                } else {
                    for (int i = a.month; i < b.month - 1; i++)
                        sum += daynum_leep[i];
                    sum += daynum_leep[a.month] - a.day + b.day;
                }
            } else {
                if (a.day <= b.day) {
                    for (int i = a.month; i < b.month; i++)
                        sum += daynum[i];
                    sum += b.day - a.day;
                } else {
                    for (int i = a.month; i < b.month - 1; i++)
                        sum += daynum[i];
                    sum += daynum[a.month] - a.day + b.day;
                }
            }

        } else {
            for (int i = a.year; i < b.year - 1; i++) {
                if (i % 400 == 0 || i % 100 != 0 && i % 4 == 0) {
                    sum += 366;
                } else sum += 365;
            }

            if ((b.year - 1) % 400 == 0 || (b.year - 1) % 100 != 0 && (b.year - 1) % 4 == 0) {
                for (int i = a.month; i <= 11; i++)
                    sum += daynum_leep[i];
                sum += daynum_leep[12] - a.day;
            } else {
                for (int i = a.month; i <= 11; i++)
                    sum += daynum_leep[i];
                sum += daynum_leep[12] - a.day;
            }

            if (b.year % 400 == 0 || b.year % 100 != 0 && b.year % 4 == 0) {
                for (int i = 1; i < b.month; i++)
                    sum += daynum[i];
                sum += b.day;
            } else {
                for (int i = 1; i < b.month; i++)
                    sum += daynum_leep[i];
                sum += b.day;
            }

        }

        return sum;
    }

    public int getWeekNum(DateBean x) {
        DateBean a, b;
        if (this.compareTo(x) <= 0) {
            a = this;
            b = x;
        } else {
            b = this;
            a = x;
        }

        int days = a.getDayNum(b);
        int sum = days / 7;
        if (b.day_of_week < a.day_of_week && days % 7 > (7 - a.day_of_week))
            sum++;
        return sum;
    }
}
