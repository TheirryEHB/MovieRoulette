package com.example.movieroulette

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.room.Room
import com.example.movieroulette.database.FirebaseDBHelper
import com.example.movieroulette.database.RoomDBHelper
import com.example.movieroulette.models.FriendsGameModel
import com.example.movieroulette.models.QnAModel
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MovieBetweenFriends : AppCompatActivity() {

    val firebase: FirebaseDBHelper = FirebaseDBHelper()
    val room: RoomDBHelper = RoomDBHelper()
    private lateinit var friendsDao: RoomDBHelper.FriendsGameDao
    private lateinit var db: RoomDBHelper.AppDatabase
    private var gameArray: List<RoomDBHelper.FriendsGame> = ArrayList()

    private lateinit var titleTextview: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_between_friends)

        titleTextview = findViewById(R.id.title_text_view)

        db = Room.databaseBuilder(
            applicationContext,
            RoomDBHelper.AppDatabase::class.java, "DB1"
        ).build()
        friendsDao = db.FriendsGameDao()

        runBlocking { launch{makeNewGame()}}
    }


    suspend fun makeNewGame() = coroutineScope {

        var questArr: ArrayList<QnAModel> = ArrayList()
        nukeTable()
        launch{questArr = firebase.getQnA()}
        launch{insertGameinDB(questArr)}
        launch{
            getCurrentGames()
            for (i in 0..questArr.size){
                questArr.get(i).movieName = RoomDBHelper.chosenMovieArr.get(i).name
            }

            titleTextview.text = gameArray.get(0).MovieName
        }



    }

    private suspend fun insertGameinDB(questArray: ArrayList<QnAModel>) = coroutineScope {
        launch {
            questArray.forEach {
                friendsDao.insertGame(RoomDBHelper.FriendsGame(0, it.movieName, it.id, false, false, 0))
//                Log.d("fd", RoomDBHelper.FriendsGame(0, it.id, false, false, 0).toString())
            }
        }
    }
    private suspend fun nukeTable() = coroutineScope{ launch {friendsDao.nukeTable() }}
    private suspend fun getCurrentGames () = coroutineScope { launch {gameArray = friendsDao.getAll()} }

    override fun onBackPressed() {
        RoomDBHelper.chosenMovieArr.clear()
        finish()
    }
}