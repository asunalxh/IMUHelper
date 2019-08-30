package com.example.imuhelper.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.timetable.R;

public class ProgressDialog extends Dialog {

    public static class Builder{
        private Context context;
        private String contentText;

        public Builder(Context context){
            this.context=context;
        }

        public void setText(String text){
            this.contentText=text;
        }

        public ProgressDialog create(){
            LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view=inflater.inflate(R.layout.item_progress_dialog,null);

            TextView textView=view.findViewById(R.id.progressDialog_content_Tv);
            textView.setText(contentText);

            ProgressBar progressBar=view.findViewById(R.id.progressDialog_progressBar);

            ProgressDialog dialog=new ProgressDialog(context);

            dialog.addContentView(view,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            return dialog;
        }
    }

    public ProgressDialog(Context context) {
        super(context);
    }

    public ProgressDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected ProgressDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setCanceledOnTouchOutside(false);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onBackPressed() {

    }
}
