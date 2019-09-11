package com.example.imuhelper.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.imuhelper.R
import com.example.imuhelper.activities.LogIn
import com.example.imuhelper.utils.*
import okhttp3.Response
import org.json.JSONObject

class SearchFragment : Fragment() {

    private lateinit var timeTv: TextView
    private lateinit var resultTv: TextView
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var timeRadioGroup: RadioGroup
    private lateinit var placeRadioGroup: RadioGroup
    private var dayPlus = 0
    private var placeIndex = 0
    private var startCourse = 1
    private var endCourse = 1
    private val placeList = arrayOf("01_001","01_002","01_003")
    private val handler = Handler()
    private lateinit var button:Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_search, container, false)
        init(view)
        return view
    }

    fun init(view: View) {
        timeTv = view.findViewById(R.id.search_time_Tv)
        resultTv = view.findViewById(R.id.search_result_Tv)
        toolbar = view.findViewById(R.id.search_toolbar)
        button = view.findViewById(R.id.searchToolbar_commit_menu)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        toolbar.title = ""
        timeRadioGroup = view.findViewById(R.id.search_time_radioGroup)
        placeRadioGroup = view.findViewById(R.id.search_place_radioGroup)
        timeTv.text = "第1节 至 第1节"
        timeTv.setOnClickListener {
            handler.post{
                val courseSelector = CourseSelector(context!!)
                val views = courseSelector.getView()
                val builder = DIYDialog.Builder(context!!,views)
                builder.setContentHeight(600)
                builder.setContentWidth(720)
                builder.setLeftButtonText("取消")
                builder.setRightButtonText("确认")
                builder.setRightButtonOnClickListener {
                    startCourse = courseSelector.getStartCourse()
                    endCourse = courseSelector.getEndCourse()
                    timeTv.text = "第${startCourse}节 至 第${endCourse}节"
                }
                val dialog = builder.create()
                dialog.show()
            }
        }
        timeRadioGroup.setOnCheckedChangeListener { radioGroup, i ->
            when (i) {
                R.id.search_today_radio -> {
                    dayPlus = 0
                }
                R.id.search_tomorrow_radio -> {
                    dayPlus = 1
                }
                R.id.search_dayAfter_radio -> {
                    dayPlus = 2
                }
            }
        }
        placeRadioGroup.setOnCheckedChangeListener { radioGroup, i ->
            when(i){
                R.id.search_zhuLou_radio -> {
                    placeIndex = 0
                }
                R.id.search_zongHeLou_radio -> {
                    placeIndex = 1
                }
                R.id.search_yanJiuShengLou_radio -> {
                    placeIndex = 2
                }
            }
        }

        button.setOnClickListener {
            Thread(Runnable {
                Init(context!!, object : OnResponse {
                    override fun onResponseOK(response: Response) {
                        when (response.message) {
                            "OK" -> {
                                getClassroom()
                            }
                            "Found" -> {
                                val intent = Intent(activity,LogIn::class.java)
                                activity?.startActivityForResult(intent,IntentTool.Search_To_LogIn)
                            }
                            else -> {

                            }
                        }
                    }

                    override fun onResponseFalse() {
                    }
                })
            }).start()
        }

    }



    fun getClassroom(){
        Thread(Runnable {
            simulation(object :OnResponse{
                override fun onResponseOK(response: Response) {
                    searchClassroom(object :OnResponse{
                        override fun onResponseOK(response: Response) {
                            val responseObject = com.alibaba.fastjson.JSONObject.parseObject(response.body!!.string())
                            val spareroomObjList = responseObject.getJSONArray("spareroomObjList")
                            val claroom = spareroomObjList.getJSONObject(0).getJSONArray("claroom")
                            val roomList = ArrayList<String>()
                            for(i in 0 until claroom.size){
                                roomList.add(claroom.getJSONObject(i).getString("classroom"))
                            }
                            val stringBuffer = StringBuffer()
                            for(i in roomList)
                                stringBuffer.append("$i ")
                            handler.post {
                                resultTv.text = stringBuffer.toString()
                            }
                        }

                        override fun onResponseFalse() {
                        }
                    },placeList[placeIndex],startCourse,endCourse,dayPlus)
                }

                override fun onResponseFalse() {
                }
            })
        }).start()
    }

}