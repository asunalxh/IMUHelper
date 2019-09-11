package com.example.imuhelper.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.example.imuhelper.bean.CourseBean;
import com.example.imuhelper.utils.TermTool;

import java.util.ArrayList;
import java.util.List;

public class CourseDBHelper extends SQLiteOpenHelper {

    private static final String CREATE_COURSE = "create table course(" +
            "id integer primary key autoincrement," +
            "name text," +
            "teacher text," +
            "classroom text," +
            "start_course integer," +
            "end_course integer," +
            "day_of_week integer," +
            "start_week integer," +
            "end_week integer," +
            "type integer)";


    public CourseDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_COURSE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


    //清空数据
    public void clear() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from course");
        db.close();
    }


    public List<CourseBean> getCourse(int week) {
        int Term_Length = TermTool.getTermLength();

        List<CourseBean> list = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();


        /*全周，课时明确*/
        Cursor cursor = db.query("course", null, "type=? and start_week<=? and end_week>=?",
                new String[]{String.valueOf(CourseBean.TYPE_ALL), String.valueOf(week), String.valueOf(week)},
                null, null, null);
        if (cursor.moveToFirst()) {
            do {
                list.add(getCourseBean(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        int type;


        if (week % 2 == 0)
            type = CourseBean.TYPE_DOUBLE;
        else type = CourseBean.TYPE_SINGLE;


        /*单双周，课时明确*/
        cursor = db.query("course", null, "type=? and start_week<=? and end_week>=?",
                new String[]{String.valueOf(type), String.valueOf(week), String.valueOf(week)},
                null, null, null);
        if (cursor.moveToFirst()) {
            do {
                list.add(getCourseBean(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();


        /*单双周，课时不明确*/
        cursor = db.query("course", null, "type=? and start_week=? and end_week=?",
                new String[]{String.valueOf(type), String.valueOf(0), String.valueOf(0)},
                null, null, null);
        if (cursor.moveToFirst()) {
            do {
                CourseBean courseBean = getCourseBean(cursor);
                list.add(courseBean);
            } while (cursor.moveToNext());
        }
        cursor.close();


        /*全周，课时不明确*/
        cursor = db.query("course", null, "type=? and start_week=? and end_week=?",
                new String[]{String.valueOf(CourseBean.TYPE_ALL), String.valueOf(0), String.valueOf(0)},
                null, null, null);
        if (cursor.moveToFirst()) {
            do {
                CourseBean courseBean = getCourseBean(cursor);
                list.add(courseBean);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return list;
    }

    public CourseBean getCourse(int week, int day_of_week, int start_course) {
        SQLiteDatabase db = this.getWritableDatabase();
        int type = CourseBean.TYPE_ALL;


        /*全周，课时明确*/
        Cursor cursor = db.query("course", null, "type=? and day_of_week=? and start_course=? and start_week<=? and end_week>=?",
                new String[]{String.valueOf(type), String.valueOf(day_of_week), String.valueOf(start_course), String.valueOf(week), String.valueOf(week)},
                null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            return getCourseBean(cursor);
        }


        /*全周，课时不明确*/
        cursor = db.query("course", null, "type=? and day_of_week=? and start_course=? and start_week=? and end_week=?",
                new String[]{String.valueOf(type), String.valueOf(day_of_week), String.valueOf(start_course), String.valueOf(0), String.valueOf(0)},
                null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            return getCourseBean(cursor);
        }


        if (week % 2 == 0)
            type = CourseBean.TYPE_DOUBLE;
        else type = CourseBean.TYPE_SINGLE;


        /*单双周，课时明确*/
        cursor = db.query("course", null, "type=? and day_of_week=? and start_course=? and start_week<=? and end_week>=?",
                new String[]{String.valueOf(type), String.valueOf(day_of_week), String.valueOf(start_course), String.valueOf(week), String.valueOf(week)},
                null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            return getCourseBean(cursor);
        }


        /*单双周，课时不明确*/
        cursor = db.query("course", null, "type=? and day_of_week=? and start_course=? and start_week=? and end_week=?",
                new String[]{String.valueOf(type), String.valueOf(day_of_week), String.valueOf(start_course), String.valueOf(0), String.valueOf(0)},
                null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            return getCourseBean(cursor);
        }

        return null;
    }

    public List<CourseBean> getCourse(String name) {
        List<CourseBean> list = new ArrayList<>();
        Cursor cursor = this.getWritableDatabase().query("course", null, "name=?", new String[]{name}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                list.add(getCourseBean(cursor));
            } while (cursor.moveToNext());
        }
        return list;
    }

    public void updateCourse(int id, ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.update("course", values, "id=?", new String[]{String.valueOf(id)});
    }

    public void updateCourse(CourseBean courseBean) {
        ContentValues values = getValues(courseBean);
        SQLiteDatabase db = this.getWritableDatabase();
        int id=courseBean.getId();
        db.update("course", values, "id=?", new String[]{String.valueOf(id)});
    }

    public void insert(CourseBean courseBean) {
        ContentValues values = getValues(courseBean);
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert("course", null, values);
    }

    public void insert(List<CourseBean> list){
        for (CourseBean courseBean : list) {
            insert(courseBean);
        }
    }

    public void remove(CourseBean courseBean) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("course","id=?",new String[]{String.valueOf(courseBean.getId())});
    }

    public void remove(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("course", "name=?", new String[]{name});
    }

    public boolean isExisted(int week, int day_of_week, int course) {

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query("course", null, "type=? and start_week<=? and end_week>=? and day_of_week=? and start_course<=? and end_course>=?",
                new String[]{String.valueOf(CourseBean.TYPE_ALL), String.valueOf(week), String.valueOf(week), String.valueOf(day_of_week), String.valueOf(course), String.valueOf(course)},
                null, null, null);
        if (cursor.getCount() > 0) {
            return true;
        }
        cursor.close();


        cursor = db.query("course", null, "type=? and start_week=? and end_week=? and day_of_week=? and start_course<=? and end_course>=?",
                new String[]{String.valueOf(CourseBean.TYPE_ALL), String.valueOf(0), String.valueOf(0), String.valueOf(day_of_week), String.valueOf(course), String.valueOf(course)},
                null, null, null);
        if (cursor.getCount() > 0)
            return true;
        cursor.close();

        int type;
        if (week % 2 == 0)
            type = CourseBean.TYPE_DOUBLE;
        else type = CourseBean.TYPE_SINGLE;

        cursor = db.query("course", null, "type=? and start_week<=? and end_week>=? and day_of_week=? and start_course<=? and end_course>=?",
                new String[]{String.valueOf(type), String.valueOf(week), String.valueOf(week), String.valueOf(day_of_week), String.valueOf(course), String.valueOf(course)},
                null, null, null);
        if (cursor.getCount() > 0)
            return true;
        cursor.close();


        cursor = db.query("course", null, "type=? and start_week=? and end_week=? and day_of_week=? and start_course<=? and end_course>=?",
                new String[]{String.valueOf(type), String.valueOf(0), String.valueOf(0), String.valueOf(day_of_week), String.valueOf(course), String.valueOf(course)},
                null, null, null);
        if (cursor.getCount() > 0)
            return true;
        cursor.close();

        return false;
    }

    public boolean isExisted(int week, int day_of_week, int course, String name) {

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query("course", null, "name!=? and type=? and start_week<=? and end_week>=? and day_of_week=? and start_course<=? and end_course>=?",
                new String[]{name, String.valueOf(CourseBean.TYPE_ALL), String.valueOf(week), String.valueOf(week), String.valueOf(day_of_week), String.valueOf(course), String.valueOf(course)},
                null, null, null);
        if (cursor.getCount() > 0) {
            return true;
        }
        cursor.close();


        cursor = db.query("course", null, "name!=? and type=? and start_week=? and end_week=? and day_of_week=? and start_course<=? and end_course>=?",
                new String[]{name, String.valueOf(CourseBean.TYPE_ALL), String.valueOf(0), String.valueOf(0), String.valueOf(day_of_week), String.valueOf(course), String.valueOf(course)},
                null, null, null);
        if (cursor.getCount() > 0)
            return true;
        cursor.close();

        int type;
        if (week % 2 == 0)
            type = CourseBean.TYPE_DOUBLE;
        else type = CourseBean.TYPE_SINGLE;

        cursor = db.query("course", null, "name!=? and type=? and start_week<=? and end_week>=? and day_of_week=? and start_course<=? and end_course>=?",
                new String[]{name, String.valueOf(type), String.valueOf(week), String.valueOf(week), String.valueOf(day_of_week), String.valueOf(course), String.valueOf(course)},
                null, null, null);
        if (cursor.getCount() > 0)
            return true;
        cursor.close();


        cursor = db.query("course", null, "name!=? and type=? and start_week=? and end_week=? and day_of_week=? and start_course<=? and end_course>=?",
                new String[]{name, String.valueOf(type), String.valueOf(0), String.valueOf(0), String.valueOf(day_of_week), String.valueOf(course), String.valueOf(course)},
                null, null, null);
        if (cursor.getCount() > 0)
            return true;
        cursor.close();

        return false;
    }

    public boolean isExisted(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query("course", null, "name=?", new String[]{name}, null, null, null);
        if (cursor.getCount() > 0)
            return true;
        return false;
    }


    private CourseBean getCourseBean(Cursor cursor) {
        CourseBean courseBean = new CourseBean();
        courseBean.setId(cursor.getInt(cursor.getColumnIndex("id")));
        courseBean.setName(cursor.getString(cursor.getColumnIndex("name")));
        courseBean.setClassroom(cursor.getString(cursor.getColumnIndex("classroom")));
        courseBean.setTeacher(cursor.getString(cursor.getColumnIndex("teacher")));
        courseBean.setType(cursor.getInt(cursor.getColumnIndex("type")));
        courseBean.setStart_course(cursor.getInt(cursor.getColumnIndex("start_course")));
        courseBean.setEnd_course(cursor.getInt(cursor.getColumnIndex("end_course")));
        courseBean.setStart_week(cursor.getInt(cursor.getColumnIndex("start_week")));
        courseBean.setEnd_week(cursor.getInt(cursor.getColumnIndex("end_week")));
        courseBean.setDay_of_week(cursor.getInt(cursor.getColumnIndex("day_of_week")));
        return courseBean;
    }

    private ContentValues getValues(CourseBean courseBean) {
        ContentValues values = new ContentValues();
        values.put("name", courseBean.getName());
        values.put("teacher", courseBean.getTeacher());
        values.put("classroom", courseBean.getClassroom());
        values.put("start_course", courseBean.getStart_course());
        values.put("end_course", courseBean.getEnd_course());
        values.put("type", courseBean.getType());
        values.put("day_of_week", courseBean.getDay_of_week());
        values.put("start_week", courseBean.getStart_week());
        values.put("end_week", courseBean.getEnd_week());
        return values;
    }

}
