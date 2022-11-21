package com.example.movieroulette.database

import androidx.room.*
import com.example.movieroulette.models.MovieModel

class RoomDBHelper {

    var db: RoomDatabase? = null
        get() { return field }
    companion object {
        var chosenMovieArr: ArrayList<MovieModel> = ArrayList()
    }

    @Entity
    data class FriendsGame(
        @PrimaryKey(autoGenerate = true) val uid: Int?,
        @ColumnInfo(name = "quest_id") val QuestId: String?,
        @ColumnInfo(name = "did_ans") val DidAns: Boolean?,
        @ColumnInfo(name = "is_right") val isRight: Boolean?,
        @ColumnInfo(name = "answer_time") val AnswerTime: Int?
    )
    @Dao
    interface FriendsGameDao{
        @Query("SELECT * FROM FriendsGame")
        fun getAll(): List<FriendsGame>

        @Query("SELECT uid FROM FriendsGame WHERE did_ans = :ans")
        fun findByAnswer(ans: String):FriendsGame

        @Query("SELECT uid FROM FriendsGame WHERE is_right = :right")
        fun findByRight(right: String):FriendsGame

        @Query("SELECT answer_time FROM friendsgame WHERE uid IN (:gameIds)")
        fun loadAllByIds(gameIds: IntArray): List<FriendsGame>
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

    @Database(entities = [FriendsGame::class, LoversGame::class, ChosenMovie::class], version = 1)
    abstract class AppDatabase : RoomDatabase() {
        abstract fun FriendsGameDao() : FriendsGameDao
        abstract fun ChosenMovieDao() : ChosenMovieDao

    }


}