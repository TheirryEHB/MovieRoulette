package com.example.movieroulette.database

import android.util.Log
import androidx.room.Database
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FirebaseDBHelper {


    val database = Firebase.database.getReference("QNA")
//    val firebaseDb = FirebaseFirestore.getInstance()
//        get() {return field}


    fun getQnA(){
        Log.d("cccc", database.toString())
       database.addListenerForSingleValueEvent(object : ValueEventListener{
           override fun onDataChange(p0: DataSnapshot) {
               val children = p0.children
               children.forEach {
                   Log.d("chli", it.toString())
               }
           }

           override fun onCancelled(p0: DatabaseError) {
               Log.e("FirebaseError", "loadPost:onCancelled", p0.toException())
           }

       })
    }


}