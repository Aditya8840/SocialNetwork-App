package com.codiyapa.socialnetwork

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.codiyapa.socialnetwork.daos.PostDao
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main3.*

class MainActivity3 : AppCompatActivity() {
    private lateinit var mAuth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)
        mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser
        PostContent.hint = "What in your mind , ${user!!.displayName}"
        button.setOnClickListener{
            val content = PostContent.text.toString().trim()
            if(content.isNotEmpty()){
                PostDao().createPost(content)
                finish()
            }
        }
    }
}