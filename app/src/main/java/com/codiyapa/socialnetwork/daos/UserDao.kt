package com.codiyapa.socialnetwork.daos

import com.codiyapa.socialnetwork.models.User
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class UserDao {
    private val db = FirebaseFirestore.getInstance()
    private val userCollection = db.collection("users")
    fun addUser(user : User?){
        user?.let {
            userCollection.document(user.uid).set(it)
        }
    }

    fun findUserById(uid : String): Task<DocumentSnapshot> {
        return userCollection.document(uid).get()
    }
}