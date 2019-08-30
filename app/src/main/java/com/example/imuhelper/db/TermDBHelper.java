package com.example.imuhelper.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.imuhelper.bean.TermBean;

import java.util.ArrayList;
import java.util.List;

public class TermDBHelper extends SQLiteOpenHelper {

    private static final String CREATE_TERM = "create table term(" +
            "id integer primary key autoincrement," +
            "name text," +
            "year integer," +
            "month integer," +
            "day integer," +
            "day_of_week integer)";

    private Context context;

    public TermDBHelper(Context context, SQLiteDatabase.CursorFactory factory) {
        super(context, "term.db", factory, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TERM);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


    public void insertTerm(TermBean termBean) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values=getValues(termBean);
        db.insert("term", null, values);
    }

    public TermBean getTerm(String name){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.query("term",null,"name=?",new String[]{name},null,null,null);
        cursor.moveToFirst();
        return getTermBean(cursor);
    }
    public TermBean getTerm(int id){
        Cursor cursor=this.getWritableDatabase().query("term",null,"id=?",new String[]{String.valueOf(id)},null,null,null);
        cursor.moveToFirst();
        return getTermBean(cursor);
    }
    public List<TermBean> getAllTerm(){
        List<TermBean> list=new ArrayList<>();
        Cursor cursor=this.getWritableDatabase().query("term",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do {
                list.add(getTermBean(cursor));
            }while(cursor.moveToNext());
        }
        return list;
    }

    public void updateTerm(int id,TermBean termBean){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=getValues(termBean);
        db.update("term",values,"id=?",new String[]{String.valueOf(id)});
    }

    public boolean isExisted(String name) {
        Cursor cursor = this.getWritableDatabase().query("term", null, "name=?", new String[]{name}, null, null, null);
        if (cursor.getCount() <= 0)
            return false;
        return true;
    }
    public boolean isExisted(String name,int id){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.query("term",null,"name=? and id!=?",
                new String[]{name,String.valueOf(id)},null,null,null);
        if(cursor.getCount()>0)
            return true;
        return false;
    }

    public void remove(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("term", "name=?", new String[]{name});
        CourseDBHelper courseDBHelper=new CourseDBHelper(context,name+".db",null);
        courseDBHelper.clear();
    }
    public void remove(int id){
        SQLiteDatabase db=this.getWritableDatabase();
        String name=getTerm(id).getName();
        db.delete("term","id=?",new String[]{String.valueOf(id)});
        CourseDBHelper courseDBHelper=new CourseDBHelper(context,name+".db",null);
        courseDBHelper.clear();
    }

    private TermBean getTermBean(Cursor cursor) {
        TermBean termBean = new TermBean();
        termBean.setId(cursor.getInt(cursor.getColumnIndex("id")));
        termBean.setName(cursor.getString(cursor.getColumnIndex("name")));
        termBean.setYear(cursor.getInt(cursor.getColumnIndex("year")));
        termBean.setMonth(cursor.getInt(cursor.getColumnIndex("month")));
        termBean.setDay(cursor.getInt(cursor.getColumnIndex("day")));
        termBean.setDay_of_week(cursor.getInt(cursor.getColumnIndex("day_of_week")));
        return termBean;
    }
    private ContentValues getValues(TermBean termBean){
        ContentValues values = new ContentValues();
        values.put("name", termBean.getName());
        values.put("year", termBean.getYear());
        values.put("month", termBean.getMonth());
        values.put("day", termBean.getDay());
        values.put("day_of_week", termBean.getDay_of_week());
        return values;
    }
}
