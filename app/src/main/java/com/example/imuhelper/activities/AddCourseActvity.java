package com.example.imuhelper.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timetable.R;
import com.example.timetable.adapter.AddCourseRvAdapter;
import com.example.timetable.bean.CourseBean;
import com.example.timetable.db.CourseDBHelper;
import com.example.timetable.utils.CourseTool;
import com.example.timetable.utils.IntentTool;
import com.example.timetable.utils.TermTool;

import java.util.ArrayList;
import java.util.List;

public class AddCourseActvity extends AppCompatActivity {

    private RecyclerView addcourseRv;
    private AddCourseRvAdapter adapter;
    private List<CourseBean> list = new ArrayList<>();//最新课程信息
    private List<CourseBean> lastList = new ArrayList<>();
    private String lastName;//原始课程名称
    private boolean isEdit = false;//判断是新建课程还是编辑课程，true为编辑

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcousre);
        /*testcode*/
        Toolbar toolbar = findViewById(R.id.addCourse_toolbar);
        setSupportActionBar(toolbar);
        /**/
        init();
    }

    private void init() {
        addcourseRv = findViewById(R.id.addCourse_Rv);
        //添加头尾
        for (int i = 0; i < 2; i++)
            list.add(new CourseBean());

        Intent intent = getIntent();
        int RequestCode = intent.getIntExtra("requestcode", 0);


        //如果是新建课程
        if (RequestCode == IntentTool.TimePager_To_AddCourse) {
            list.add(1, new CourseBean());
            isEdit = false;
        } /*编辑课程*/ else {
            isEdit = true;
            invalidateOptionsMenu();

            lastName = intent.getStringExtra("name");
            CourseDBHelper courseDBHelper = new CourseDBHelper(this, TermTool.getDBName(this), null);

            lastList = courseDBHelper.getCourse(lastName);

            for (CourseBean courseBean : lastList) {
                list.add(list.size() - 1, courseBean);
            }
            list.get(0).setName(list.get(1).getName());
            courseDBHelper.close();
        }


        adapter = new AddCourseRvAdapter(list, this);
        //设置添加时间点按钮点击事件
        adapter.setOnBottomButtonClickListener(new AddCourseRvAdapter.OnBottomButtonClickListener() {
            @Override
            public void onClick() {
                list.add(adapter.getItemCount() - 1, new CourseBean());
                adapter.notifyItemChanged(adapter.getItemCount() - 2);
            }
        });
        //设置删除按钮点击事件
        adapter.setOnContentButtonClickListener(new AddCourseRvAdapter.OnContentButtonClickListener() {
            @Override
            public void onClick(final int position) {
                if (list.size() <= 3)
                    Toast.makeText(getBaseContext(), "不能再少了！！！", Toast.LENGTH_SHORT).show();
                else {
                    list.remove(position);
                    adapter.notifyItemRemoved(position);
                    adapter.notifyDataSetChanged();
                }
            }
        });
        addcourseRv.setAdapter(adapter);
        addcourseRv.setLayoutManager(new LinearLayoutManager(this));
        ((Button)findViewById(R.id.addCourseToolbar_commit_menu)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CourseDBHelper courseDBHelper = new CourseDBHelper(getBaseContext(), TermTool.getDBName(getBaseContext()), null);
                String name = list.get(0).getName();


                if (isEdit) {
                    //检查课程
                    if (name.length() <= 0) {
                        Toast.makeText(getBaseContext(), "请输入课程名", Toast.LENGTH_SHORT).show();
                        return;
                    } else if ((!name.equals(lastName)) && courseDBHelper.isExisted(name)) {
                        Toast.makeText(getBaseContext(), "课程已存在", Toast.LENGTH_SHORT).show();
                        return;
                    }


                    boolean flag = true;

                    CourseTool courseTool = new CourseTool();

                    //检查课时是否重复
                    for (int i = 1; i < list.size() - 1; i++) {
                        CourseBean courseBean = list.get(i);
                        courseBean.setName(name);
                        for (int week = courseBean.getStart_week(); week <= courseBean.getEnd_week(); week++)
                            for (int course = courseBean.getStart_course(); course <= courseBean.getEnd_course(); course++) {
                                if (courseTool.addAndJust(week, courseBean.getDay_of_week(), course) == false)
                                    flag = false;

                                switch (courseBean.getType()) {
                                    case CourseBean.TYPE_ALL:
                                        if (courseDBHelper.isExisted(week, courseBean.getDay_of_week(), course, lastName))
                                            flag = false;
                                        break;
                                    case CourseBean.TYPE_SINGLE:
                                        if (week % 2 == 1 && courseDBHelper.isExisted(week, courseBean.getDay_of_week(), course, lastName))
                                            flag = false;
                                        break;
                                    case CourseBean.TYPPE_DOUBLE:
                                        if (week % 2 == 0 && courseDBHelper.isExisted(week, courseBean.getDay_of_week(), course, lastName))
                                            flag = false;
                                        break;
                                }
                            }


                    }


                    if (flag) {
                        for (int i = 1; i < list.size() - 1; i++) {
                            CourseBean courseBean = list.get(i);
                            if (courseBean.getId() == -1)
                                courseDBHelper.insert(courseBean);
                            else {
                                courseDBHelper.updateCourse(courseBean);
                            }
                        }
                        for(CourseBean courseBean:lastList){
                            boolean f=true;
                            for(int i=1;i<list.size()-1;i++){
                                if(courseBean.getId()==list.get(i).getId()){
                                    f=false;
                                    break;
                                }
                            }
                            if(f){
                                courseDBHelper.remove(courseBean);
                            }
                        }
                        Intent intent = new Intent();
                        intent.putExtra("name", name);
                        setResult(IntentTool.RESULT_OK);
                        finish();
                    } else {
                        Toast.makeText(getBaseContext(), "课时重复", Toast.LENGTH_SHORT).show();
                    }


                } else {

                    //检查课程
                    if (name.length() <= 0) {
                        Toast.makeText(getBaseContext(), "请输入课程名", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (courseDBHelper.isExisted(name)) {
                        Toast.makeText(getBaseContext(), "课程已存在", Toast.LENGTH_SHORT).show();
                        return;
                    }


                    boolean flag = true;

                    CourseTool courseTool = new CourseTool();
                    //检查课时是否重复
                    for (int i = 1; i < list.size() - 1; i++) {
                        CourseBean courseBean = list.get(i);
                        courseBean.setName(name);
                        for (int week = courseBean.getStart_week(); week <= courseBean.getEnd_week(); week++)
                            for (int course = courseBean.getStart_course(); course <= courseBean.getEnd_course(); course++) {
                                if (courseTool.addAndJust(week, courseBean.getDay_of_week(), course) == false)
                                    flag = false;
                                switch (courseBean.getType()) {
                                    case CourseBean.TYPE_ALL:
                                        if (courseDBHelper.isExisted(week, courseBean.getDay_of_week(), course))
                                            flag = false;
                                        break;
                                    case CourseBean.TYPE_SINGLE:
                                        if (week % 2 == 1 && courseDBHelper.isExisted(week, courseBean.getDay_of_week(), course))
                                            flag = false;
                                        break;
                                    case CourseBean.TYPPE_DOUBLE:
                                        if (week % 2 == 0 && courseDBHelper.isExisted(week, courseBean.getDay_of_week(), course))
                                            flag = false;
                                        break;
                                }
                            }
                    }

                    if (flag) {
                        for (int i = 1; i < list.size() - 1; i++)
                            courseDBHelper.insert(list.get(i));
                        Intent intent = new Intent();
                        intent.putExtra("name", name);
                        setResult(IntentTool.RESULT_OK);
                        finish();
                    } else {
                        courseDBHelper.remove(name);
                        Toast.makeText(getBaseContext(), "课时重复", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_addcourse_toolbar, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (!isEdit)
            menu.findItem(R.id.addCourseToolbar_delete_menu).setVisible(false);
        else menu.findItem(R.id.addCourseToolbar_delete_menu).setVisible(true);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            //点击删除课程按钮
            case R.id.addCourseToolbar_delete_menu:
                CourseDBHelper dbHelper = new CourseDBHelper(this, TermTool.getDBName(this), null);
                dbHelper.remove(lastList.get(0).getName());
                setResult(IntentTool.RESULT_OK);
                finish();

                break;
            default:
                break;
        }
        return true;
    }
}
