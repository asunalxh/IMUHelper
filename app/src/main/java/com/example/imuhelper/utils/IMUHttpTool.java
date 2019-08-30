package com.example.imuhelper.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.timetable.activities.TimeTableActivity;
import com.example.timetable.bean.CourseBean;
import com.example.timetable.db.CourseDBHelper;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class IMUHttpTool {

    private String JSESSIONID;
    private ImageView imageView;
    private Handler handler = new Handler();
    private Context context;

    private ProgressDialog dialog;

    public IMUHttpTool(Context context, ImageView imageView) {
        this.context = context;
        this.imageView = imageView;
        ProgressDialog.Builder builder = new ProgressDialog.Builder(context);
        builder.setText("正在下载");
        dialog = builder.create();
    }

    public void initHttp() {
        final OkHttpClient client = new OkHttpClient.Builder()
                .build();
        final Request request = new Request.Builder()
                .url("http://jwxt.imu.edu.cn/")
                .addHeader("Host", "jwxt.imu.edu.cn")
                .addHeader("Connection", "keep-alive")
                .addHeader("Upgrade-Insecure-Requests", "1")
                .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3")
                .addHeader("Accept-Language", "zh,zh-TW;q=0.9,en-US;q=0.8,en;q=0.7,zh-CN;q=0.6")
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.message().equals("OK")) {
                    getJSESSIONID(response);
                    getIMG();
                }
            }
        });
    }

    public void Log_In(String username, String password, String code) {
        OkHttpClient client = new OkHttpClient();
        FormBody body = new FormBody.Builder()
                .add("j_username", username)
                .add("j_password", password)
                .add("j_captcha", code)
                .build();
        Request request = new Request.Builder()
                .url("http://jwxt.imu.edu.cn/j_spring_security_check")
                .addHeader("Host", "jwxt.imu.edu.cn")
                .addHeader("Connection", "keep-alive")
                .addHeader("Cache-Control", "max-age=0")
                .addHeader("Origin", "http://jwxt.imu.edu.cn")
                .addHeader("Upgrade-Insecure-Requests", "1")
                .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3")
                .addHeader("Referer", "http://jwxt.imu.edu.cn/login")
                .addHeader("Accept-Language", "zh,zh-TW;q=0.9,en-US;q=0.8,en;q=0.7,zh-CN;q=0.6")
                .addHeader("Cookie", "JSESSIONID=" + JSESSIONID)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                String string = response.message();
                if (string.equals("Internal Server Error")) {
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "登陆错误", Toast.LENGTH_SHORT).show();
                            getIMG();
                        }
                    };
                    handler.post(runnable);
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            dialog.show();
                        }
                    });
                    getCourse();
                }
            }
        });
    }

    public void getIMG() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://jwxt.imu.edu.cn/img/captcha.jpg")
                .addHeader("Host", "jwxt.imu.edu.cn")
                .addHeader("Connection", "keep-alive")
                .addHeader("Accept", "image/webp,image/apng,image/*,*/*;q=0.8")
                .addHeader("Referer", "http://jwxt.imu.edu.cn/login")
                .addHeader("Accept-Language", "zh,zh-TW;q=0.9,en-US;q=0.8,en;q=0.7,zh-CN;q=0.6")
                .addHeader("Cookie", "JSESSIONID=" + JSESSIONID)
                .get()
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                InputStream inputStream = response.body().byteStream();
                final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        imageView.setImageBitmap(bitmap);
                    }
                };
                handler.post(runnable);
            }
        });
    }

    public void getCourse() {
        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url("http://jwxt.imu.edu.cn/student/courseSelect/thisSemesterCurriculum/ajaxStudentSchedule/callback")
                .addHeader("Host", "jwxt.imu.edu.cn")
                .addHeader("Connection", "keep-alive")
                .addHeader("Accept", "*/*")
                .addHeader("X-Requested-With", "XMLHttpRequest")
                .addHeader("Referer", "http://jwxt.imu.edu.cn/student/courseSelect/thisSemesterCurriculum/index")
                .addHeader("Accept-Language", "zh,zh-TW;q=0.9,en-US;q=0.8,en;q=0.7,zh-CN;q=0.6")
                .addHeader("Cookie", "selectionBar=82022; JSESSIONID=" + JSESSIONID)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                storageCourse(response.body().string());
                Log_Out();
            }
        });
    }

    public void Index() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://jwxt.imu.edu.cn/index.jsp")
                .addHeader("Host", "jwxt.imu.edu.cn")
                .addHeader("Connection", "keep-alive")
                .addHeader("Cache-Control", "max-age=0")
                .addHeader("Upgrade-Insecure-Requests", "1")
                .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3")
                .addHeader("Referer", "http://jwxt.imu.edu.cn/login")
                .addHeader("Accept-Language", "zh,zh-TW;q=0.9,en-US;q=0.8,en;q=0.7,zh-CN;q=0.6")
                .addHeader("Cookie", "JSESSIONID=" + JSESSIONID)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
            }
        });

    }

    public void Log_Out() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://jwxt.imu.edu.cn/logout")
                .addHeader("Host", "jwxt.imu.edu.cn")
                .addHeader("Connection", "keep-alive")
                .addHeader("Upgrade-Insecure-Requests", "1")
                .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3")
                .addHeader("Referer", "http://jwxt.imu.edu.cn/student/courseSelect/thisSemesterCurriculum/index")
                .addHeader("Accept-Language", "zh,zh-TW;q=0.9,en-US;q=0.8,en;q=0.7,zh-CN;q=0.6")
                .addHeader("Cookie", "selectionBar=82022")
                .addHeader("Cookie", "JSESSIONID=" + JSESSIONID)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                    }
                });
                Intent intent = new Intent(context, TimeTableActivity.class);
                ((Activity) context).setResult(IntentTool.RESULT_OK, intent);
                ((Activity) context).finish();
            }
        });
    }

    private void getJSESSIONID(Response response) {
        JSESSIONID = response.header("Set-Cookie").substring(11, 32);
    }

    private void storageCourse(final String string) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                CourseDBHelper courseDBHelper = new CourseDBHelper(context, TermTool.getDBName(context), null);
                courseDBHelper.clear();
                JSONObject response = JSONObject.parseObject(string);
                JSONArray dateList = response.getJSONArray("dateList");
                JSONObject coursedata = dateList.getJSONObject(0);
                JSONArray selectCourseList = coursedata.getJSONArray("selectCourseList");
                for (int i = 0; i < selectCourseList.size(); i++) {
                    JSONObject course = selectCourseList.getJSONObject(i);
                    String name = course.getString("courseName");
                    String teacher = course.getString("attendClassTeacher");
                    JSONArray timeAndplace = course.getJSONArray("timeAndPlaceList");
                    if (timeAndplace == null)
                        continue;
                    for (int j = 0; j < timeAndplace.size(); j++) {
                        JSONObject time = timeAndplace.getJSONObject(j);
                        CourseBean courseBean = new CourseBean();
                        courseBean.setName(name);
                        courseBean.setTeacher(teacher);
                        courseBean.setDay_of_week(time.getInteger("classDay"));
                        courseBean.setClassroom(time.getString("teachingBuildingName") + time.getString("classroomName"));
                        courseBean.setStart_course(time.getInteger("classSessions"));
                        courseBean.setEnd_course(time.getInteger("continuingSession") + courseBean.getStart_course() - 1);
                        String type = time.getString("weekDescription");
                        if (type.equals("单周")) {
                            courseBean.setStart_week(0);
                            courseBean.setEnd_week(0);
                            courseBean.setType(CourseBean.TYPE_SINGLE);
                        } else if (type.equals("双周")) {
                            courseBean.setStart_week(0);
                            courseBean.setEnd_week(0);
                            courseBean.setType(CourseBean.TYPPE_DOUBLE);
                        } else {
                            courseBean.setType(CourseBean.TYPE_ALL);
                            int start_week = 0;
                            int end_week = 0;
                            int z = 0;
                            for (; z < type.length(); z++) {
                                char x = type.charAt(z);
                                if (x >= '0' && x <= '9') {
                                    start_week = start_week * 10 + (x - '0');
                                } else break;
                            }
                            for (z++; z < type.length(); z++) {
                                char x = type.charAt(z);
                                if (x >= '0' && x <= '9') {
                                    end_week = end_week * 10 + (x - '0');
                                } else break;
                            }
                            courseBean.setStart_week(start_week);
                            courseBean.setEnd_week(end_week);
                        }
                        courseDBHelper.insert(courseBean);
                    }
                }
            }
        }).start();

    }
}
