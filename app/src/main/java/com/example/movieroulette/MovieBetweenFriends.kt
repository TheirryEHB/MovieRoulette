package com.example.movieroulette

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.room.Room
import com.example.movieroulette.database.FirebaseDBHelper
import com.example.movieroulette.database.RoomDBHelper
import com.example.movieroulette.models.FriendsGameModel
import com.example.movieroulette.models.QnAModel
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.thread
import kotlin.math.floor

class MovieBetweenFriends : AppCompatActivity() {

    val firebase: FirebaseDBHelper = FirebaseDBHelper()
    val room: RoomDBHelper = RoomDBHelper()
    private lateinit var friendsDao: RoomDBHelper.FriendsGameDao
    private lateinit var db: RoomDBHelper.AppDatabase
    private var gameArray: List<RoomDBHelper.FriendsGame> = ArrayList()
    private lateinit var currentGame: RoomDBHelper.FriendsGame
    private var gameIndex: Int = 0

    private lateinit var titleTextview: TextView
    private lateinit var questionTextview: TextView
    var questArr: ArrayList<QnAModel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_between_friends)

        titleTextview = findViewById(R.id.title_text_view)
        questionTextview = findViewById(R.id.question_text_view)
        val yesButton = findViewById<Button>(R.id.yes_button)
        val noButton = findViewById<Button>(R.id.no_button)
        yesButton.setOnClickListener {
            checkAnswer("true")
        }
        noButton.setOnClickListener { checkAnswer("false") }

        db = Room.databaseBuilder(
            applicationContext,
            RoomDBHelper.AppDatabase::class.java, "DB1"
        )
            .fallbackToDestructiveMigration()
            .build()
        friendsDao = db.FriendsGameDao()

       thread { makeNewGame() }

    }

    private fun checkAnswer(ans: String){
        if (ans == currentGame.Answer){
            currentGame.DidAns = true
            currentGame.isRight = true
            currentGame.AnswerTime = 0
            updateGame()
        }
        else{
            currentGame.DidAns = true
            currentGame.isRight = false
            currentGame.AnswerTime = 0
            updateGame()
        }
    }
    private fun updateGame(){
        if(gameIndex == gameArray.size){
            thread {
                friendsDao.insertGame(currentGame)
            }
        }
        else {
            thread {
                friendsDao.insertGame(currentGame)
                ++gameIndex
                fillInView()
            }
        }

    }

    private fun fillInView(){
        GlobalScope.launch(Dispatchers.Main) {
            currentGame = gameArray[gameIndex]
            titleTextview.text = gameArray[gameIndex].MovieName
            questionTextview.text = gameArray[gameIndex].Question
        }
    }

    private fun makeNewGame(){
        nukeTable()
        FirebaseDBHelper.getQnA(::firstUpdateView)
    }

     private fun firstUpdateView(questA: ArrayList<QnAModel>){
         questArr = questA
        if (questArr.size != 0) {
            questArr.shuffle()
            questArr = questArr.take(RoomDBHelper.chosenMovieArr.size).toCollection(ArrayList())
            for (i in 0 until RoomDBHelper.chosenMovieArr.size - 1) {
                questArr[i].movieName = RoomDBHelper.chosenMovieArr[i].name
            }
            thread {
                runBlocking {
                    insertGamesinDB(questArr)
                    getCurrentGames()
                    fillInView()
                }
            }
        }
        else
            Log.e("fffisNull", FirebaseDBHelper.questionArr.size.toString())

    }
    private fun insertGamesinDB(questArray: ArrayList<QnAModel>) {
        questArray.forEach {
            val uuid = UUID.randomUUID().toString()
            Log.d("fddd", uuid)
            friendsDao.insertGame(RoomDBHelper.FriendsGame(uuid, it.movieName, it.id, it.question, it.answer, false, false, 0))
        }
    }
    private fun nukeTable() { friendsDao.nukeTable() }
    private fun getCurrentGames () { gameArray = friendsDao.getAll() }

    override fun onBackPressed() {
        RoomDBHelper.chosenMovieArr.clear()
        finish()
    }
}