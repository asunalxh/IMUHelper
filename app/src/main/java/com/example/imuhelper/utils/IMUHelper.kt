package com.example.imuhelper.utils

import android.content.Context
import android.util.Log
import com.alibaba.fastjson.JSONObject
import com.example.imuhelper.bean.CourseBean
import com.example.imuhelper.bean.ScoreBean
import okhttp3.*
import java.io.IOException
import java.util.concurrent.TimeUnit

private var JSESSIONID: String = ""


interface OnResponse {
    fun onResponseOK(response: Response)
    fun onResponseFalse()
}

fun Init(context: Context, onResopnse: OnResponse? = null) {
    var sharedPreferences = context.getSharedPreferences("JSESSIONID", Context.MODE_PRIVATE)
    JSESSIONID = sharedPreferences.getString("JSESSIONID", "").toString()
    var client = OkHttpClient.Builder()
        .followRedirects(false)
        .build()
    var builder = Request.Builder()
        .url("http://jwxt.imu.edu.cn")
        .addHeader("Connection", "keep-alive")
        .addHeader("Upgrade-Insecure-Requests", "1")
        .addHeader(
            "Accept",
            "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3"
        )
        .addHeader("Accept-Language", "zh,zh-TW;q=0.9,en-US;q=0.8,en;q=0.7,zh-CN;q=0.6")

    if (JSESSIONID.isNotEmpty()) {
        builder.addHeader("Cookie", "JSESSIONID=${JSESSIONID}")
        var call = client.newCall(builder.build())
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                if (onResopnse != null)
                    onResopnse.onResponseFalse()
            }

            override fun onResponse(call: Call, response: Response) {
                if (onResopnse != null)
                    onResopnse.onResponseOK(response)
            }

        })

    } else {
        var call = client.newCall(builder.build())
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                if (onResopnse != null)
                    onResopnse.onResponseFalse()
            }

            override fun onResponse(call: Call, response: Response) {
                var str = response.header("Set-Cookie")
                JSESSIONID = str?.substring(11, 32) ?: ""
                var edit = context.getSharedPreferences("JSESSIONID", Context.MODE_PRIVATE).edit()
                edit.putString("JSESSIONID", JSESSIONID)
                edit.apply()
                Init(context, onResopnse)
            }
        })
    }
}

fun logIn(onResponse: OnResponse? = null) {
    var client = OkHttpClient.Builder().build()
    var request = Request.Builder()
        .url("http://jwxt.imu.edu.cn/login")
        .addHeader("Connection", "keep-alive")
        .addHeader("Cache-Control", "max-age=0")
        .addHeader("Upgrade-Insecure-Requests", "1")
        .addHeader(
            "Accept",
            "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3"
        )
        .addHeader("Referer", "http://jwxt.imu.edu.cn/")
        .addHeader("Accept-Language", "zh,zh-TW;q=0.9,en-US;q=0.8,en;q=0.7,zh-CN;q=0.6")
        .addHeader("Cookie", "JSESSIONID=${JSESSIONID}")
        .build()
    var call = client.newCall(request)
    call.enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            onResponse?.onResponseFalse()
        }

        override fun onResponse(call: Call, response: Response) {
            onResponse?.onResponseOK(response)
        }
    })
}


fun getCodeImage(onResponse: OnResponse? = null) {
    var client = OkHttpClient.Builder().build()
    var request = Request.Builder()
        .url("http://jwxt.imu.edu.cn/img/captcha.jpg")
        .addHeader("Connection", "keep-alive")
        .addHeader("Accept", "image/webp,image/apng,image/*,*/*;q=0.8")
        .addHeader("Purpose", "prefetch")
        .addHeader("Referer", "http://jwxt.imu.edu.cn/login")
        .addHeader("Accept-Language", "zh,zh-TW;q=0.9,en-US;q=0.8,en;q=0.7,zh-CN;q=0.6")
        .addHeader("Cookie", "JSESSIONID=${JSESSIONID}")
        .build()
    var call = client.newCall(request)
    call.enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            onResponse?.onResponseFalse()
        }

        override fun onResponse(call: Call, response: Response) {
            onResponse?.onResponseOK(response)
        }
    })
}

fun SecurityCheck(account: String, password: String, code: String, onResponse: OnResponse? = null) {
    var client = OkHttpClient.Builder()
        .build()
    var formBody = FormBody.Builder()
        .add("j_username", account)
        .add("j_password", password)
        .add("j_captcha", code)
        .build()
    var request = Request.Builder()
        .url("http://jwxt.imu.edu.cn/j_spring_security_check")
        .addHeader("Connection", "keep-alive")
        .addHeader("Cache-Control", "max-age=0")
        .addHeader("Origin", "http://jwxt.imu.edu.cn")
        .addHeader("Upgrade-Insecure-Requests", "1")
        .addHeader(
            "Accept",
            "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3"
        )
        .addHeader("Referer", "http://jwxt.imu.edu.cn/login?errorCode=badCredentials")
        .addHeader("Accept-Language", "zh,zh-TW;q=0.9,en-US;q=0.8,en;q=0.7,zh-CN;q=0.6")
        .addHeader("Cookie", "JSESSIONID=${JSESSIONID}")
        .post(formBody)
        .build()
    var call = client.newCall(request)
    call.enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            onResponse?.onResponseFalse()
        }

        override fun onResponse(call: Call, response: Response) {
            onResponse?.onResponseOK(response)
        }
    })
}

fun Index(onResponse: OnResponse? = null) {
    var client = OkHttpClient.Builder().build()
    var request = Request.Builder()
        .url("http://jwxt.imu.edu.cn/index.jsp")
        .addHeader("Connection", "keep-alive")
        .addHeader("Cache-Control", "max-age=0")
        .addHeader("Upgrade-Insecure-Requests", "1")
        .addHeader(
            "Accept",
            "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3"
        )
        .addHeader("Referer", "http://jwxt.imu.edu.cn/login?errorCode=badCredentials")
        .addHeader("Accept-Language", "zh,zh-TW;q=0.9,en-US;q=0.8,en;q=0.7,zh-CN;q=0.6")
        .addHeader("Cookie", "JSESSIONID=${JSESSIONID}")
        .build()
    var call = client.newCall(request)
    call.enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            onResponse?.onResponseFalse()
        }

        override fun onResponse(call: Call, response: Response) {
            simulation(onResponse)
        }
    })
}

fun simulation(onResopnse: OnResponse? = null) {
    var client = OkHttpClient.Builder()
        .connectTimeout(30,TimeUnit.SECONDS)
        .build()
    var formBody = FormBody.Builder().build()
    var request = Request.Builder()
        .url("http://jwxt.imu.edu.cn/main/checkSelectCourseStatus")
        .addHeader("Connection", "keep-alive")
        .addHeader("X-Requested-With", "XMLHttpRequest")
        .addHeader("Origin", "http://jwxt.imu.edu.cn")
        .addHeader("Referer", "http://jwxt.imu.edu.cn/index.jsp")
        .addHeader("Accept-Language", "zh,zh-TW;q=0.9,en-US;q=0.8,en;q=0.7,zh-CN;q=0.6")
        .addHeader("Cookie", "JSESSIONID=${JSESSIONID}")
        .post(formBody)
        .build()
    var call = client.newCall(request)
    call.enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            onResopnse?.onResponseFalse()
        }

        override fun onResponse(call: Call, response: Response) {
            simulation_2(onResopnse)
        }
    })

}

fun simulation_2(onResopnse: OnResponse? = null) {
    var client = OkHttpClient.Builder().build()
    var request = Request.Builder()
        .url("http://jwxt.imu.edu.cn/student/rollManagement/personalInfoUpdate/passdIsModified")
        .addHeader("Connection", "keep-alive")
        .addHeader("Accept", "application/json, text/javascript, */*; q=0.01")
        .addHeader("X-Requested-With", "XMLHttpRequest")
        .addHeader("Referer", "http://jwxt.imu.edu.cn/index.jsp")
        .addHeader("Accept-Language", "zh,zh-TW;q=0.9,en-US;q=0.8,en;q=0.7,zh-CN;q=0.6")
        .addHeader("Cookie", "JSESSIONID=${JSESSIONID}")
        .build()
    var call = client.newCall(request)
    call.enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {

        }

        override fun onResponse(call: Call, response: Response) {
            simulation_3(onResopnse)
        }
    })
}

fun simulation_3(onResopnse: OnResponse? = null) {
    var client = OkHttpClient.Builder().build()
    var request = Request.Builder()
        .url("http://jwxt.imu.edu.cn/main/showPyfaInfo")
        .addHeader("Connection", "keep-alive")
        .addHeader("Accept", "application/json, text/javascript, */*; q=0.01")
        .addHeader("X-Requested-With", "XMLHttpRequest")
        .addHeader("Referer", "http://jwxt.imu.edu.cn/index.jsp")
        .addHeader("Accept-Language", "zh,zh-TW;q=0.9,en-US;q=0.8,en;q=0.7,zh-CN;q=0.6")
        .addHeader("Cookie", "JSESSIONID=${JSESSIONID}")
        .build()
    var call = client.newCall(request)
    call.enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {

        }

        override fun onResponse(call: Call, response: Response) {
            simulation_4(onResopnse)
        }
    })
}

fun simulation_4(onResopnse: OnResponse? = null) {
    var client = OkHttpClient.Builder().build()
    var request = Request.Builder()
        .url("http://jwxt.imu.edu.cn/student/rollManagement/personalInfoUpdate/jhrInformationCollection")
        .addHeader("Connection", "keep-alive")
        .addHeader("Accept", "application/json, text/javascript, */*; q=0.01")
        .addHeader("X-Requested-With", "XMLHttpRequest")
        .addHeader("Referer", "http://jwxt.imu.edu.cn/index.jsp")
        .addHeader("Accept-Language", "zh,zh-TW;q=0.9,en-US;q=0.8,en;q=0.7,zh-CN;q=0.6")
        .addHeader("Cookie", "JSESSIONID=${JSESSIONID}")
        .build()
    var call = client.newCall(request)
    call.enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {

        }

        override fun onResponse(call: Call, response: Response) {
            simulation_5(onResopnse)
        }
    })
}

fun simulation_5(onResponse: OnResponse? = null) {
    var client = OkHttpClient.Builder().build()
    var request = Request.Builder()
        .url("http://jwxt.imu.edu.cn/main/showNotice")
        .addHeader("Connection", "keep-alive")
        .addHeader("Content-Length", "12")
        .addHeader("Accept", "application/json, text/javascript, */*; q=0.01")
        .addHeader("X-Requested-With", "XMLHttpRequest")
        .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
        .addHeader("Origin", "http://jwxt.imu.edu.cn")
        .addHeader("Referer", "http://jwxt.imu.edu.cn/index.jsp")
        .addHeader("Accept-Language", "zh,zh-TW;q=0.9,en-US;q=0.8,en;q=0.7,zh-CN;q=0.6")
        .addHeader("Cookie", "JSESSIONID=${JSESSIONID}")
        .post(FormBody.Builder().add("flag", "refresh").build())
        .build()
    var call = client.newCall(request)
    call.enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {

        }

        override fun onResponse(call: Call, response: Response) {
            onResponse?.onResponseOK(response)
        }
    })
}

fun getCourse(onResopnse: OnResponse? = null) {
    var client = OkHttpClient.Builder().build()
    var request = Request.Builder()
        .url("http://jwxt.imu.edu.cn/student/courseSelect/thisSemesterCurriculum/index")
        .addHeader("Connection", "keep-alive")
        .addHeader("Cache-Control", "max-age=0")
        .addHeader("Upgrade-Insecure-Requests:", "1")
        .addHeader(
            "Accept",
            "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3"
        )
        .addHeader("Referer", "http://jwxt.imu.edu.cn/index.jsp")
        .addHeader("Accept-Language", "zh,zh-TW;q=0.9,en-US;q=0.8,en;q=0.7,zh-CN;q=0.6")
        .addHeader("Cookie", "JSESSIONID=${JSESSIONID}; selectionBar=82022")
        .build()
    var call = client.newCall(request)
    call.enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {

        }

        override fun onResponse(call: Call, response: Response) {
            getCourse_2(onResopnse)
        }
    })
}

fun getCourse_2(onResopnse: OnResponse? = null) {
    var client = OkHttpClient.Builder().build()
    var request = Request.Builder()
        .url("http://jwxt.imu.edu.cn/static/template.css")
        .addHeader("Connection", "keep-alive")
        .addHeader("Accept", "text/css,*/*;q=0.1")
        .addHeader("Referer", "http://jwxt.imu.edu.cn/student/courseSelect/thisSemesterCurriculum/index")
        .addHeader("Accept-Language", "zh,zh-TW;q=0.9,en-US;q=0.8,en;q=0.7,zh-CN;q=0.6")
        .build()
    var call = client.newCall(request)
    call.enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {

        }

        override fun onResponse(call: Call, response: Response) {
            getCourse_3(onResopnse)
        }
    })

}

fun getCourse_3(onResopnse: OnResponse? = null) {
    var client = OkHttpClient.Builder().build()
    var request = Request.Builder()
        .url("http://jwxt.imu.edu.cn/static/wroResources?id=/WEB-INF/assets/font-awesome/4.1.0/fonts/fontawesome-webfont.woff?v=4.1.0")
        .addHeader("Connection", "keep-alive")
        .addHeader("Origin", "http://jwxt.imu.edu.cn")
        .addHeader("Accept", "*/*")
        .addHeader("Referer", "http://jwxt.imu.edu.cn/static/template.css")
        .addHeader("Accept-Language", "zh,zh-TW;q=0.9,en-US;q=0.8,en;q=0.7,zh-CN;q=0.6")
        .addHeader("Cookie", "JSESSIONID=${JSESSIONID}; selectionBar=82022")
        .build()
    var call = client.newCall(request)
    call.enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {

        }

        override fun onResponse(call: Call, response: Response) {
            getCourse_4(onResopnse)
        }
    })
}

fun getCourse_4(onResopnse: OnResponse? = null) {
    var client = OkHttpClient.Builder().build()
    var request = Request.Builder()
        .url("http://jwxt.imu.edu.cn/student/courseSelect/thisSemesterCurriculum/ajaxStudentSchedule/callback")
        .addHeader("Host", "jwxt.imu.edu.cn")
        .addHeader("Connection", "keep-alive")
        .addHeader("Accept", "*/*")
        .addHeader("X-Requested-With", "XMLHttpRequest")
        .addHeader("Referer", "http://jwxt.imu.edu.cn/student/courseSelect/thisSemesterCurriculum/index")
        .addHeader("Accept-Language", "zh,zh-TW;q=0.9,en-US;q=0.8,en;q=0.7,zh-CN;q=0.6")
        .addHeader("Cookie", "selectionBar=82022; JSESSIONID=${JSESSIONID}")
        .build()
    var call = client.newCall(request)
    call.enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {


        }

        override fun onResponse(call: Call, response: Response) {
            onResopnse?.onResponseOK(response)
            getCourse_5()
        }
    })
}

fun getCourse_5() {
    var client = OkHttpClient.Builder().build()
    var request = Request.Builder()
        .url("http://jwxt.imu.edu.cn/img/icon/favicon.ico")
        .addHeader("Connection", "keep-alive")
        .addHeader("Pragma", "no-cache")
        .addHeader("Cache-Control", "no-cache")
        .addHeader("Accept", "image/webp,image/apng,image/*,*/*;q=0.8")
        .addHeader("Referer", "http://jwxt.imu.edu.cn/student/courseSelect/thisSemesterCurriculum/index")
        .addHeader("Accept-Language", "zh,zh-TW;q=0.9,en-US;q=0.8,en;q=0.7,zh-CN;q=0.6")
        .addHeader("Cookie", "JSESSIONID=${JSESSIONID}; selectionBar=82022")
        .build()
    var call = client.newCall(request)
    call.enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {

        }

        override fun onResponse(call: Call, response: Response) {
            getCourse_6()
        }
    })
}

fun getCourse_6() {
    var client = OkHttpClient.Builder().build()
    var request = Request.Builder()
        .url("http://jwxt.imu.edu.cn/ajax/getSectionAndTime")
        .addHeader("Connection", "keep-alive")
        .addHeader("Content-Length", "16")
        .addHeader("Accept", "application/json, text/javascript, */*; q=0.01")
        .addHeader("X-Requested-With", "XMLHttpRequest")
        .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
        .addHeader("Origin", "http://jwxt.imu.edu.cn")
        .addHeader("Referer", "http://jwxt.imu.edu.cn/student/courseSelect/thisSemesterCurriculum/index")
        .addHeader("Accept-Language", "zh,zh-TW;q=0.9,en-US;q=0.8,en;q=0.7,zh-CN;q=0.6")
        .addHeader("Cookie", "JSESSIONID=${JSESSIONID}; selectionBar=82022")
        .post(
            FormBody.Builder()
                .add("planNumber", "")
                .add("ff", "f").build()
        )
        .build()
    var call = client.newCall(request)
    call.enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {

        }

        override fun onResponse(call: Call, response: Response) {

        }
    })
}

fun getScore(onResopnse: OnResponse? = null) {
    var client = OkHttpClient.Builder().build()
    var request = Request.Builder()
        .url("http://jwxt.imu.edu.cn/student/integratedQuery/scoreQuery/allPassingScores/index")
        .addHeader("Connection", "keep-alive")
        .addHeader("Cache-Control", "max-age=0")
        .addHeader("Upgrade-Insecure-Requests:", "1")
        .addHeader(
            "Accept",
            "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3"
        )
        .addHeader("Referer", "http://jwxt.imu.edu.cn/student/integratedQuery/scoreQuery/allPassingScores/index")
        .addHeader("Accept-Language", "zh,zh-TW;q=0.9,en-US;q=0.8,en;q=0.7,zh-CN;q=0.6")
        .addHeader("Cookie", "JSESSIONID=${JSESSIONID}; selectionBar=1379870")
        .build()
    var call = client.newCall(request)
    call.enqueue(object : Callback {
        override fun onResponse(call: Call, response: Response) {
            getScore_2(onResopnse)
        }

        override fun onFailure(call: Call, e: IOException) {

        }
    })
}

fun getScore_2(onResopnse: OnResponse? = null) {
    var client = OkHttpClient.Builder().build()
    var request = Request.Builder()
        .url("http://jwxt.imu.edu.cn/static/template.css")
        .addHeader("Connection", "keep-alive")
        .addHeader("Cache-Control", "max-age=0")
        .addHeader("Upgrade-Insecure-Requests", "1")
        .addHeader(
            "Accept",
            "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3"
        )
        .addHeader("Referer", "http://jwxt.imu.edu.cn/student/integratedQuery/scoreQuery/allPassingScores/index")
        .addHeader("Accept-Language", "zh,zh-TW;q=0.9,en-US;q=0.8,en;q=0.7,zh-CN;q=0.6")
        .addHeader("Cookie", "JSESSIONID=${JSESSIONID}; selectionBar=1379870")
        .build()
    var call = client.newCall(request)
    call.enqueue(object : Callback {
        override fun onResponse(call: Call, response: Response) {
            getScore_3(onResopnse)
        }

        override fun onFailure(call: Call, e: IOException) {

        }
    })
}

fun getScore_3(onResopnse: OnResponse? = null) {
    var client = OkHttpClient.Builder().build()
    var request = Request.Builder()
        .url("http://jwxt.imu.edu.cn/student/integratedQuery/scoreQuery/allPassingScores/callback")
        .addHeader("Connection", "keep-alive")
        .addHeader("Accept", "*/*")
        .addHeader("X-Requested-With", "XMLHttpRequest")
        .addHeader("Referer", "http://jwxt.imu.edu.cn/student/integratedQuery/scoreQuery/allPassingScores/index")
        .addHeader("Accept-Language", "zh,zh-TW;q=0.9,en-US;q=0.8,en;q=0.7,zh-CN;q=0.6")
        .addHeader("Cookie", "JSESSIONID=${JSESSIONID}; selectionBar=1379870")
        .build()
    var call = client.newCall(request)
    call.enqueue(object : Callback {
        override fun onResponse(call: Call, response: Response) {
            onResopnse?.onResponseOK(response)
            getScore_4()
        }

        override fun onFailure(call: Call, e: IOException) {

        }
    })
}

fun getScore_4() {
    var client = OkHttpClient.Builder().build()
    var request = Request.Builder()
        .url("http://jwxt.imu.edu.cn/img/icon/favicon.ico")
        .addHeader("Connection", "keep-alive")
        .addHeader("Pragma", "no-cache")
        .addHeader("Cache-Control", "no-cache")
        .addHeader("Accept", "image/webp,image/apng,image/*,*/*;q=0.8")
        .addHeader("Referer", "http://jwxt.imu.edu.cn/student/integratedQuery/scoreQuery/allPassingScores/index")
        .addHeader("Accept-Language", "zh,zh-TW;q=0.9,en-US;q=0.8,en;q=0.7,zh-CN;q=0.6")
        .addHeader("Cookie", "JSESSIONID=${JSESSIONID}; selectionBar=1379870")
        .build()
    var call = client.newCall(request)
    call.enqueue(object : Callback {
        override fun onResponse(call: Call, response: Response) {
            getScore_5()
        }

        override fun onFailure(call: Call, e: IOException) {

        }
    })
}

fun getScore_5() {
    var client = OkHttpClient.Builder().build()
    var request = Request.Builder()
        .url("http://jwxt.imu.edu.cn/student/integratedQuery/scoreQuery/coursePropertyScores/useScoreExtension")
        .addHeader("Connection", "keep-alive")
        .addHeader("Content-Length", "0")
        .addHeader("Accept", "application/json, text/javascript, */*; q=0.01")
        .addHeader("X-Requested-With", "XMLHttpRequest")
        .addHeader("Origin", "http://jwxt.imu.edu.cn")
        .addHeader("Referer", "http://jwxt.imu.edu.cn/student/integratedQuery/scoreQuery/allPassingScores/index")
        .addHeader("Accept-Language", "zh,zh-TW;q=0.9,en-US;q=0.8,en;q=0.7,zh-CN;q=0.6")
        .addHeader("Cookie", "JSESSIONID=${JSESSIONID}; selectionBar=1379870")
        .post(FormBody.Builder().build())
        .build()
    var call = client.newCall(request)
    call.enqueue(object : Callback {
        override fun onResponse(call: Call, response: Response) {

        }

        override fun onFailure(call: Call, e: IOException) {

        }
    })
}


fun logout() {
    var client = OkHttpClient.Builder().build()
    var request = Request.Builder()
        .url("http://jwxt.imu.edu.cn/logout")
        .addHeader("Connection", "keep-alive")
        .addHeader("Upgrade-Insecure-Requests", "1")
        .addHeader(
            "Accept",
            "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3"
        )
        .addHeader("Referer", "http://jwxt.imu.edu.cn/index.jsp")
        .addHeader("Accept-Language", "zh,zh-TW;q=0.9,en-US;q=0.8,en;q=0.7,zh-CN;q=0.6")
        .addHeader("Cookie", "JSESSIONID=${JSESSIONID}")
        .build()
    val call = client.newCall(request)
    call.enqueue(object : Callback {
        override fun onResponse(call: Call, response: Response) {
            Log.d("test:", "response")
        }

        override fun onFailure(call: Call, e: IOException) {

        }
    })
}

fun enterout() {
    var client = OkHttpClient.Builder().build()
    var request = Request.Builder()
        .url("http://jwxt.imu.edu.cn/enterOut")
        .addHeader("Connection", "keep-alive")
        .addHeader("Upgrade-Insecure-Requests", "1")
        .addHeader(
            "Accept",
            "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3"
        )
        .addHeader("Referer", "http://jwxt.imu.edu.cn/index.jsp")
        .addHeader("Accept-Language", "zh,zh-TW;q=0.9,en-US;q=0.8,en;q=0.7,zh-CN;q=0.6")
        .build()
    var call = client.newCall(request)
    call.enqueue(object : Callback {
        override fun onResponse(call: Call, response: Response) {

        }

        override fun onFailure(call: Call, e: IOException) {

        }
    })
}





fun searchClassroom(onResopnse: OnResponse? = null, where: String, startCourse: Int, endCourse: Int,dayPlus:Int) {
    var client = OkHttpClient.Builder().build()
    var request = Request.Builder()
        .url("http://jwxt.imu.edu.cn/student/teachingResources/freeClassroom/index")
        .addHeader("Connection", "keep-alive")
        .addHeader("Upgrade-Insecure-Requests", "1")
        .addHeader(
            "Accept",
            "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3"
        )
        .addHeader("Referer", "http://jwxt.imu.edu.cn/index.jsp")
        .addHeader("Accept-Language", "zh,zh-TW;q=0.9,en-US;q=0.8,en;q=0.7,zh-CN;q=0.6")
        .addHeader("Cookie", "JSESSIONID=${JSESSIONID}; selectionBar=1443372")
        .build()
    var call = client.newCall(request)
    call.enqueue(object : Callback {
        override fun onResponse(call: Call, response: Response) {
            searchClassroom_2(onResopnse,where,startCourse,endCourse,dayPlus)
        }

        override fun onFailure(call: Call, e: IOException) {

        }
    })
}

fun searchClassroom_2(onResopnse: OnResponse? = null, where: String, startCourse: Int, endCourse: Int,dayPlus:Int) {
    var client = OkHttpClient.Builder().build()
    var request = Request.Builder()
        .url("http://jwxt.imu.edu.cn/static/template.css")
        .addHeader("Connection", "keep-alive")
        .addHeader("Accept", "text/css,*/*;q=0.1")
        .addHeader("Referer", "http://jwxt.imu.edu.cn/student/teachingResources/freeClassroom/index")
        .addHeader("Accept-Language", "zh,zh-TW;q=0.9,en-US;q=0.8,en;q=0.7,zh-CN;q=0.6")
        .addHeader("Cookie", "JSESSIONID=${JSESSIONID}; selectionBar=1443372")
        .build()
    var call = client.newCall(request)
    call.enqueue(object : Callback {
        override fun onResponse(call: Call, response: Response) {
            searchClassroom_3(onResopnse,where,startCourse,endCourse,dayPlus)
        }

        override fun onFailure(call: Call, e: IOException) {

        }
    })
}

fun searchClassroom_3(onResopnse: OnResponse? = null, where: String, startCourse: Int, endCourse: Int,dayPlus:Int) {
    var client = OkHttpClient.Builder().build()
    var request = Request.Builder()
        .url("http://jwxt.imu.edu.cn/static/wroResources?id=/WEB-INF/assets/font-awesome/4.1.0/fonts/fontawesome-webfont.woff?v=4.1.0")
        .addHeader("Connection", "keep-alive")
        .addHeader("Origin", "http://jwxt.imu.edu.cn")
        .addHeader("Accept", "*/*")
        .addHeader("Referer", "http://jwxt.imu.edu.cn/static/template.css")
        .addHeader("Accept-Language", "zh,zh-TW;q=0.9,en-US;q=0.8,en;q=0.7,zh-CN;q=0.6")
        .addHeader("Cookie", "JSESSIONID=${JSESSIONID}; selectionBar=1443372")
        .build()
    var call = client.newCall(request)
    call.enqueue(object : Callback {
        override fun onResponse(call: Call, response: Response) {
            searchClassroom_4(onResopnse,where,startCourse,endCourse,dayPlus)
        }

        override fun onFailure(call: Call, e: IOException) {

        }
    })
}


fun searchClassroom_4(onResopnse: OnResponse? = null, where: String, startCourse: Int, endCourse: Int,dayPlus:Int) {
    var client = OkHttpClient.Builder().build()
    var formBody = FormBody.Builder()
        .add("position", where)
        .add("xqm", "%E5%8C%97%E6%A0%A1%E5%8C%BA")
        .build()
    var request = Request.Builder()
        .url("http://jwxt.imu.edu.cn/student/teachingResources/freeClassroom/today")
        .addHeader("Connection", "keep-alive")
        .addHeader("Content-Length", "47")
        .addHeader("Cache-Control", "max-age=0")
        .addHeader("Origin", "http://jwxt.imu.edu.cn")
        .addHeader("Upgrade-Insecure-Requests", "1")
        .addHeader("Content-Type", "application/x-www-form-urlencoded")
        .addHeader(
            "Accept",
            "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3"
        )
        .addHeader("Referer", "http://jwxt.imu.edu.cn/student/teachingResources/freeClassroom/today")
        .addHeader("Accept-Language", "zh,zh-TW;q=0.9,en-US;q=0.8,en;q=0.7,zh-CN;q=0.6")
        .addHeader("Cookie", "JSESSIONID=${JSESSIONID}; selectionBar=1443372")
        .post(formBody)
        .build()
    var call = client.newCall(request)
    call.enqueue(object : Callback {
        override fun onResponse(call: Call, response: Response) {
            searchClassroom_5(onResopnse,where,startCourse,endCourse,dayPlus)
        }

        override fun onFailure(call: Call, e: IOException) {

        }
    })
}

fun searchClassroom_5(onResopnse: OnResponse? = null, where: String, startCourse: Int, endCourse: Int,dayPlus:Int) {
    var client = OkHttpClient.Builder().build()
    var request = Request.Builder()
        .url("http://jwxt.imu.edu.cn/static/template.css")
        .addHeader("Connection", "keep-alive")
        .addHeader("Accept", "text/css,*/*;q=0.1")
        .addHeader("Referer", "http://jwxt.imu.edu.cn/student/teachingResources/freeClassroom/today")
        .addHeader("Accept-Language", "zh,zh-TW;q=0.9,en-US;q=0.8,en;q=0.7,zh-CN;q=0.6")
        .addHeader("Cookie", "JSESSIONID=${JSESSIONID}; selectionBar=1443372")
        .build()
    var call = client.newCall(request)
    call.enqueue(object : Callback {
        override fun onResponse(call: Call, response: Response) {
            searchClassroom_6(onResopnse,where,startCourse,endCourse,dayPlus)
        }

        override fun onFailure(call: Call, e: IOException) {

        }
    })
}

fun searchClassroom_6(onResopnse: OnResponse? = null, where: String, startCourse: Int, endCourse: Int,dayPlus:Int) {
    var client = OkHttpClient.Builder().build()
    var formBody = FormBody.Builder()
        .add("xqh", "1")
        .build()
    var request = Request.Builder()
        .url("http://jwxt.imu.edu.cn/student/teachingResources/freeClassroom/queryCodeTeaBuildingList")
        .addHeader("Connection", "keep-alive")
        .addHeader("Content-Length", "7")
        .addHeader("Accept", "application/json, text/javascript, */*; q=0.01")
        .addHeader("X-Requested-With", "XMLHttpRequest")
        .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
        .addHeader("Origin", "http://jwxt.imu.edu.cn")
        .addHeader("Referer", "http://jwxt.imu.edu.cn/student/teachingResources/freeClassroom/today")
        .addHeader("Accept-Language", "zh,zh-TW;q=0.9,en-US;q=0.8,en;q=0.7,zh-CN;q=0.6")
        .addHeader("Cookie", "JSESSIONID=${JSESSIONID}; selectionBar=1443372")
        .post(formBody)
        .build()
    var call = client.newCall(request)
    call.enqueue(object : Callback {
        override fun onResponse(call: Call, response: Response) {
            searchClassroom_7(onResopnse,where,startCourse,endCourse,dayPlus)
        }

        override fun onFailure(call: Call, e: IOException) {

        }
    })
}

fun searchClassroom_7(onResopnse: OnResponse? = null, where:String, startCourse: Int, endCourse: Int,dayPlus:Int) {
    var client = OkHttpClient.Builder().build()
    var URL = "http://jwxt.imu.edu.cn/student/teachingResources/freeClassroom/today/"
    URL += startCourse.toString()
    for (i in startCourse + 1..endCourse) {
        URL += "," + i.toString()
    }
    var formBody=FormBody.Builder()
        .add("dayplus",dayPlus.toString())
        .build()
    var request = Request.Builder()
        .url(URL)
        .addHeader("Connection", "keep-alive")
        .addHeader("Content-Length", "9")
        .addHeader("Accept", "application/json, text/javascript, */*; q=0.01")
        .addHeader("X-Requested-With", "XMLHttpRequest")
        .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
        .addHeader("Origin", "http://jwxt.imu.edu.cn")
        .addHeader("Referer", "http://jwxt.imu.edu.cn/student/teachingResources/freeClassroom/today")
        .addHeader("Accept-Language", "zh,zh-TW;q=0.9,en-US;q=0.8,en;q=0.7,zh-CN;q=0.6")
        .addHeader("Cookie", "JSESSIONID=${JSESSIONID}; selectionBar=1443372")
        .post(formBody)
        .build()
    var call = client.newCall(request)
    call.enqueue(object : Callback {
        override fun onResponse(call: Call, response: Response) {
            onResopnse?.onResponseOK(response)
        }

        override fun onFailure(call: Call, e: IOException) {
            onResopnse?.onResponseFalse()
        }
    })
}



fun getCourseList(string: String): List<CourseBean> {
    val list = ArrayList<CourseBean>()
    val responseJson = JSONObject.parseObject(string)
    val courseList = responseJson.getJSONArray("dateList").getJSONObject(0).getJSONArray("selectCourseList")
    for (i in 0 until courseList.size) {

        val courseInfo = courseList.getJSONObject(i)
        val courseTeacher = courseInfo.getString("attendClassTeacher")
        val courseName = courseInfo.getString("courseName")
        val timeList = courseInfo.getJSONArray("timeAndPlaceList")
        timeList?.run {
            for (j in 0 until timeList.size) {
                var timeInfo = timeList.getJSONObject(j)
                var dayOfWeek = timeInfo.getInteger("classDay")
                var startCourse = timeInfo.getInteger("classSessions")
                var endCourse = startCourse + timeInfo.getInteger("continuingSession") - 1
                var week = timeInfo.getString("weekDescription")
                var place = timeInfo.getString("teachingBuildingName") + timeInfo.getString("classroomName")
                when (week) {
                    "单周" -> {
                        var courseBean = CourseBean(
                            courseName,
                            courseTeacher,
                            place,
                            CourseBean.TYPE_SINGLE,
                            startCourse,
                            endCourse,
                            dayOfWeek
                        )
                        courseBean.start_week = 0
                        courseBean.end_week = 0
                        list.add(courseBean)
                    }
                    "双周" -> {
                        var courseBean = CourseBean(
                            courseName,
                            courseTeacher,
                            place,
                            CourseBean.TYPE_DOUBLE,
                            startCourse,
                            endCourse,
                            dayOfWeek
                        )
                        courseBean.start_week = 0
                        courseBean.end_week = 0
                        list.add(courseBean)
                    }
                    else -> {
                        var startWeek: Int = 0
                        var endWeek: Int = 0
                        var weeks=week.split("-","周")
                        startWeek=weeks[0].toInt()
                        endWeek=weeks[1].toInt()
                        Log.d("test:","$startWeek  $endWeek")
                        var courseBean = CourseBean(
                            courseName,
                            courseTeacher,
                            place,
                            CourseBean.TYPE_ALL,
                            startCourse,
                            endCourse,
                            dayOfWeek
                        )
                        courseBean.start_week = startWeek
                        courseBean.end_week = endWeek
                        list.add(courseBean)
                    }
                }
            }
        }
    }
    return list
}



fun getScoreList(string: String): List<ScoreBean> {
    var list=ArrayList<ScoreBean>()
    var responseObject= JSONObject.parseObject(string)
    var lnList=responseObject.getJSONArray("lnList")
//    var courseObject=lnList.getJSONObject(lnList.size-1)
    for(j in 0 until lnList.size)
    {
        var courseObject=lnList.getJSONObject(j)
        var scoreList=courseObject.getJSONArray("cjList")
        for (i in 0 until scoreList.size) {
            var score=scoreList.getJSONObject(i)
            var courseName=score.getString("courseName")
            var courseScore=score.getString("courseScore")
            var credit=score.getInteger("credit")
            var pointScore=score.getDouble("gradePointScore")?:0.0
            list.add(ScoreBean(courseName,courseScore,credit,pointScore))
        }
    }

    return list
}



