package com.example.imuhelper.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timetable.R;
import com.example.timetable.bean.CourseBean;
import com.example.timetable.utils.DIYDialog;
import com.example.timetable.utils.DateSelector;
import com.example.timetable.utils.TimeSelector;

import java.util.List;

public class AddCourseRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final static String[] weeks = {"自动", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20"};
    private final static String[] dayOfWeeks = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};

    private final int TYPE_HEADER = 0;
    private final int TYPE_CONTENT = 1;
    private final int TYPE_BOTTOM = 2;

    private List<CourseBean> list;

    private OnBottomButtonClickListener onBottomButtonClickListener;
    private OnContentButtonClickListener onContentButtonClickListener;

    private Context context;

    public void setOnBottomButtonClickListener(OnBottomButtonClickListener onBottomButtonClickListener) {
        this.onBottomButtonClickListener = onBottomButtonClickListener;
    }

    public void setOnContentButtonClickListener(OnContentButtonClickListener onContentButtonClickListener) {
        this.onContentButtonClickListener = onContentButtonClickListener;
    }

    public AddCourseRvAdapter(List<CourseBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    private class HeaderHolder extends RecyclerView.ViewHolder {
        EditText nameEdit;

        public HeaderHolder(@NonNull View itemView) {
            super(itemView);
            nameEdit = itemView.findViewById(R.id.addCourse_name_edit);
        }
    }

    private class ContentHolder extends RecyclerView.ViewHolder {
        TextView dateTv;
        TextView timeTv;
        EditText classroomEdit;
        EditText teacherEdit;
        Button button;

        public ContentHolder(@NonNull View itemView) {
            super(itemView);
            dateTv=itemView.findViewById(R.id.addCourse_date_Tv);
            timeTv=itemView.findViewById(R.id.addCourse_time_Tv);
            classroomEdit = itemView.findViewById(R.id.addCourse_classroom_edit);
            teacherEdit = itemView.findViewById(R.id.addCourse_teacher_edit);
            button=itemView.findViewById(R.id.addCourse_Btn);
        }
    }

    private class BottomHolder extends RecyclerView.ViewHolder {
        Button button;

        public BottomHolder(@NonNull View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.addCourse_addTime_btn);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return TYPE_HEADER;
        else if (position == getItemCount() - 1)
            return TYPE_BOTTOM;
        else return TYPE_CONTENT;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case TYPE_HEADER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_addcourse_header, parent, false);
                return new HeaderHolder(view);
            case TYPE_BOTTOM:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_addcourse_bottom, parent, false);
                return new BottomHolder(view);
            case TYPE_CONTENT:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_addcourse_content, parent, false);
                return new ContentHolder(view);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof HeaderHolder) {
            ((HeaderHolder) holder).nameEdit.setText(list.get(0).getName());
            ((HeaderHolder) holder).nameEdit.addTextChangedListener(HeaderTextWatcher);
        } else if (holder instanceof ContentHolder) {
            final CourseBean courseBean = list.get(position);
            if(courseBean.getStart_week()>0)
                 ((ContentHolder) holder).dateTv.setText("第"+weeks[courseBean.getStart_week()]+'-'+weeks[courseBean.getEnd_week()]+"周");
            else
                ((ContentHolder) holder).dateTv.setText("自动");
            ((ContentHolder) holder).classroomEdit.setText(courseBean.getClassroom());
            ((ContentHolder) holder).teacherEdit.setText(courseBean.getTeacher());
            ((ContentHolder) holder).timeTv.setText(dayOfWeeks[courseBean.getDay_of_week() - 1]+" 第"+
                    courseBean.getStart_course()+'-'+courseBean.getEnd_course()+"节");

            ((ContentHolder) holder).classroomEdit.addTextChangedListener(new TimeTextWatcher(position, 1));
            ((ContentHolder) holder).teacherEdit.addTextChangedListener(new TimeTextWatcher(position, 2));

            ((ContentHolder) holder).button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onContentButtonClickListener!=null)
                        onContentButtonClickListener.onClick(position);
                }
            });

            ((ContentHolder) holder).dateTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final DateSelector dateSelector = new DateSelector(context);
                    if(courseBean.getStart_week()>0&&courseBean.getEnd_week()>=0)
                        dateSelector.setSelect(courseBean.getStart_week(),courseBean.getEnd_week());
                    DIYDialog.Builder builder = new DIYDialog.Builder(context, dateSelector.getView());
                    builder.setContentHeight(750);
                    builder.setContentWidth(600);
                    builder.setRightButtonOnClickListener(new DIYDialog.Builder.onButtonClickListener() {
                        @Override
                        public void onClick() {
                            int startWeek = dateSelector.getStartWeek();
                            int endWeek = dateSelector.getEndWeek();
                            list.get(position).setStart_week(startWeek);
                            list.get(position).setEnd_week(endWeek);
                            if(courseBean.getStart_week()>0)
                                ((ContentHolder) holder).dateTv.setText("第"+weeks[courseBean.getStart_week()]+'-'+weeks[courseBean.getEnd_week()]+"周");
                            else
                                ((ContentHolder) holder).dateTv.setText("自动");                        }
                    });
                    DIYDialog diyDialog = builder.create();
                    diyDialog.show();
                }
            });
            ((ContentHolder) holder).timeTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final TimeSelector timeSelector = new TimeSelector(context);
                    timeSelector.setSelect(courseBean.getDay_of_week(),courseBean.getStart_course(),courseBean.getEnd_course());
                    DIYDialog.Builder builder = new DIYDialog.Builder(context, timeSelector.getView());
                    builder.setContentHeight(750);
                    builder.setContentWidth(600);
                    builder.setRightButtonOnClickListener(new DIYDialog.Builder.onButtonClickListener() {
                        @Override
                        public void onClick() {
                            int startCourse = timeSelector.getStartCourse();
                            int endCourse = timeSelector.getEndCourse();
                            int dayOfWeek = timeSelector.getDayOfWeek();
                            list.get(position).setStart_course(startCourse);
                            list.get(position).setEnd_course(endCourse);
                            list.get(position).setDay_of_week(dayOfWeek);
                            ((ContentHolder) holder).timeTv.setText(dayOfWeeks[courseBean.getDay_of_week() - 1]+" 第"+
                                    courseBean.getStart_course()+'-'+courseBean.getEnd_course()+"节");
                        }
                    });
                    DIYDialog diyDialog = builder.create();
                    diyDialog.show();
                }
            });

        } else if (holder instanceof BottomHolder) {
            if (onBottomButtonClickListener != null)
                ((BottomHolder) holder).button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onBottomButtonClickListener.onClick();
                    }
                });
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    private class TimeTextWatcher implements TextWatcher {

        int position;
        int which;

        public TimeTextWatcher(int position, int which) {
            this.position = position;
            this.which = which;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.length() > 0) {
                switch (which) {
                    case 1:
                        list.get(position).setClassroom(editable.toString());
                        break;
                    case 2:
                        list.get(position).setTeacher(editable.toString());
                        break;
                }
            }


        }
    }

    private TextWatcher HeaderTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            list.get(0).setName(editable.toString());
        }
    };


    public interface OnBottomButtonClickListener {
        void onClick();
    }

    public interface OnContentButtonClickListener {
        void onClick(int position);
    }
}
