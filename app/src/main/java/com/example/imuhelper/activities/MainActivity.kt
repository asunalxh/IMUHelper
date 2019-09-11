package com.example.imuhelper.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.FrameLayout
import com.example.imuhelper.R
import com.example.imuhelper.db.CourseDBHelper
import com.example.imuhelper.fragment.HomeFragment
import com.example.imuhelper.fragment.MineFragment
import com.example.imuhelper.fragment.SearchFragment
import com.example.imuhelper.utils.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import okhttp3.Response
import kotlin.math.min

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNv: BottomNavigationView
    private lateinit var homeFrag: HomeFragment
    private lateinit var searchFrag: SearchFragment
    private lateinit var mineFrag: MineFragment
    private var position = 0
    private val handler = Handler()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
    }

    private fun init() {

        homeFrag = HomeFragment()
        searchFrag = SearchFragment()
        mineFrag = MineFragment()
        bottomNv = findViewById(R.id.main_bottom_Nv)

        var transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.main_context_Frag, homeFrag)
            .add(R.id.main_context_Frag, searchFrag)
            .add(R.id.main_context_Frag, mineFrag)
            .hide(searchFrag)
            .hide(mineFrag)
            .show(homeFrag)
            .commit()
//        transaction.add(R.id.main_context_Frag,homeFrag)
//            .commit()

        bottomNv.setOnNavigationItemSelectedListener {
            var transaction = supportFragmentManager.beginTransaction()
            when (it.itemId) {
                R.id.navigation_home -> {
                    if (position != 0) {
                        transaction
                            .hide(searchFrag)
                            .hide(mineFrag)
                            .show(homeFrag)
                            .commit()
                        position = 0
                    }
//                    transaction.replace(R.id.main_context_Frag,homeFrag).commit()
                }
                R.id.navigation_search -> {
                    if (position != 1) {
                        transaction
                            .show(searchFrag)
                            .hide(mineFrag)
                            .hide(homeFrag)
                            .commit()
                        position = 1
                    }
//                    transaction.replace(R.id.main_context_Frag,searchFrag).commit()
                }
                R.id.navigation_mine -> {
                    if (position != 2) {
                        transaction
                            .hide(searchFrag)
                            .show(mineFrag)
                            .hide(homeFrag)
                            .commit()
                        position = 2
                    }
//                    transaction.replace(R.id.main_context_Frag,mineFrag).commit()
                }
            }
            return@setOnNavigationItemSelectedListener true
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {

            IntentTool.Main_To_IMU -> if (resultCode == IntentTool.RESULT_OK) {
                Thread(Runnable {
                    val progressDialog = ProgressDialog.Builder(this,"正在下载").create()
                    handler.post{
                        progressDialog.show()
                    }
                    simulation(object : OnResponse {
                        override fun onResponseOK(response: Response) {
                            getCourse(object : OnResponse {
                                override fun onResponseOK(response: Response) {
                                    var courseList = getCourseList(response.body!!.string())
                                    var courseDBHelper = CourseDBHelper(
                                        baseContext,
                                        TermTool.getDBName(baseContext),
                                        null
                                    )
                                    courseDBHelper.clear()
                                    courseDBHelper.insert(courseList)
                                    handler.post{
                                        progressDialog.hide()
                                    }
                                    homeFrag.reFresh()

                                }

                                override fun onResponseFalse() {

                                }
                            })
                        }

                        override fun onResponseFalse() {
                        }
                    })
                    handler.post{
                        progressDialog.hide()
                    }
                }).start()
            }

            IntentTool.Search_To_LogIn -> if (resultCode == IntentTool.RESULT_OK) {
                Thread(Runnable {
                    val progressDialog = ProgressDialog.Builder(this,"正在加载").create()
                    handler.post {
                        progressDialog.show()
                    }
                    searchFrag.getClassroom()
                    handler.post{
                        progressDialog.hide()
                    }
                }).start()
            }

            //添加课程
            IntentTool.TimePager_To_AddCourse -> if (resultCode == IntentTool.RESULT_OK) {
                homeFrag.reFresh()
            }

            //无学期变有学期
            IntentTool.NoneTerm_To_AddTerm -> if (resultCode == IntentTool.RESULT_OK) {
                homeFrag.reFresh()
            }

            //课程编辑
            IntentTool.CourseCard_To_AddCourse -> if (resultCode == IntentTool.RESULT_OK)
                homeFrag.reFresh()

            //下载课程
            IntentTool.Main_To_IMU -> if (resultCode == IntentTool.RESULT_OK)
                homeFrag.reFresh()

            IntentTool.Main_To_TermList -> if (resultCode == IntentTool.RESULT_OK) {
                homeFrag.reFresh()
            }
        }
    }

}
