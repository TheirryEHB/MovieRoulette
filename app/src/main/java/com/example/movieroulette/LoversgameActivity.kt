package com.example.movieroulette

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.example.movieroulette.database.RoomDBHelper
import com.squareup.picasso.Picasso

class LoversgameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loversgame)

        val randomNumber = (0..1).random()
        val winningMovie = RoomDBHelper.chosenMovieArr[randomNumber]

        val titleText = findViewById<TextView>(R.id.textTitle)
        titleText.text = winningMovie.name
        val image = findViewById<ImageView>(R.id.imageView)
        val url =  "https://image.tmdb.org/t/p/w500"+winningMovie.img
        Picasso.get().load(url).into(image)

    }

    override fun onBackPressed() {
        RoomDBHelper.chosenMovieArr.clear()
        finish()
    }
}