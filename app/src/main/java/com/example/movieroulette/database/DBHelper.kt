package com.example.movieroulette.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) :
    SQLiteOpenHelper(context, "DB1", null, 1) {

//    private val dab: SQLiteDatabase = context.openOrCreateDatabase("DB1", Context.MODE_PRIVATE, null)

    override fun onCreate(db: SQLiteDatabase) {
        var query = ("CREATE TABLE " + "chosenmovies" + " ("
                + "ID_mov" + " INTEGER PRIMARY KEY, " +
                "Name_mov" + " TEXT" + ")"
                )
        db.execSQL(query)
        query = ("CREATE TABLE " + "QNA" + " ("
                + "ID_qna" + " INTEGER PRIMARY KEY, " +
                "Question" + " TEXT, " +
                "Answer" + " TEXT" + ")"
                )
        db.execSQL(query)
        query = ("CREATE TABLE " + "Game" + " ("
                + "ID_game" + " INTEGER PRIMARY KEY, " +
                "Question_id" + "INTEGER, " +
                "Did_ans_1" + "TEXT, " +
                "Did_ans_2" + "TEXT, " +
                "Time1" + "INTEGER, " +
                "Time2" + "INTEGER" + ")"
                )
        db.execSQL(query)

    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + "chosenmovies")
        onCreate(db)
    }
}