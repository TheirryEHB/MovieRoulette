package com.example.movieroulette

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class WinnerActivity : AppCompatActivity() {

    private lateinit var winnertext: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_winner)

        winnertext = findViewById(R.id.winner_textview)
    }
}