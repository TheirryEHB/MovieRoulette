package com.example.movieroulette

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.movieroulette.database.RoomDBHelper
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import org.json.JSONException
import org.json.JSONObject
import kotlin.system.exitProcess


class MainActivity : AppCompatActivity() {

    //actionbar of navigation drawer
    //Ondersteuning van landscape/portrait mode (m.a.w. verschillende layoutbestanden) voor minstens 1 activity
    //Minimale ondersteuning van hdpi, xhdpi, xxhdpi en xxxhdpi schermen --> toch gewoon gelijk aan sp, dp en vector images te gebruiken?
    //Gebruik van JUnit-framework voor het schrijven van testen voor je applicatie (minimum 1)
    //Correcte toepassing van de navigation design patterns binnen Android
    //Ondersteuning voor minstens Engels en een andere taal naar keuze.
    //TODO zelfde film in set

    private val data = ArrayList<JSONObject>()
    private lateinit var searchBar:LinearLayout
    private val room = RoomDBHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val recyclerView: RecyclerView = findViewById(R.id.front_recycle_view)
        val newGameButton = findViewById<Button>(R.id.new_game_button)
        searchBar = findViewById(R.id.search_bar)
        val searchButton: Button = findViewById(R.id.search_button)
        val searchEditText: EditText = findViewById(R.id.search_edit_text)
//        val parentView = findViewById<ConstraintLayout>(R.id.main_constraint)

        searchBar.visibility = View.GONE
//        val params = recyclerView.layoutParams as ConstraintLayout.LayoutParams
//        params.topToTop = parentView.id
//        recyclerView.requestLayout()

        /*RoomDb*/
        room.db = Room.databaseBuilder(
                applicationContext,
                RoomDBHelper.AppDatabase::class.java, "DB1"
        ).build()

        /*Make top week movies url*/
        val topWeekUrl = getTrendingMoviesOfWeek()
        /*Make recyclerView*/
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        /*Get data and fill recyclerView*/
        runBlocking { getData(topWeekUrl) }
        recyclerView.adapter = adapter
        /*New game button*/
        newGameButton.setOnClickListener {
            if (RoomDBHelper.chosenMovieArr.size < 2 )
                Toast.makeText(this,
                    "Choose at least twoo movies.", Toast.LENGTH_SHORT).show()
            else{
                if (RoomDBHelper.chosenMovieArr.size == 2)
                    startLoversGame()
                else if (RoomDBHelper.chosenMovieArr.size > 2)
                    startFriendsGame()
            }
        }
        /*Search button*/
        searchButton.setOnClickListener {
            if(searchEditText.text.toString() == "")
                Toast.makeText(this, "Chose a title first", Toast.LENGTH_SHORT).show()
            else {
                runBlocking {
                    getData(getMovieSearch(searchEditText.text.toString()))
                    adapter.notifyDataSetChanged()
                }
            }
        }
    }

    /**
     * Gets movie data from API
     * **/
    suspend fun getData(topWeekUrl: String) = coroutineScope{
     //   {"adult":false,"backdrop_path":"\/yYrvN5WFeGYjJnRzhY0QXuo4Isw.jpg","id":505642,"title":"Black Panther: Wakanda Forever","original_language":"en","original_title":"Black Panther: Wakanda Forever","overview":"Queen Ramonda, Shuri, M’Baku, Okoye and the Dora Milaje fight to protect their nation from intervening world powers in the wake of King T’Challa’s death. As the Wakandans strive to embrace their next chapter, the heroes must band together with the help of War Dog Nakia and Everett Ross and forge a new path for the kingdom of Wakanda.","poster_path":"\/sv1xJUazXeYqALzczSZ3O6nkH75.jpg","media_type":"movie","genre_ids":[28,12,878],"popularity":4392.866,"release_date":"2022-11-09","video":false,"vote_average":7.6,"vote_count":630}
        val res = GetRequest().main(topWeekUrl)
        if (res.substring(0, 4) == "Some") {
            Log.d("urlError", res)
        } else {
            val jsonob = parse(res)
            if (jsonob != null) {
                data.clear()
                val movieRes = jsonob.getJSONArray("results")
                for (i in 0 until movieRes.length()) {
                    data.add(movieRes.getJSONObject(i))
                }
            } else{}
            }
    }

    private val adapter = CustomAdapter(data)

    fun getAPIKey(): String {
        return "api_key=8ff2d545fa19ee5ef1d52be200306079"
    }
    fun getBaseURL(): String{
        return "https://api.themoviedb.org/3/"
    }
    fun getTrendingMoviesOfWeek(): String{
        return getBaseURL()+"trending/movie/day?"+getAPIKey()
    }
    fun getMovieSearch(title: String): String{
        return getBaseURL()+"search/movie?"+getAPIKey()+"&language=en-US&query="+title+"&page=1&include_adult=true"
    }

    fun parse(json: String): JSONObject? {
        var jsonObject: JSONObject? = null
        try {
            jsonObject = JSONObject(json)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return jsonObject
    }


    private fun startLoversGame(){
        val intent = Intent(this, LoversgameActivity::class.java)
        startActivity(intent)
    }
    private fun startFriendsGame(){
        val intent = Intent(this, MovieBetweenFriends::class.java)
        startActivity(intent)
    }

    /*Navigatie*/
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.custom_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.Search -> {
                searchBar.visibility = View.VISIBLE
                return true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }

    }

    override fun onBackPressed() {
        this@MainActivity.finish()
        exitProcess(0)
    }
}