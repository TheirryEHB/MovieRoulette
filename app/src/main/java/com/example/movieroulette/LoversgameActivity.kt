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
        val langText = findViewById<TextView>(R.id.textLang)
        val releaseText = findViewById<TextView>(R.id.textRelease)
        val ratingText = findViewById<TextView>(R.id.textRating)
        titleText.text = winningMovie.name
        langText.text = winningMovie.lang
        releaseText.text = winningMovie.release
        ratingText.text = winningMovie.rating
        val image = findViewById<ImageView>(R.id.imageView)
        val url =  "https://image.tmdb.org/t/p/w500"+winningMovie.img
        Picasso.get().load(url).into(image)

    }

    override fun onBackPressed() {
        RoomDBHelper.chosenMovieArr.clear()
        finish()
    }
}