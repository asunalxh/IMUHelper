package com.example.imuhelper.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import com.example.timetable.R;
import com.example.timetable.fragments.NoneTermFragment;
import com.example.timetable.fragments.TimePagerFragment;
import com.example.timetable.utils.DIYDialog;
import com.example.timetable.utils.IntentTool;
import com.example.timetable.utils.MessageDialog;
import com.example.timetable.utils.TermTool;


public class TimeTableActivity extends AppCompatActivity {

    private TimePagerFragment timePagerFragment;
    private NoneTermFragment noneTermFragment;
    private Toolbar toolbar;
    private boolean isTimetable = false;
    private TextView titleTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);

        toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        titleTv=findViewById(R.id.main_toolbar_title);

        initView();
    }


    private void initView() {
        //预加载
        timePagerFragment = new TimePagerFragment();
        noneTermFragment = new NoneTermFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.main_content_layout, timePagerFragment)
                .add(R.id.main_content_layout, noneTermFragment);

        //如果有学期信息
        if (TermTool.init(this)) {
            isTimetable = true;
            invalidateOptionsMenu();
            transaction.hide(noneTermFragment)
                    .commit();
            timePagerFragment.loadTimetable();
        } /*没有学期信息*/ else {
            isTimetable = false;
            transaction.hide(timePagerFragment)
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_toolber, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (isTimetable == false) {
            menu.findItem(R.id.mainToolbar_addCourse_menu).setVisible(false);
            menu.findItem(R.id.mainToolbar_download_menu).setVisible(false);
        } else {
            menu.findItem(R.id.mainToolbar_addCourse_menu).setVisible(true);
            menu.findItem(R.id.mainToolbar_download_menu).setVisible(true);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            //添加课程
            case R.id.mainToolbar_addCourse_menu:
                Intent intent = new Intent(getBaseContext(), AddCourseActvity.class);
                intent.putExtra("requestcode", IntentTool.TimePager_To_AddCourse);
                startActivityForResult(intent, IntentTool.TimePager_To_AddCourse);
                break;
            //下载课程表
            case R.id.mainToolbar_download_menu:
                MessageDialog.Builder builder=new MessageDialog.Builder(this,"当前课程表将会被覆盖","确定","取消");
                builder.setRightButtonOnClickListener(new DIYDialog.Builder.onButtonClickListener() {
                    @Override
                    public void onClick() {
                        Intent intent1 = new Intent(getBaseContext(), IMU.class);
                        startActivityForResult(intent1, IntentTool.Main_To_IMU);
                    }
                });
                builder.create().show();

                break;

            //学期列表
            case R.id.mainToolbar_termList_menu:
                Intent intent2 = new Intent(getBaseContext(), TermListActivity.class);
                startActivityForResult(intent2, IntentTool.Main_To_TermList);
                break;

            default:
                break;
        }
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            //添加课程
            case IntentTool.TimePager_To_AddCourse:
                if (resultCode == IntentTool.RESULT_OK) {
                    timePagerFragment.reFlesh();
                }
                break;

            //无学期变有学期
            case IntentTool.NoneTerm_To_AddTerm:
                if (resultCode == IntentTool.RESULT_OK) {
                    TermTool.setSelectedTerm(getBaseContext(), data.getStringExtra("name"));
                    isTimetable = true;
                    invalidateOptionsMenu();
                    getSupportFragmentManager().beginTransaction()
                            .hide(noneTermFragment)
                            .show(timePagerFragment)
                            .commit();
                    timePagerFragment.loadTimetable();
                }
                break;

            //课程编辑
            case IntentTool.CourseCard_To_AddCourse:
                if (resultCode == IntentTool.RESULT_OK)
                    timePagerFragment.reFlesh();
                break;

            //下载课程
            case IntentTool.Main_To_IMU:
                if (resultCode == IntentTool.RESULT_OK)
                    timePagerFragment.reFlesh();
                break;


            case IntentTool.Main_To_TermList:
                if (resultCode == IntentTool.RESULT_OK){
                    int id=TermTool.getSelectedId();
                    if(id==-1){
                        getSupportFragmentManager().beginTransaction()
                                .show(noneTermFragment)
                                .hide(timePagerFragment)
                                .commit();
                    }else{
                        getSupportFragmentManager().beginTransaction()
                                .show(timePagerFragment)
                                .hide(noneTermFragment)
                                .commit();
                        timePagerFragment.loadTimetable();
                    }
                    invalidateOptionsMenu();
                }
        }
    }



}
