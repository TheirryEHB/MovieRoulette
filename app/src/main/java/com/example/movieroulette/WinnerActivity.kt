package com.example.movieroulette

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.movieroulette.database.RoomDBHelper

class WinnerActivity : AppCompatActivity() {

    private lateinit var winnertext: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_winner)

        winnertext = findViewById(R.id.winner_textview)

        val bundle : Bundle? = intent.extras
        val id = bundle!!.getString("id")
        val moviename = bundle.getString("name")

        winnertext.text = moviename
    }

    override fun onBackPressed() {
        RoomDBHelper.chosenMovieArr.clear()
//        finish()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}