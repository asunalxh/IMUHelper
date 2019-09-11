package com.example.imuhelper.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.imuhelper.R

class SearchResultRvAdapter(list:ArrayList<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var list = list


    class Holder(view : View) : RecyclerView.ViewHolder(view) {
        var textView  = view.findViewById<TextView>(R.id.result_Tv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_result_tv,parent,false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as Holder).textView.text = (list[position])
    }


}