package com.example.movieroulette

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.movieroulette.database.RoomDBHelper
import com.example.movieroulette.models.MovieModel
import org.json.JSONObject

class CustomAdapter(private val dataSet: ArrayList<JSONObject>):
RecyclerView.Adapter<CustomAdapter.ViewHolder>(){

    var chosenMovies = RoomDBHelper.chosenMovieArr
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
            val movie = MovieModel()
            movie.id = itemsData.getString("id")
            movie.lang = itemsData.getString("original_language")
            movie.name = itemsData.getString("original_title")
            movie.rating = itemsData.getString("vote_average")
            movie.release = itemsData.getString("release_date")

            if (chosenMovies.size >= 5){
                Toast.makeText(holder.headElement.context,
                    "Max number of movies is 5", Toast.LENGTH_LONG).show()
            }
            else{
//                if(chosenMovies.size >= 1) {
//                    for (m in chosenMovies) {
//                        if (m.id == movie.id)
//                            Toast.makeText(
//                                holder.headElement.context,
//                                "Movie already added",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        else
//                            chosenMovies.add(movie)
//                    }
//                }
//                else
                    chosenMovies.add(movie)
            }
        }

    }

    override fun getItemCount(): Int {
        return dataSet.size
    }
}