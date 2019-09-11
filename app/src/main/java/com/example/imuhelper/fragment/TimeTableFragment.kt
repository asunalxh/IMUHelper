package com.example.imuhelper.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.imuhelper.bean.CourseBean
import com.example.imuhelper.db.CourseDBHelper
import com.example.imuhelper.utils.TermTool
import com.example.imuhelper.R
import com.example.imuhelper.activities.AddCourseActivity
import com.example.imuhelper.utils.IntentTool


class TimeTableFragment(week:Int) : Fragment() {

    private lateinit var frameArray:Array<FrameLayout>
    private lateinit var linearLayout: LinearLayout
    private val height = 200
    private var week:Int = week
    private val handler = Handler()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_timetable, container, false)
        initView(view)
        return view
    }


    private fun initView(view: View) {
        linearLayout = view.findViewById(R.id.timeTable_courseNumber_layout)
        frameArray = arrayOf(
            view.findViewById(R.id.timeTable_monday_layout) ,
            view.findViewById(R.id.timeTable_tuesday_layout) ,
            view.findViewById(R.id.timeTable_wednesday_layout),
            view.findViewById(R.id.timeTable_thursday_layout),
            view.findViewById(R.id.timeTable_friday_layout),
            view.findViewById(R.id.timeTable_saturday_layout),
            view.findViewById(R.id.timeTable_sunday_layout)
        )
        loadCourse()
    }

    fun loadCourse() {
        val courseNum = TermTool.getCourseNumber()
        for (i in 1..courseNum) {
            val view = layoutInflater.inflate(R.layout.item_number_card, null)
            view.findViewById<TextView>(R.id.numberCard_Tv).text = i.toString()
            val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,height)
            view.layoutParams = params
            linearLayout.addView(view)
        }

        var list = CourseDBHelper(context,TermTool.getDBName(context),null).getCourse(week)
        for(course in list){
            insertCard(course)
        }
    }

    private fun insertCard(courseBean: CourseBean){
        val view = layoutInflater.inflate(R.layout.item_course_card,null)
        val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,height*(courseBean.end_course-courseBean.start_course+1))
        view.y = ((courseBean.start_course-1)*height).toFloat()
        view.layoutParams = params
        view.findViewById<TextView>(R.id.courseCard_text).text = "${courseBean.name}\n${courseBean.teacher}\n${courseBean.classroom}"
        view.setOnLongClickListener {
            val startCourse = getStartCourse(it)
            val dayOfWeek = getDayOfWeek(it)

            val intent = Intent(activity, AddCourseActivity::class.java)
            intent.putExtra("requestCode",IntentTool.CourseCard_To_AddCourse)

            val courseDBHelper = CourseDBHelper(context,TermTool.getDBName(context),null)
            intent.putExtra("name",courseDBHelper.getCourse(week,dayOfWeek,startCourse).name)

            activity!!.startActivityForResult(intent,IntentTool.TimePager_To_AddCourse)

            true
        }
        frameArray[courseBean.day_of_week-1].addView(view)
    }



    fun getDayOfWeek(view:View):Int{
        val frameLayout = view.parent
        for ((i,layout) in frameArray.withIndex()) {
            if(frameLayout == layout){
                return i + 1
            }
        }
        return 0
    }

    fun getStartCourse(view: View):Int{
        return (view.y/height).toInt()+1
    }

}