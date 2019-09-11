package com.example.imuhelper.adapter

import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.imuhelper.R
import com.example.imuhelper.bean.CourseBean
import com.example.imuhelper.bean.ScoreBean
import kotlinx.android.synthetic.main.item_scorelist.view.*
import org.w3c.dom.Text

class ScoreListAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var list: List<ScoreBean>

    constructor(list: List<ScoreBean>) : this() {
        this.list = list
    }

    inner class ScoreHolder(view: View) : RecyclerView.ViewHolder(view) {
        var courseNameTv: TextView = view.findViewById(R.id.scoreList_courseName)
        var scoreTv: TextView = view.findViewById(R.id.scoreList_score)
        var pointTv: TextView = view.findViewById(R.id.scoreList_point)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_scorelist, parent,false)
        return ScoreHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ScoreHolder).courseNameTv.text = list[position].courseName
        (holder as ScoreHolder).scoreTv.text = list[position].score
        (holder as ScoreHolder).pointTv.text = list[position].pointScore.toString()
    }

}