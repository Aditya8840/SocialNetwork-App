package com.codiyapa.socialnetwork.daos

import com.codiyapa.socialnetwork.models.Post
import com.codiyapa.socialnetwork.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class PostDao {
    private val db = FirebaseFirestore.getInstance()
    val posts = db.collection("content")
    private val mAuth = FirebaseAuth.getInstance()
    val userS = mAuth.currentUser
    fun createPost(text: String, imageUrl: String){
        val user = User(
                userS.uid,
                userS.displayName,
                userS.photoUrl.toString()
        )
        val time = System.currentTimeMillis()
        val post = Post(text, user, time, imageUrl)
        posts.document().set(post)
    }
}