package com.example.imuhelper.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.timetable.R;

public class DIYDialog extends Dialog {

    public static class Builder {

        private Context context;
        private View contentView;
        private String leftButtonText;
        private String rightButtonText;
        private onButtonClickListener rightListener;
        private onButtonClickListener leftListener;
        private int contentHeight = 0;
        private int contentWidth = 0;


        public void setRightButtonOnClickListener(onButtonClickListener bottonOnclickListener) {
            this.rightListener = bottonOnclickListener;
        }

        public void setLeftButtonOnCLickListener(onButtonClickListener buttonClickListener) {
            this.leftListener = buttonClickListener;
        }

        public Builder(Context context, View contentView) {
            this.context = context;
            this.contentView = contentView;
        }

        Builder(Context context, View contentView, String rightButtonText, String leftButtonText) {
            this.context = context;
            this.contentView = contentView;
            this.rightButtonText = rightButtonText;
            this.leftButtonText = leftButtonText;
        }

        public DIYDialog create() {
            final DIYDialog dialog = new DIYDialog(context, R.style.BaseDialog);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.item_diydialog, null);

            Button rightButton = view.findViewById(R.id.diyDialog_right_btn);
            Button leftButton = view.findViewById(R.id.diyDialog_left_btn);
            rightButton.setText(rightButtonText);
            leftButton.setText(leftButtonText);

            rightButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (rightListener != null)
                        rightListener.onClick();
                    dialog.dismiss();
                }
            });
            leftButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (leftListener != null)
                        leftListener.onClick();
                    dialog.dismiss();
                }
            });
            LinearLayout.LayoutParams params;
            if (contentWidth == 0 || contentHeight == 0)
                params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            else params = new LinearLayout.LayoutParams(contentWidth, contentHeight);
            LinearLayout content = view.findViewById(R.id.diyDialog_content_layout);
            content.setLayoutParams(params);
            content.addView(contentView);

            dialog.addContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            return dialog;
        }

        public void setContentHeight(int contentHeight) {
            this.contentHeight = contentHeight;
        }

        public void setContentWidth(int contentWidth) {
            this.contentWidth = contentWidth;
        }

        public interface onButtonClickListener {
            void onClick();
        }
    }


    public DIYDialog(Context context, int themeResId) {
        super(context, themeResId);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setCanceledOnTouchOutside(true);//点击空白地方关闭
    }


}
