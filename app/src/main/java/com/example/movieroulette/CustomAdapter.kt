package com.example.movieroulette

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONObject

class CustomAdapter(private val dataSet: ArrayList<JSONObject>):
RecyclerView.Adapter<CustomAdapter.ViewHolder>(){

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        var titleView: TextView
        var langView: TextView

        init{
            titleView = view.findViewById(R.id.textTitle)
            langView = view.findViewById(R.id.textLang)

        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder{

        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.recycle_front, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemsData = dataSet[position]

        Log.d("valuu", itemsData.toString())
        holder.titleView.text = itemsData.getString("title")
        holder.langView.text = itemsData.getString("original_language")
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }
}