package com.example.movieroulette

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.json.JSONException
import org.json.JSONObject
import com.example.movieroulette.database.DBHelper as DBHelper


class MainActivity : AppCompatActivity() {
    private val data = ArrayList<JSONObject>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Make top week movies url
        val topWeekUrl = getTrendingMoviesOfWeek()
        //Make recyclerView
        val recyclerView: RecyclerView = findViewById(R.id.front_recycle_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        //Get data and fill recyclerView
        val job = runBlocking { getData(topWeekUrl) }
        recyclerView.adapter = adapter
        //Make databse and table for chosen movies and new game
        val db = DBHelper(this)
    }

    suspend fun getData(topWeekUrl: String) = coroutineScope{
        launch {
            //{"adult":false,"backdrop_path":"\/yYrvN5WFeGYjJnRzhY0QXuo4Isw.jpg","id":505642,"title":"Black Panther: Wakanda Forever","original_language":"en","original_title":"Black Panther: Wakanda Forever","overview":"Queen Ramonda, Shuri, M’Baku, Okoye and the Dora Milaje fight to protect their nation from intervening world powers in the wake of King T’Challa’s death. As the Wakandans strive to embrace their next chapter, the heroes must band together with the help of War Dog Nakia and Everett Ross and forge a new path for the kingdom of Wakanda.","poster_path":"\/sv1xJUazXeYqALzczSZ3O6nkH75.jpg","media_type":"movie","genre_ids":[28,12,878],"popularity":4392.866,"release_date":"2022-11-09","video":false,"vote_average":7.6,"vote_count":630}
            val res = GetRequest().main(topWeekUrl)
            if (res.substring(0, 4) == "Some") {
                Log.d("urlError", res.toString())
            } else {
                val jsonob = parse(res)
                if (jsonob != null) {
                    val movieRes = jsonob.getJSONArray("results")
                    for (i in 0 until movieRes.length()) {
//                        Log.d("sss", movieRes.getJSONObject(i).toString())
                        data.add(movieRes.getJSONObject(i))
                    }
                }
            }
        }

    }

    val adapter = CustomAdapter(data)



    fun getAPIKey(): String {
        return "api_key=8ff2d545fa19ee5ef1d52be200306079"
    }
    fun getBaseURL(): String{
        return "https://api.themoviedb.org/3/"
    }
    fun getTrendingMoviesOfWeek(): String{
        return getBaseURL()+"trending/movie/day?"+getAPIKey()
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
}