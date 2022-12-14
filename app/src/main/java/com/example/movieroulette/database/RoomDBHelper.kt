package com.example.movieroulette.database

import android.util.Log
import androidx.room.*
import androidx.room.migration.AutoMigrationSpec
import com.example.movieroulette.models.MovieModel
import org.json.JSONException
import org.json.JSONObject

class RoomDBHelper {

    var db: RoomDatabase? = null
        get() { return field }
    companion object {
        var chosenMovieArr: ArrayList<MovieModel> = ArrayList()

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

    @Entity(tableName = "FriendsGame")
    data class FriendsGame(
        @PrimaryKey val Uuid: String,
        @ColumnInfo(name = "movie_name") val MovieName: String?,
        @ColumnInfo(name = "quest_id") val QuestId: String?,
        @ColumnInfo(name = "question") val Question: String?,
        @ColumnInfo(name = "answer") val Answer: String?,
        @ColumnInfo(name = "did_ans") var DidAns: Boolean?,
        @ColumnInfo(name = "is_right") var isRight: Boolean?,
        @ColumnInfo(name = "answer_time") var AnswerTime: Int?
    )
    @Dao
    interface FriendsGameDao{
        @Query("SELECT * FROM FriendsGame")
        fun getAll(): List<FriendsGame>

        @Query("SELECT Uuid FROM FriendsGame WHERE did_ans = :ans")
        fun findByAnswer(ans: String):FriendsGame

        @Query("SELECT Question, Uuid FROM FriendsGame WHERE Uuid = (:uuid)")
        fun getQuestion(uuid: String):FriendsGame

        @Query("SELECT Answer, Uuid FROM FriendsGame WHERE Uuid = (:uuid)")
        fun getAnswer(uuid: String):FriendsGame

        @Query("SELECT * FROM FriendsGame WHERE is_right = :right")
        fun findByRight(right: String): List<FriendsGame>

        @Query("SELECT answer_time, Uuid FROM friendsgame WHERE Uuid IN (:gameIds)")
        fun loadAllByIds(gameIds: Array<String>): List<FriendsGame>


        @Insert
        fun insertGame(fi: FriendsGame)

        @Query("UPDATE FriendsGame SET did_ans=:didAns, is_right=:isRight, answer_time=:ansTime WHERE Uuid=(:uuid)")
        fun update(didAns: String, isRight:String, ansTime:String, uuid: String)

        @Query("DELETE FROM FriendsGame")
        fun nukeTable()
    }

    @Entity
    data class LoversGame(
        @PrimaryKey(autoGenerate = true) val uid: Int,
        @ColumnInfo(name = "winner_movie_id") val WinnerMovieId: String?
    )

    @Entity
    data class ChosenMovie(
        @PrimaryKey(autoGenerate = true) val uid: Int?,
        @ColumnInfo(name = "movie_id") val MovieId:String?,
        @ColumnInfo(name = "game_id") val QuestId: Int?,
        @ColumnInfo(name = "movie_name") val MovieName:String?,
        @ColumnInfo(name = "movie_ratring") val Rating:String?,
        @ColumnInfo(name = "movie_release") val ReleaseDate:String?,
        @ColumnInfo(name = "movie_lang") val MovieLang:String?
    )
    @Dao
    interface ChosenMovieDao {
        @Query("SELECT * FROM ChosenMovie")
        fun getAll(): List<ChosenMovie>

        @Query("SELECT movie_id FROM ChosenMovie WHERE game_id = :gameid")
        fun findByQid(gameid: Int):ChosenMovie
    }


    @Database(entities = [FriendsGame::class, LoversGame::class, ChosenMovie::class], version = 2)
    //autoMigrations = [ AutoMigration (from = 1, to = 2,spec = AppDatabase.MyAutoMigration::class)]

    abstract class AppDatabase : RoomDatabase() {

        @DeleteTable(tableName = "FriendsGame")
        class MyAutoMigration : AutoMigrationSpec

        abstract fun FriendsGameDao() : FriendsGameDao
        abstract fun ChosenMovieDao() : ChosenMovieDao



    }


}