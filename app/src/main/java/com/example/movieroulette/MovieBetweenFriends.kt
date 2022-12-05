package com.example.movieroulette

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.room.Room
import com.example.movieroulette.database.FirebaseDBHelper
import com.example.movieroulette.database.RoomDBHelper
import com.example.movieroulette.models.QnAModel
import kotlinx.coroutines.*
import kotlin.concurrent.thread

class MovieBetweenFriends : AppCompatActivity() {

    val firebase: FirebaseDBHelper = FirebaseDBHelper()
    val room: RoomDBHelper = RoomDBHelper()
    private lateinit var friendsDao: RoomDBHelper.FriendsGameDao
    private lateinit var db: RoomDBHelper.AppDatabase
    private var gameArray: List<RoomDBHelper.FriendsGame> = ArrayList()

    private lateinit var titleTextview: TextView
    var questArr: ArrayList<QnAModel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_between_friends)

        titleTextview = findViewById(R.id.title_text_view)

        db = Room.databaseBuilder(
            applicationContext,
            RoomDBHelper.AppDatabase::class.java, "DB1"
        ).build()
        friendsDao = db.FriendsGameDao()

       thread { makeNewGame() }

    }

    private fun fillInView(index: Int){
        GlobalScope.launch(Dispatchers.Main) {
            titleTextview.text = gameArray[index].MovieName
        }
    }

    private fun makeNewGame(){
        nukeTable()
        firebase.getQnA() {
            result ->
             firstUpdateView(result)
        }
    }

     private fun firstUpdateView(questA: ArrayList<QnAModel>) {
        questArr = questA
        if (questArr.size != 0) {
            Log.e("fff", FirebaseDBHelper.questionArr.size.toString())

            for (i in 0 until RoomDBHelper.chosenMovieArr.size) {
                questArr[i].movieName = RoomDBHelper.chosenMovieArr[i].name
            }

            insertGameinDB(questArr)
            getCurrentGames()
            fillInView(0)
        }
        else
            Log.e("fffisNull", FirebaseDBHelper.questionArr.size.toString())
    }


    private  fun insertGameinDB(questArray: ArrayList<QnAModel>) {
        questArray.forEach {
            friendsDao.insertGame(RoomDBHelper.FriendsGame(0, it.movieName, it.id, false, false, 0))
//                Log.d("fd", RoomDBHelper.FriendsGame(0, it.id, false, false, 0).toString())
        }
    }
    private fun nukeTable() { friendsDao.nukeTable() }
    private  fun getCurrentGames () { gameArray = friendsDao.getAll() }

    override fun onBackPressed() {
        RoomDBHelper.chosenMovieArr.clear()
        finish()
    }
}