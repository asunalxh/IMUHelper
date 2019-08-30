package com.example.imuhelper.bean;

public class CourseBean {

    public static final int TYPE_ALL = 0;
    public static final int TYPE_SINGLE = 1;
    public static final int TYPPE_DOUBLE = 2;

    private int id = -1;
    private String name = "";
    private String teacher = "";
    private String classroom = "";
    private int type = TYPE_ALL;//0全部，1单周，2双周
    private int start_course = 1;
    private int end_course = 1;
    private int day_of_week = 1;
    private int start_week = 0;
    private int end_week = 0;

    public CourseBean() {

    }

    public CourseBean(String name, String teacher, String classroom) {
        this.name = name;
        this.teacher = teacher;
        this.classroom = classroom;
    }

    public CourseBean(int type, int start_course, int end_course, int day_of_week) {
        this.type = type;
        this.start_course = start_course;
        this.end_course = end_course;
        this.day_of_week = day_of_week;
    }

    public CourseBean(int type, int start_course, int end_course, int day_of_week, int start_week, int end_week) {
        this.type = type;
        this.start_course = start_course;
        this.end_course = end_course;
        this.day_of_week = day_of_week;
        this.start_week = start_week;
        this.end_week = end_week;
    }

    public CourseBean(String name, String teacher, String classroom, int type, int start_course, int end_course, int day_of_week) {
        this.classroom = classroom;
        this.name = name;
        this.teacher = teacher;
        this.type = type;
        this.start_course = start_course;
        this.end_course = end_course;
        this.day_of_week = day_of_week;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStart_course() {
        return start_course;
    }

    public void setStart_course(int start_course) {
        this.start_course = start_course;
    }

    public int getEnd_course() {
        return end_course;
    }

    public void setEnd_course(int end_course) {
        this.end_course = end_course;
    }

    public int getDay_of_week() {
        return day_of_week;
    }

    public void setDay_of_week(int day_of_week) {
        this.day_of_week = day_of_week;
    }

    public int getStart_week() {
        return start_week;
    }

    public void setStart_week(int start_week) {
        this.start_week = start_week;
    }

    public int getEnd_week() {
        return end_week;
    }

    public void setEnd_week(int end_week) {
        this.end_week = end_week;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
