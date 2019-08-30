package com.example.imuhelper.bean;

public class TermBean extends DateBean {
    private int id=-1;
    private String name;

    public TermBean(){}

    public TermBean(String name,int year, int month, int day) {
        super(year, month, day);
        this.name=name;
    }

    public TermBean(String name,int year, int month, int day, int day_of_week) {
        super(year, month, day, day_of_week);
        this.name=name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
