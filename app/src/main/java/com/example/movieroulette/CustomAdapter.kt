package com.example.movieroulette

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONObject

class CustomAdapter(private val dataSet: ArrayList<JSONObject>):
RecyclerView.Adapter<CustomAdapter.ViewHolder>(){

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        var headElement: ConstraintLayout
        var titleView: TextView
        var langView: TextView
        val ratingView: TextView
        val releaseView: TextView

        init{
            headElement = view.findViewById(R.id.front_recycle_item)
            titleView = view.findViewById(R.id.textTitle)
            langView = view.findViewById(R.id.textLang)
            ratingView = view.findViewById(R.id.textRating)
            releaseView = view.findViewById(R.id.textRelease)

        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder{

        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.recycle_front, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemsData = dataSet[position]

        holder.titleView.text = itemsData.getString("original_title")
        holder.langView.text = itemsData.getString("original_language")
        holder.ratingView.text = itemsData.getString("vote_average")
        holder.releaseView.text = itemsData.getString("release_date")

        holder.headElement.setOnClickListener {
            Log.d("here", holder.titleView.text.toString())
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }
}