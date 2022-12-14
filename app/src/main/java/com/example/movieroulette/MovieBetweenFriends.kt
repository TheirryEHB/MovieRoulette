package com.example.movieroulette

import android.content.Intent
import android.media.Image
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.SystemClock
import android.util.Log
import android.widget.Button
import android.widget.Chronometer
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.room.Room
import com.example.movieroulette.database.FirebaseDBHelper
import com.example.movieroulette.database.RoomDBHelper
import com.example.movieroulette.models.FriendsGameModel
import com.example.movieroulette.models.MovieModel
import com.example.movieroulette.models.QnAModel
import com.squareup.picasso.Picasso
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
    private var elapsedTime: Int = 0
    private val maxTime: Int = 10

    private lateinit var titleTextview: TextView
    private lateinit var questionTextview: TextView
    private lateinit var posterView: ImageView
    var questArr: ArrayList<QnAModel> = ArrayList()

    //timer
    private lateinit var view_timer: Chronometer

    //TODO zien of getisright goed werkt

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_between_friends)

        titleTextview = findViewById(R.id.title_text_view)
        questionTextview = findViewById(R.id.question_text_view)
        view_timer = findViewById(R.id.view_timer)
        posterView = findViewById<ImageView>(R.id.posterView)



        val yesButton = findViewById<Button>(R.id.yes_button)
        val noButton = findViewById<Button>(R.id.no_button)
        yesButton.setOnClickListener {
            view_timer.stop()
//            if (elapsedTime > maxTime)
//                elapsedTime = maxTime
            checkAnswer("true")
        }
        noButton.setOnClickListener {
            view_timer.stop()
//            if (elapsedTime > maxTime)
//                elapsedTime = maxTime
            checkAnswer("false")
        }

        db = Room.databaseBuilder(
            applicationContext,
            RoomDBHelper.AppDatabase::class.java, "DB1"
        )
            .fallbackToDestructiveMigration()
            .build()
        friendsDao = db.FriendsGameDao()

       thread { makeNewGame() }

        view_timer.isCountDown = true
        view_timer.base = SystemClock.elapsedRealtime() + 10000
        view_timer.setOnChronometerTickListener {
            ++elapsedTime
            if(SystemClock.elapsedRealtime() >= view_timer.base){
                view_timer.stop()
                checkAnswer("none")
            }
        }
    }

    private fun checkAnswer(ans: String){
        if (ans == currentGame.Answer){
            currentGame.DidAns = true
            currentGame.isRight = true
            currentGame.AnswerTime = elapsedTime
            Log.d("elapsedTime", elapsedTime.toString())
            updateGame()
        }
        else if(ans == "none"){
            currentGame.DidAns = false
            currentGame.isRight = false
            currentGame.AnswerTime = elapsedTime
            updateGame()
        }
        else{
            currentGame.DidAns = true
            currentGame.isRight = false
            currentGame.AnswerTime = elapsedTime
            updateGame()
        }
        elapsedTime = 0
        view_timer.base = SystemClock.elapsedRealtime() + 10000
        view_timer.start()
    }
    private fun updateGame(){
        if(gameIndex == gameArray.size - 1){ //Last question has been answered
            thread {
                friendsDao.update(currentGame.DidAns.toString(), currentGame.isRight.toString(), currentGame.AnswerTime.toString(), currentGame.Uuid)
                decideWinner()
            }
        }
        else {
            thread { //Show next question
                friendsDao.update(currentGame.DidAns.toString(), currentGame.isRight.toString(), currentGame.AnswerTime.toString(), currentGame.Uuid)
                ++gameIndex
                fillInView()
            }
        }

    }
    private fun decideWinner(){
        thread{
            runBlocking {
                getIsRightGames()
                var isLeast: Int = maxTime
                gameArray.forEach{
                    if (it.AnswerTime!! <= isLeast) {
                        currentGame = it
                        isLeast = currentGame.AnswerTime!!
                    }
                }
                winnerIntent()
            }
        }

    }
    private fun winnerIntent(){
        val intent = Intent(this, WinnerActivity::class.java)
        var themov = MovieModel()
        RoomDBHelper.chosenMovieArr.forEach {
         if(it.name == currentGame.MovieName)
             themov = it
        }
        intent.putExtra("id", themov.id)
        intent.putExtra("name", themov.name)
        intent.putExtra("rating", themov.rating)
        intent.putExtra("release", themov.release)
        intent.putExtra("lang", themov.lang)
        intent.putExtra("imgUrl", themov.img)
        startActivity(intent)
    }

    private fun fillInView(){
        GlobalScope.launch(Dispatchers.Main) {
            currentGame = gameArray[gameIndex]
            var themov = MovieModel()
            RoomDBHelper.chosenMovieArr.forEach {
                if(it.name == currentGame.MovieName)
                    themov = it
            }
            titleTextview.text = gameArray[gameIndex].MovieName
            questionTextview.text = gameArray[gameIndex].Question
            val url =  "https://image.tmdb.org/t/p/w500"+ themov.img
            Picasso.get().load(url).into(posterView)
            view_timer.start()
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
            for (i in 0 until RoomDBHelper.chosenMovieArr.size) {
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
            Log.d("fddd", it.movieName)
            friendsDao.insertGame(RoomDBHelper.FriendsGame(uuid, it.movieName, it.id, it.question, it.answer, false, false, 0))
        }
    }
    private fun nukeTable() { friendsDao.nukeTable() }
    private fun getCurrentGames() { gameArray = friendsDao.getAll() }
    private fun getIsRightGames(){ gameArray = friendsDao.findByRight("true") }

    override fun onBackPressed() {
        RoomDBHelper.chosenMovieArr.clear()
//        finish()
        nukeTable()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}