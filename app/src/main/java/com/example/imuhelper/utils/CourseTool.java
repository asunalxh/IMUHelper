package com.example.imuhelper.utils;

import java.util.ArrayList;
import java.util.List;

public class CourseTool {

    List<CourseTime> list = new ArrayList<>();

    public boolean addAndJust(int week, int dayOfWeek, int course) {
        CourseTime courseTime = new CourseTime(week, dayOfWeek, course);
        for (CourseTime time : list)
            if (time.equals(courseTime))
                return false;
            list.add(courseTime);
        return true;
    }


    private class CourseTime {
        private int week;
        private int dayOfWeek;
        private int course;

        @Override
        public boolean equals(Object obj) {
            CourseTime courseTime= (CourseTime) obj;
            if(week==courseTime.getWeek()&&dayOfWeek==courseTime.getDayOfWeek()&&course==courseTime.getCourse())
                return true;
            else if((week==0||courseTime.getWeek()==0)&&dayOfWeek==courseTime.getDayOfWeek()&&course==courseTime.getCourse()){
                return true;
            }
            return false;
        }

        public CourseTime(int week, int dayOfWeek, int course) {
            this.week = week;
            this.dayOfWeek = dayOfWeek;
            this.course = course;
        }

        public int getWeek() {
            return week;
        }

        public void setWeek(int week) {
            this.week = week;
        }

        public int getDayOfWeek() {
            return dayOfWeek;
        }

        public void setDayOfWeek(int dayOfWeek) {
            this.dayOfWeek = dayOfWeek;
        }

        public int getCourse() {
            return course;
        }

        public void setCourse(int course) {
            this.course = course;
        }
    }
}
