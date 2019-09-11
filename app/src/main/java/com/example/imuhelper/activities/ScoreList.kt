package com.example.imuhelper.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imuhelper.R
import com.example.imuhelper.adapter.ScoreListAdapter
import com.example.imuhelper.bean.ScoreBean
import com.example.imuhelper.utils.*
import okhttp3.Response
import java.util.*
import kotlin.collections.ArrayList

class ScoreList : AppCompatActivity() {

    private var scoreList = ArrayList<ScoreBean>()
    private lateinit var adapter: ScoreListAdapter
    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score_list)


        val scoreListRv = findViewById<RecyclerView>(R.id.scoreList_Rv)
        adapter = ScoreListAdapter(scoreList)
        scoreListRv.adapter = adapter
        scoreListRv.layoutManager = LinearLayoutManager(this)

        Thread(Runnable {
            Init(this, object : OnResponse {
                override fun onResponseOK(response: Response) {
                    when (response.message) {
                        "OK" -> {
                            getScoreFun()
                        }
                        "Found" -> {
                            var intent = Intent(baseContext, LogIn::class.java)
                            startActivityForResult(intent, IntentTool.ScoreList_To_LogIn)
                        }
                    }
                }

                override fun onResponseFalse() {
                    handler.post {
                        Toast.makeText(baseContext,"连接错误", Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }).start()

    }


    private fun getScoreFun() {
        val progressDialog = ProgressDialog.Builder(this, "正在加载").create()


        simulation(object : OnResponse {
            override fun onResponseOK(response: Response) {
                handler.post{
                    progressDialog.show()
                }
                getScore(object : OnResponse {
                    override fun onResponseOK(response: Response) {
                        var list = getScoreList(response.body!!.string())
                        handler.post {
                            for ((i, score) in list.withIndex()) {
                                scoreList.add(i, score)
                                adapter.notifyItemInserted(i)
                            }
                            adapter.notifyDataSetChanged()
                            progressDialog.hide()
                        }
                    }

                    override fun onResponseFalse() {
                    }
                })
            }

            override fun onResponseFalse() {
                handler.post {
                    Toast.makeText(baseContext,"连接错误", Toast.LENGTH_SHORT).show()
                }
            }
        })

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            IntentTool.ScoreList_To_LogIn -> if (resultCode == IntentTool.RESULT_OK) {
                getScoreFun()
            } else {
                this.finish()
            }
        }
    }


}
