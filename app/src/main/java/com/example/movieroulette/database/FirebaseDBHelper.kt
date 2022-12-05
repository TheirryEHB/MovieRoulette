package com.example.movieroulette.database

import android.util.Log
import com.example.movieroulette.models.QnAModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlin.math.floor
import kotlin.reflect.KFunction1

class FirebaseDBHelper {

    companion object {
        var questionArr = ArrayList<QnAModel>()
        var random = floor((Math.random() * RoomDBHelper.chosenMovieArr.size))
        var database = Firebase.database.getReference("QNA")

//    val firebaseDb = FirebaseFirestore.getInstance()
//        get() {return field}

        fun getQnA(callback: (questA: ArrayList<QnAModel>) -> Unit) {
            database.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    val children = p0.children
                    val c = children.count()
                    Log.d("ededsize1", c.toString())
//                    for (i in 1 until c - 1) {
//                        val qm = QnAModel()
//                        qm.id = children.elementAt(i).key.toString()
//                        qm.answer = children.elementAt(i).child("answer").value.toString()
//                        qm.question = children.elementAt(i).child("question").value.toString()
//                        questionArr.add(qm)
//                        Log.e("ededsize1", questionArr.size.toString())
//                    }
                    children.forEach {
                        val qm = QnAModel()
                        qm.id = it.key.toString()
                        qm.answer = it.child("answer").value.toString()
                        qm.question = it.child("question").value.toString()
                        questionArr.add(qm)
                        Log.e("ededsize1", questionArr.size.toString())
                    }
                    callback.invoke(questionArr)
                }

                override fun onCancelled(p0: DatabaseError) {
                    Log.e("FirebaseError", "loadPost:onCancelled", p0.toException())
                }

            })
        }
    }


}