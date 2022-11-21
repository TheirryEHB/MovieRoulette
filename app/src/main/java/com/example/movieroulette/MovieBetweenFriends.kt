package com.example.movieroulette

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.movieroulette.database.FirebaseDBHelper
import com.example.movieroulette.database.RoomDBHelper

class MovieBetweenFriends : AppCompatActivity() {

    val firebase: FirebaseDBHelper = FirebaseDBHelper()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_between_friends)

        makeNewGame()
    }


    fun makeNewGame(){
        firebase.getQnA()
    }

    override fun onBackPressed() {
        RoomDBHelper.chosenMovieArr.clear()
        finish()
    }
}