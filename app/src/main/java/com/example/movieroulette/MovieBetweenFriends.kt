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
    private lateinit var friendsDao: RoomDBHelper.FriendsGameDao
    private lateinit var db: RoomDBHelper.AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_between_friends)

        db = Room.databaseBuilder(
            applicationContext,
            RoomDBHelper.AppDatabase::class.java, "DB1"
        ).build()
        friendsDao = db.FriendsGameDao()

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
            friendsDao.insertGame(RoomDBHelper.FriendsGame(0, it.id, false, false, 0))

        }

    }

    override fun onBackPressed() {
        RoomDBHelper.chosenMovieArr.clear()
        finish()
    }
}