package com.example.movieroulette.database

import android.util.Log
import com.example.movieroulette.models.QnAModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class FirebaseDBHelper {


    val database = Firebase.database.getReference("QNA")
    companion object {
        var questionArr = ArrayList<QnAModel>()
        var random = Math.floor((Math.random() * RoomDBHelper.chosenMovieArr.size))
    }
//    val firebaseDb = FirebaseFirestore.getInstance()
//        get() {return field}

     fun getQnA(callback: (questA: ArrayList<QnAModel>) -> String){
           database.addListenerForSingleValueEvent(object : ValueEventListener {
               override fun onDataChange(p0: DataSnapshot) {
                   val children = p0.children
                   val c = children.count()
                   children.forEach {
                       val qm = QnAModel()
                       qm.id = it.key.toString()
                       qm.answer = it.child("answer").value.toString()
                       qm.question = it.child("question").value.toString()
                       questionArr.add(qm)
                       Log.e("ededsize1", questionArr.size.toString())
                   }
                   Log.e("ededsize", questionArr.size.toString())
                   callback.invoke(questionArr)
               }

               override fun onCancelled(p0: DatabaseError) {
                   Log.e("FirebaseError", "loadPost:onCancelled", p0.toException())
               }

           })
    }


}