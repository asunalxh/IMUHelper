package com.example.imuhelper.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.imuhelper.R
import com.example.imuhelper.activities.AddCourseActivity
import com.example.imuhelper.activities.LogIn
import com.example.imuhelper.activities.TermListActivity
import com.example.imuhelper.db.CourseDBHelper
import com.example.imuhelper.db.TermDBHelper
import com.example.imuhelper.utils.*
import okhttp3.Response

class HomeFragment : Fragment() {

    private lateinit var tablePagerFragment: TablePagerFragment
    private lateinit var noneTermFragment: NoneTermFragment
    private lateinit var toolbar: Toolbar
    private var isTimetable = false
    private val list = arrayOf(
        "第1周",
        "第2周",
        "第3周",
        "第4周",
        "第5周",
        "第6周",
        "第7周",
        "第8周",
        "第9周",
        "第10周",
        "第11周",
        "第12周",
        "第13周",
        "第14周",
        "第15周",
        "第16周",
        "第17周",
        "第18周",
        "第19周",
        "第20周"
    )
    private var todayPosition = -1
    private val handler = Handler()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        var view = layoutInflater.inflate(R.layout.fragment_home, container, false)
        toolbar = view.findViewById(R.id.home_toolbar)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        toolbar.title = ""
        initView()
        return view
    }


    private fun initView() {
        //预加载
        tablePagerFragment = TablePagerFragment()
        noneTermFragment = NoneTermFragment()
        tablePagerFragment.onPageChangeFunction = object : TablePagerFragment.OnPageChangeFunction {
            override fun onPageChange(position: Int) {
                handler.post {
                    if (position == todayPosition)
                        toolbar.title = "课程表 - ${list[position]}(本周)"
                    else toolbar.title = "课程表 - ${list[position]}"
                }
            }
        }
        val transaction = childFragmentManager.beginTransaction()
        transaction.add(R.id.main_content_layout, tablePagerFragment)
            .add(R.id.main_content_layout, noneTermFragment)


        //如果有学期信息
        if (TermTool.init(context)) {
            isTimetable = true
            (activity as AppCompatActivity).invalidateOptionsMenu()
            transaction.hide(noneTermFragment)
                .commit()
            val today = CalendarHelper().date
            val term = TermDBHelper(context, null).getTerm(TermTool.getSelectedId())
            if (today.compareTo(term) >= 0 && today.getWeekNum(term) < TermTool.getTermLength()) {
                todayPosition = today.getWeekNum(term)
            }

            tablePagerFragment.loadTimeTable()
        } /*没有学期信息*/
        else {
            isTimetable = false
            transaction.hide(tablePagerFragment)
                .commit()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_home_toolber, menu)
    }


    override fun onPrepareOptionsMenu(menu: Menu) {
        if (!isTimetable) {
            menu.findItem(R.id.homeToolbar_addCourse_menu).isVisible = false
            menu.findItem(R.id.homeToolbar_download_menu).isVisible = false
            menu.findItem(R.id.homeToolbar_termList_menu).isVisible = false
        } else {
            menu.findItem(R.id.homeToolbar_addCourse_menu).isVisible = true
            menu.findItem(R.id.homeToolbar_download_menu).isVisible = true
            menu.findItem(R.id.homeToolbar_termList_menu).isVisible = true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            //添加课程
            R.id.homeToolbar_addCourse_menu -> {
                val intent = Intent(context, AddCourseActivity::class.java)
                intent.putExtra("requestCode", IntentTool.TimePager_To_AddCourse)
                activity?.startActivityForResult(intent, IntentTool.TimePager_To_AddCourse)
            }
            //下载课程表
            R.id.homeToolbar_download_menu -> {
                val builder = MessageDialog.Builder(context, "当前课程表将会被覆盖", "确定", "取消")
                builder.setRightButtonOnClickListener {
                    context?.let {

                        Thread(Runnable {
                            Init(it, object : OnResponse {
                                override fun onResponseOK(response: Response) {
                                    when (response.message) {
                                        "OK" -> {
                                            val progressDialog =
                                                ProgressDialog.Builder(context, "正在下载").create()
                                            handler.post {
                                                progressDialog.show()
                                            }

                                            simulation(object : OnResponse {
                                                override fun onResponseOK(response: Response) {
                                                    getCourse(object : OnResponse {
                                                        override fun onResponseOK(response: Response) {
                                                            val courseList =
                                                                getCourseList(response.body!!.string())
                                                            val courseDBHelper = CourseDBHelper(
                                                                it,
                                                                TermTool.getDBName(it),
                                                                null
                                                            )
                                                            courseDBHelper.clear()
                                                            courseDBHelper.insert(courseList)
                                                            reFresh()
                                                            handler.post {
                                                                progressDialog.hide()
                                                            }
                                                        }

                                                        override fun onResponseFalse() {
                                                        }
                                                    })
                                                }

                                                override fun onResponseFalse() {
                                                }
                                            })
                                        }
                                        "Found" -> {
                                            var intent = Intent(activity, LogIn::class.java)
                                            activity?.startActivityForResult(
                                                intent,
                                                IntentTool.Main_To_IMU
                                            )
                                        }
                                    }
                                }

                                override fun onResponseFalse() {
                                    handler.post {
                                        Toast.makeText(context,"连接错误",Toast.LENGTH_SHORT).show()
                                    }
                                }
                            })
                        }).start()


                    }
                }
                builder.create().show()
            }

            //学期列表
            R.id.homeToolbar_termList_menu -> {
                val intent2 = Intent(context, TermListActivity::class.java)
                activity?.startActivityForResult(intent2, IntentTool.Main_To_TermList)
            }

        }
        return true
    }

    fun reFresh() {


        todayPosition = -1
        val today = CalendarHelper().date
        val term = TermDBHelper(context, null).getTerm(TermTool.getSelectedId())
        if (today.compareTo(term) >= 0 && today.getWeekNum(term) < TermTool.getTermLength()) {
            todayPosition = today.getWeekNum(term)
        }
        //如果有学期信息
        if (TermTool.init(context)) {
            isTimetable = true
            (activity as AppCompatActivity).invalidateOptionsMenu()
            childFragmentManager.beginTransaction().hide(noneTermFragment)
                .show(tablePagerFragment)
                .commit()
            val today = CalendarHelper().date
            val term = TermDBHelper(context, null).getTerm(TermTool.getSelectedId())
            if (today.compareTo(term) >= 0 && today.getWeekNum(term) < TermTool.getTermLength())
                todayPosition = today.getWeekNum(term)
            tablePagerFragment.fresh()

        } /*没有学期信息*/
        else {
            isTimetable = false
            childFragmentManager.beginTransaction().hide(tablePagerFragment)
                .show(noneTermFragment)
                .commit()
        }
    }


}