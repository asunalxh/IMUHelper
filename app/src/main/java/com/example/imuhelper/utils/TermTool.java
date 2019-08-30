package com.example.imuhelper.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.timetable.bean.TermBean;
import com.example.timetable.db.CourseDBHelper;
import com.example.timetable.db.TermDBHelper;

public class TermTool {

    private static int selectedId;//当前显示的学期存储用id

    private static int termLength = 1;//当前显示的学期总共多少周
    private static int courseNumber = 8;//当前显示的学期一天几节课

    private final static int minCourseNumber = 8;

    /**
     * 获得显示的学期课程信息存储表名
     *
     * @param context
     * @return
     */
    public static String getDBName(Context context) {
        return getSelectedTerm(context).getName() + ".db";
    }

    /**
     * 读取存储的选中学期信息
     *
     * @param context
     * @return
     */
    public static boolean init(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("selected_term", Context.MODE_PRIVATE);
        selectedId = sharedPreferences.getInt("id", -1);
        if (selectedId == -1)
            return false;
        reFreshTerm(context);
        return true;
    }

    /**
     * 得到选中学期的全部信息
     *
     * @param context
     * @return
     */
    public static TermBean getSelectedTerm(Context context) {
        TermDBHelper termDBHelper = new TermDBHelper(context, null);
        return termDBHelper.getTerm(selectedId);
    }

    /**
     * 重新选择显示的学期
     *
     * @param context
     * @param name
     */
    public static void setSelectedTerm(Context context, String name) {
        TermDBHelper termDBHelper = new TermDBHelper(context, null);
        TermBean termBean = termDBHelper.getTerm(name);
        selectedId = termBean.getId();
        SharedPreferences.Editor editor = context.getSharedPreferences("selected_term", Context.MODE_PRIVATE).edit();
        editor.putInt("id", selectedId);
        editor.apply();
        reFreshTerm(context);
    }

    /**
     * @param context
     * @param id
     */
    public static void setSelectedTerm(Context context, int id) {
        selectedId = id;
        SharedPreferences.Editor editor = context.getSharedPreferences("selected_term", Context.MODE_PRIVATE).edit();
        editor.putInt("id", id);
        editor.apply();
        reFreshTerm(context);
    }


    public static void setNoSelect(Context context) {
        selectedId = -1;
        SharedPreferences.Editor editor = context.getSharedPreferences("selected_term", Context.MODE_PRIVATE).edit();
        editor.putInt("id", selectedId);
        editor.apply();
    }

    /**
     * 获得最大的学期长度，最大一天的节次
     *
     * @param context
     */
    public static void reFreshTerm(Context context) {
        CourseDBHelper courseDBHelper = new CourseDBHelper(context, getDBName(context), null);
        SQLiteDatabase db = courseDBHelper.getWritableDatabase();
        Cursor cursor = db.query("course", null, null, null, null, null, "end_course desc");
        if (cursor.moveToFirst()) {
            courseNumber = cursor.getInt(cursor.getColumnIndex("end_course"));
            courseNumber = Math.max(courseNumber, minCourseNumber);
        } else courseNumber = minCourseNumber;
        cursor.close();
        cursor = db.query("course", null, null, null, null, null, "end_week desc");
        if (cursor.moveToFirst()) {
            termLength = cursor.getInt(cursor.getColumnIndex("end_week"));
            termLength = Math.max(1, termLength);
        } else termLength = 1;
        cursor.close();
    }


    /**
     * 返回当前显示的学期总共有多少周
     *
     * @return
     */
    public static int getTermLength() {
        return termLength;
    }

    /**
     * 返回当前显示的学期一天有几节课
     *
     * @return
     */
    public static int getCourseNumber() {
        return courseNumber;
    }

    /**
     * 获得当前选中的学期存储用id
     *
     * @return
     */
    public static int getSelectedId() {
        return selectedId;
    }
}
