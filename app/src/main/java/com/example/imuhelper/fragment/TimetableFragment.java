package com.example.imuhelper.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.timetable.R;
import com.example.timetable.activities.AddCourseActvity;
import com.example.timetable.bean.CourseBean;
import com.example.timetable.db.CourseDBHelper;
import com.example.timetable.utils.IntentTool;
import com.example.timetable.utils.TermTool;

import java.util.List;


/**
 * 课程表碎片
 */
public class TimetableFragment extends Fragment {

    private FrameLayout[] weeks;//七列课程表父控件
    private LinearLayout layout;//左侧节次卡片父控件
    private final int height = 200;//课程卡片单位长度
    private int week;//第几周课程

    public TimetableFragment(int week) {
        this.week = week;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_timetable, container, false);
        initView(view);
        initCourse();

        return view;
    }


    /**
     * 初始化界面，动态显示一天节次
     *
     * @param view
     */
    private void initView(View view) {
        int Course_number= TermTool.getCourseNumber();
        layout = view.findViewById(R.id.timeTable_courseNumber_layout);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        for (int i = 1; i <= Course_number; i++) {
            View views = getLayoutInflater().inflate(R.layout.item_number_card, null);
            ((TextView)views.findViewById(R.id.numberCard_Tv)).setText(String.valueOf(i));
            views.setLayoutParams(params);
            layout.addView(views);
        }
        weeks = new FrameLayout[8];
        weeks[1] = view.findViewById(R.id.timeTable_monday_layout);
        weeks[2] = view.findViewById(R.id.timeTable_tuesday_layout);
        weeks[3] = view.findViewById(R.id.timeTable_wednesday_layout);
        weeks[4] = view.findViewById(R.id.timeTable_thursday_layout);
        weeks[5] = view.findViewById(R.id.timeTable_friday_layout);
        weeks[6] = view.findViewById(R.id.timeTable_saturday_layout);
        weeks[7] = view.findViewById(R.id.timeTable_sunday_layout);
    }


    /**
     * 从数据库获得课程信息
     */
    private void initCourse() {
        CourseDBHelper dbHelper=new CourseDBHelper(getContext(),TermTool.getDBName(getContext()),null);
        List<CourseBean> list=dbHelper.getCourse(week);
        for(int i=0;i<list.size();i++){
            CourseBean courseBean=list.get(i);
            insert_CourseCard(courseBean);
        }
        dbHelper.close();
    }


    /**
     * 根据课程信息自动地位显示到课程表上
     *
     * @param courseBean
     */
    public void insert_CourseCard(CourseBean courseBean) {
        View card = getLayoutInflater().inflate(R.layout.item_course_card, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height * (courseBean.getEnd_course() - courseBean.getStart_course() + 1));
        TextView textView=card.findViewById(R.id.courseCard_text);
        textView.setText(courseBean.getName()+'\n'+courseBean.getTeacher()+'\n'+courseBean.getClassroom());
        card.setLayoutParams(params);
        card.setY(height * (courseBean.getStart_course() - 1));
        int day_of_week = courseBean.getDay_of_week();

        final CourseDBHelper dbHelper=new CourseDBHelper(getContext(),TermTool.getDBName(getContext()),null);

        /*课程卡片点长按事件*/
        card.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent intent=new Intent(getActivity(), AddCourseActvity.class);
                intent.putExtra("requestcode", IntentTool.CourseCard_To_AddCourse);

                int dayOfWeek=getDay_of_Week(view);
                int startCourse=getStart_course(view);
                CourseBean course=dbHelper.getCourse(week,dayOfWeek,startCourse);

                intent.putExtra("name",course.getName());

                getActivity().startActivityForResult(intent,IntentTool.CourseCard_To_AddCourse);
                return true;
            }
        });
        weeks[day_of_week].addView(card);
    }


    /**
     * 重新加载课程，应用课程更改
     */
    public void reFresh(){


        int Course_number=TermTool.getCourseNumber();
        layout.removeAllViews();

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        for (int i = 1; i <= Course_number; i++) {
            View views = getLayoutInflater().inflate(R.layout.item_number_card, null);
            ((TextView)views.findViewById(R.id.numberCard_Tv)).setText(String.valueOf(i));
            views.setLayoutParams(params);
            layout.addView(views);
        }

        for(int i=1;i<=7;i++)
            weeks[i].removeAllViews();

        initCourse();
    }


    /**
     *根据卡片位置信息获得改课程位于周几
     *
     * @param view
     * @return
     */
    private int getDay_of_Week(View view){
        FrameLayout parent= (FrameLayout) view.getParent();
        for(int i=1;i<=7;i++)
            if(weeks[i].equals(parent)) {
                return i;
            }
        return 0;
    }


    /**
     * 根据卡片位置信息获得课程开始节次
     *
     * @param view
     * @return
     */
    private int getStart_course(View view){
        int y= (int) view.getY();
        return y/height+1;
    }

}
