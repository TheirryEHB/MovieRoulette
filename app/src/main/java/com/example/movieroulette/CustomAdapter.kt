package com.example.movieroulette

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class CustomAdapter(private val dataSet: Array<String>):
RecyclerView.Adapter<CustomAdapter.ViewHolder>(){

    class ViewHolder(view: recycleview_irem): RecyclerView.ViewHolder(view){
        val titleView: textTitle

        init{}
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder{

        val view = LayoutInlater.from(viewGroup.context)
            .inlate(R.layout.textTitle, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}