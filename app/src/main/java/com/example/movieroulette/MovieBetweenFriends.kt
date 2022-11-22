package com.example.movieroulette

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.room.Room
import com.example.movieroulette.database.FirebaseDBHelper
import com.example.movieroulette.database.RoomDBHelper
import com.example.movieroulette.models.QnAModel
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.runBlocking

class MovieBetweenFriends : AppCompatActivity() {

    val firebase: FirebaseDBHelper = FirebaseDBHelper()
    val room: RoomDBHelper = RoomDBHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_between_friends)

        makeNewGame()
    }


    fun makeNewGame(){
        val job = runBlocking {
            val questArr = firebase.getQnA()
            val job2 = runBlocking { insertGameinDB(questArr)}
        }

    }

    suspend fun insertGameinDB(questArray: ArrayList<QnAModel>){
        questArray.forEach {
//            RoomDBHelper.FriendsGame.
//            RoomDBHelper.FriendsGameDao.insertGame(it)
        }

    }

    override fun onBackPressed() {
        RoomDBHelper.chosenMovieArr.clear()
        finish()
    }
}