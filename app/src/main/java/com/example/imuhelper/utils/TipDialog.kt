package com.example.imuhelper.utils

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.example.imuhelper.R

class TipDialog : Dialog {


    constructor(context: Context) : super(context) {}

    constructor(context: Context, themeResId: Int) : super(context, themeResId) {}

    protected constructor(
        context: Context,
        cancelable: Boolean,
        cancelListener: DialogInterface.OnCancelListener?
    ) : super(context, cancelable, cancelListener) {
    }


    internal class Builder(context: Context,contentText:String = ""){
        private var context = context
        private var contentText = contentText

        fun create(): TipDialog{
            val tipDialog = TipDialog(context, R.style.BaseDialog)

            val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            val view = layoutInflater.inflate(R.layout.item_tip_dialog,null)
            view.findViewById<TextView>(R.id.tipDialog_content_Tv).text = contentText
            tipDialog.addContentView(view, ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT))

            return tipDialog
        }

    }
}