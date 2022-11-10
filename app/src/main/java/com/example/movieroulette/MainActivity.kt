package com.example.movieroulette

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.json.JSONObject
import java.net.URL

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val trendingWeek = URL(getTrendingMoviesOfWeek()).readText()
    }

    fun getAPIKey(): String {
        return "api_key=8ff2d545fa19ee5ef1d52be200306079"
    }
    fun getBaseURL(): String{
        return "https://api.themoviedb.org/3/"
    }
    fun getTrendingMoviesOfWeek(): String{
        return getBaseURL()+"trndeing/movie/week"+getAPIKey()
    }
}