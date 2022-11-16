package com.example.movieroulette

import android.util.Log
import java.net.URL
import java.net.HttpURLConnection
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*


class GetRequest {
    suspend fun main(uri: String): String {
        val url = URL(uri)
        var returnline: String = ""
        var line: String = ""
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"


        val client = HttpClient(CIO){expectSuccess = true}
        val response: HttpResponse = client.get(url)
        client.close()
        if (response.status.toString() == "200 OK")
            return response.body()
        return "Someting whent wrong: " + response.status.toString()

    }
}