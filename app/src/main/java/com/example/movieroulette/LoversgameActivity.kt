package com.example.movieroulette

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.movieroulette.database.RoomDBHelper

class LoversgameActivity : AppCompatActivity() {
    val room = RoomDBHelper()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loversgame)

        val randomNumber = (0..1).random()
        val winningMovie = RoomDBHelper.chosenMovieArr.get(randomNumber)

        val titleText = findViewById<TextView>(R.id.textTitle)
        titleText.text = winningMovie.name


    }

    override fun onBackPressed() {
//        super.onBackPressed()
        RoomDBHelper.chosenMovieArr.clear()
        finish()
    }
}