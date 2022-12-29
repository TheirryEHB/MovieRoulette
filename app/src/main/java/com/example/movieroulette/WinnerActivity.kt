package com.example.movieroulette

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.example.movieroulette.database.RoomDBHelper
import com.squareup.picasso.Picasso

class WinnerActivity : AppCompatActivity() {

    private lateinit var winnertext: TextView
    private lateinit var winnerimg: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_winner)

        winnertext = findViewById(R.id.winner_textview)
        winnerimg = findViewById(R.id.winner_imageView)

        val bundle : Bundle? = intent.extras
        val id = bundle!!.getString("id")
        val moviename = bundle.getString("name")
        val url = bundle.getString("imgUrl")

        winnertext.text = moviename
        Picasso.get().load(url).into(winnerimg)
    }

    override fun onBackPressed() {
        RoomDBHelper.chosenMovieArr.clear()
//        finish()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}