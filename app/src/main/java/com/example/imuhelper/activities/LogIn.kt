package com.example.imuhelper.activities

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.alibaba.fastjson.JSONObject
import com.example.imuhelper.R
import com.example.imuhelper.bean.CourseBean
import com.example.imuhelper.bean.ScoreBean
import com.example.imuhelper.utils.*
import okhttp3.Response

class LogIn : AppCompatActivity() {

    private lateinit var accountEdit:EditText
    private lateinit var passwordEdit:EditText
    private lateinit var codeEdit : EditText
    private lateinit var codeIv:ImageView
    private lateinit var button:Button
    private val handler= Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)
        accountEdit=findViewById(R.id.imu_username_edit)
        passwordEdit=findViewById(R.id.imu_password_edit)
        codeEdit=findViewById(R.id.imu_code_edit)
        codeIv=findViewById(R.id.imu_Iv)
        button=findViewById(R.id.imu_logIn_btn)

        logIn(object :OnResponse{
            override fun onResponseOK(response: Response) {
                getCodeImage(onGetImgResponse())
            }

            override fun onResponseFalse() {

            }
        })

        codeIv.setOnClickListener {
            handler.post{
                getCodeImage(onGetImgResponse())
            }
        }

        button.setOnClickListener {
            SecurityCheck(accountEdit.text.toString(),passwordEdit.text.toString(),codeEdit.text.toString(),
                object:OnResponse{
                    override fun onResponseFalse() {
                    }

                    override fun onResponseOK(response: Response) {
                        when(response.message)
                        {
                            "OK" -> {
                                setResult(IntentTool.RESULT_OK)
                                finish()
                            }
                            else ->{
                                handler.post{
                                    Toast.makeText(baseContext,"登陆错误",Toast.LENGTH_SHORT).show()
                                    getCodeImage(onGetImgResponse())
                                }
                            }
                        }
                    }
                })
        }

    }

    inner class onGetImgResponse : OnResponse {
        override fun onResponseOK(response: Response) {
            var inputStream = response.body?.byteStream()
            var bitmap = BitmapFactory.decodeStream(inputStream)
            handler.post { codeIv.setImageBitmap(bitmap) }
        }

        override fun onResponseFalse() {

        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        setResult(IntentTool.RESULT_FALSE)
        finish()
    }
}
