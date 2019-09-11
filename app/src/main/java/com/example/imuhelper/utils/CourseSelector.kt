package com.example.imuhelper.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.asunalxh.wheelview.WheelView
import com.example.imuhelper.R

class CourseSelector(context: Context) {

    private val list = arrayOf("第1节","第2节","第3节","第4节","第5节","第6节","第7节","第8节","第9节","第10节","第11节","第12节","第13节","第14节","第15节","第16节","第17节","第18节","第19节","第20节")
    private val wheelview = arrayOfNulls<WheelView>(2)
    private var view: View

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        view = inflater.inflate(R.layout.item_twoselector, null)
        wheelview[0] = view.findViewById(R.id.twoSelector_First)
        wheelview[1] = view.findViewById(R.id.twoSelector_Second)

        wheelview[0]!!.setList(list)
        wheelview[1]!!.setList(list)

        for(wheelView in wheelview){
            wheelView?.setTextSize(60)
            wheelView?.setTextColor(-0xcccccd)
            wheelView?.setSelectBackgroundColor(-0x6a0f1b)

        }


        wheelview[0]?.setOnSelectChangeListener {
            wheelview[1]?.setMinSelect(it)
        }
    }

    fun getStartCourse():Int{
        return wheelview[0]!!.selectIndex+1
    }

    fun getStartCourseString():String{
        return wheelview[0]!!.selectString
    }

    fun getEndCourse():Int{
        return wheelview[1]!!.selectIndex+1
    }

    fun getEndCourseString():String{
        return wheelview[1]!!.selectString
    }

    fun getView():View{
        return view
    }
}