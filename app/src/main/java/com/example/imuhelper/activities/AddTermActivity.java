package com.example.imuhelper.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.imuhelper.R;
import com.example.imuhelper.bean.*;
import com.example.imuhelper.db.*;
import com.example.imuhelper.utils.*;

public class AddTermActivity extends AppCompatActivity {

    private EditText nameEdit;
    private TextView yearTv;
    private TextView monthTv;
    private TextView dayTv;
    private LinearLayout timeLayout;
    private Button button;
    private int year, month, day;

    private boolean isEdit = false;
    private int lastId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addterm);

        init();
    }

    private void init() {
        nameEdit = findViewById(R.id.addTerm_name_edit);
        yearTv = findViewById(R.id.addTerm_year_Tv);
        monthTv = findViewById(R.id.addTerm_month_Tv);
        dayTv = findViewById(R.id.addTerm_day_Tv);
        button = findViewById(R.id.addTerm_btn);
        timeLayout = findViewById(R.id.addTerm_time);


        final Intent intent = getIntent();
        lastId = intent.getIntExtra("id", -1);
        //新建课程表
        if (lastId == -1) {
            isEdit = false;
            CalendarHelper calendarHelper = new CalendarHelper();
            year = calendarHelper.getYear();
            month = calendarHelper.getMonth();
            day = calendarHelper.getDay();
        }/*编辑课程表*/ else {
            isEdit = true;
            TermDBHelper termDBHelper = new TermDBHelper(this, null);
            TermBean termBean = termDBHelper.getTerm(lastId);
            nameEdit.setText(termBean.getName());
            year = termBean.getYear();
            month = termBean.getMonth();
            day = termBean.getDay();
        }

        yearTv.setText(String.valueOf(year));
        monthTv.setText(String.valueOf(month));
        dayTv.setText(String.valueOf(day));


        timeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CalendarHelper helper = new CalendarHelper();
                CalendarView calendarView = new CalendarView(getBaseContext());
                calendarView.setSelectedWeekBackgroundColor(Color.BLUE);
                calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(CalendarView calendarView, int i, int i1, int i2) {
                        helper.setDate(i, i1 + 1, i2);
                    }
                });
                DIYDialog.Builder builder = new DIYDialog.Builder(AddTermActivity.this, calendarView);
                builder.setLeftButtonText("取消");
                builder.setRightButtonText("确认");
                builder.setRightButtonOnClickListener(new DIYDialog.Builder.onButtonClickListener() {
                    @Override
                    public void onClick() {
                        year = helper.getYear();
                        month = helper.getMonth();
                        day = helper.getDay();
                        yearTv.setText(String.valueOf(year));
                        monthTv.setText(String.valueOf(month));
                        dayTv.setText(String.valueOf(day));
                    }
                });
                DIYDialog diyDialog = builder.create();
                diyDialog.show();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameEdit.getText().toString();
                TermDBHelper dbHelper = new TermDBHelper(getBaseContext(), null);
                if (name.length() <= 0) {
                    Toast.makeText(getBaseContext(), "请输入名称", Toast.LENGTH_SHORT).show();
                    return;
                }
                TermBean termBean = new TermBean(name, year, month, day);

                if (isEdit) {
                    if (!dbHelper.isExisted(name, lastId)) {
                        dbHelper.updateTerm(lastId, termBean);

                        Intent result = new Intent();
                        result.putExtra("name", name);
                        result.putExtra("position", getIntent().getIntExtra("position", -1));
                        setResult(IntentTool.RESULT_OK, result);
                        finish();
                    } else Toast.makeText(getBaseContext(), "名称已存在", Toast.LENGTH_SHORT).show();
                } else {
                    if (!dbHelper.isExisted(name, lastId)) {

                        dbHelper.insertTerm(termBean);
                        TermTool.setSelectedTerm(getBaseContext(),name);
                        Intent result = new Intent();
                        result.putExtra("name", name);
                        setResult(IntentTool.RESULT_OK, result);
                        finish();
                    } else Toast.makeText(getBaseContext(), "名称已存在", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

}
