package com.example.movieroulette.database

import android.util.Log
import androidx.room.Database
import com.example.movieroulette.models.QnAModel
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

class FirebaseDBHelper {


    val database = Firebase.database.getReference("QNA")
    companion object {
        var questionArr = ArrayList<QnAModel>()
        var random = Math.floor((Math.random() * RoomDBHelper.chosenMovieArr.size))
    }
//    val firebaseDb = FirebaseFirestore.getInstance()
//        get() {return field}


    suspend fun getQnA(): ArrayList<QnAModel> {

       database.addListenerForSingleValueEvent(object : ValueEventListener {
           override fun onDataChange(p0: DataSnapshot) {
               val children = p0.children
               children.forEach {
                   val qm = QnAModel()

                   if (it != null) {
                       qm.id = it.key.toString()
                       qm.answer = it.child("answer").value.toString()
                       qm.question = it.child("question").value.toString()
                       questionArr.add(qm)
                       Log.e("ededsize", questionArr.size.toString())
                   }
               }
           }

           override fun onCancelled(p0: DatabaseError) {
               Log.e("FirebaseError", "loadPost:onCancelled", p0.toException())
           }

           })

        delay(2000L)
        Log.e("ededsizes", questionArr.size.toString())
        return questionArr
    }


}