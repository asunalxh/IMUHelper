package com.example.imuhelper.bean

class ScoreBean {
    var score: String = ""//成绩
    var courseName:String=""
    var pointScore:Double=0.0//绩点
    var credit:Int =0//学分

    constructor(courseName: String = "", score: String = "", credit: Int = 0, pointScore: Double=0.0){
        this.courseName= courseName
        this.score=score
        this.credit=credit
        this.pointScore=pointScore
    }
}