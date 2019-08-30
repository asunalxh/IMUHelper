package com.example.imuhelper.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.timetable.R;

public class MessageDialog extends Dialog {


    public static class Builder {

        private Context context;
        private String contentText;
        private String leftButtonText;
        private String rightButtonText;
        private DIYDialog.Builder.onButtonClickListener rightListener;
        private DIYDialog.Builder.onButtonClickListener leftListener;
        private int contentHeight = 0;
        private int contentWidth = 0;


        public void setRightButtonOnClickListener(DIYDialog.Builder.onButtonClickListener bottonOnclickListener) {
            this.rightListener = bottonOnclickListener;
        }

        public void setLeftButtonOnCLickListener(DIYDialog.Builder.onButtonClickListener buttonClickListener) {
            this.leftListener = buttonClickListener;
        }

        public Builder(Context context) {
            this.context = context;
        }

        public Builder(Context context,String contentText, String rightButtonText, String leftButtonText) {
            this.context = context;
            this.contentText=contentText;
            this.rightButtonText = rightButtonText;
            this.leftButtonText = leftButtonText;
        }

        public MessageDialog create() {
            final MessageDialog dialog = new MessageDialog(context, R.style.BaseDialog);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.item_messagedialog, null);

            Button rightButton = view.findViewById(R.id.messageDialog_right_btn);
            Button leftButton = view.findViewById(R.id.messageDialog_left_btn);
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

            ((TextView)view.findViewById(R.id.messageDialog_content_Tv)).setText(contentText);

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

    public MessageDialog(Context context) {
        super(context);
    }

    public MessageDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected MessageDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setCanceledOnTouchOutside(true);//点击空白地方关闭
    }

}
