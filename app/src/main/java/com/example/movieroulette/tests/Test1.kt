package com.example.movieroulette.tests

import com.example.movieroulette.MainActivity
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.json.JSONException
import org.json.JSONObject
import org.junit.Before
import org.junit.Test

class Test1 {

        lateinit var mainActivitySpy: MainActivity

        @Before
        fun setUp() {
        }

        @Test
        fun jsonObjectParseTest() {

            val stringBuilder: StringBuilder =
                java.lang.StringBuilder("{\"adult\":false,\"backdrop_path\":\"\\/yYrvN5WFeGYjJnRzhY0QXuo4Isw.jpg\",\"id\":505642,\"title\":\"Black Panther: Wakanda Forever\",\"original_language\":\"en\",\"original_title\":\"Black Panther: Wakanda Forever\",\"overview\":\"Queen Ramonda, Shuri, M’Baku, Okoye and the Dora Milaje fight to protect their nation from intervening world powers in the wake of King T’Challa’s death. As the Wakandans strive to embrace their next chapter, the heroes must band together with the help of War Dog Nakia and Everett Ross and forge a new path for the kingdom of Wakanda.\",\"poster_path\":\"\\/sv1xJUazXeYqALzczSZ3O6nkH75.jpg\",\"media_type\":\"movie\",\"genre_ids\":[28,12,878],\"popularity\":4392.866,\"release_date\":\"2022-11-09\",\"video\":false,\"vote_average\":7.6,\"vote_count\":630}")
            val ob = parse(stringBuilder.toString())
            assertTrue(ob != null)
            if (ob != null) {
                assertEquals(ob.getString("adult"), false)
            }
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