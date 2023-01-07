package com.example.movieroulette

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.movieroulette.database.RoomDBHelper
import com.example.movieroulette.models.MovieModel
import com.squareup.picasso.Picasso
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
        var imageView: ImageView

        init{
            headElement = view.findViewById(R.id.front_recycle_item)
            titleView = view.findViewById(R.id.textTitle)
            langView = view.findViewById(R.id.textLang)
            ratingView = view.findViewById(R.id.textRating)
            releaseView = view.findViewById(R.id.textRelease)
            imageView = view.findViewById(R.id.imageView)

        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder{

        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.recycle_front, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemsData = dataSet[position]

        holder.titleView.text = itemsData.getString("title")
        holder.langView.text = itemsData.getString("original_language")
        holder.ratingView.text = itemsData.getString("vote_average")
        holder.releaseView.text = itemsData.getString("release_date")
        val url =  "https://image.tmdb.org/t/p/w500"+itemsData.getString("poster_path")
        Picasso.get().load(url).into(holder.imageView)

        holder.headElement.setOnClickListener {
            val movie = MovieModel()
            movie.id = itemsData.getString("id")
            movie.lang = itemsData.getString("original_language")
            movie.name = itemsData.getString("title")
            movie.rating = itemsData.getString("vote_average")
            movie.release = itemsData.getString("release_date")
            movie.img = itemsData.getString("poster_path")


            if(chosenMovies.size == 0)
                chosenMovies.add(movie)
            else if(chosenMovies.size >= 1 && chosenMovies.size < 5) {
//                for (m in chosenMovies) {
//                    if (m.id == movie.id)
//                        Toast.makeText(holder.headElement.context,
//                            "Movie already added", Toast.LENGTH_SHORT).show()
//                    else{
//                        chosenMovies.add(movie)
//                    }
//                }
                chosenMovies.add(movie)
            }
            else
                Toast.makeText(holder.headElement.context,
                    "Max number of movies is 5", Toast.LENGTH_LONG).show()
        }

    }

    override fun getItemCount(): Int {
        return dataSet.size
    }
}