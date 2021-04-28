package com.codiyapa.socialnetwork

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.codiyapa.socialnetwork.daos.PostDao
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.OnProgressListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.UploadTask.*
import kotlinx.android.synthetic.main.activity_main3.*
import java.io.IOException
import java.util.*


@Suppress("DEPRECATION")
class MainActivity3 : AppCompatActivity() {
    companion object{
        var PICK_IMAGE_REQUEST = 22
        var num = 0
        var confirm = 0
    }
    private lateinit var mAuth : FirebaseAuth
    private lateinit var filepath : Uri
    private lateinit var storage :FirebaseStorage
    private lateinit var storageReference : StorageReference
    private lateinit var imageUrl : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)
        mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser
        PostContent.hint = "What in your mind , ${user!!.displayName}"
        chooser.setOnClickListener{
            imageSelector()
            num = 1
        }
        button.setOnClickListener{
            if(num == 1){
                uploadImage()
            }else{
                if(TextUtils.isEmpty(PostContent.text.toString())){
                    Toast.makeText(this , "Fields are empty", Toast.LENGTH_LONG).show()
                }else{
                postText("hii")
                }
            }
        }
    }

    private fun postText(imageUrl : String) {
        if(TextUtils.isEmpty(PostContent.text.toString())){
            val content = " "
            PostDao().createPost(content, imageUrl)
            finish()
        }else{
            val content = PostContent.text.toString()
            PostDao().createPost(content, imageUrl)
            finish()
        }
    }

    private fun imageSelector() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(
                intent,
                "Select Image from here..."
            ),
            PICK_IMAGE_REQUEST
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST
            && resultCode == RESULT_OK
            && data != null
            && data.data != null){
            filepath = data.data!!

            try {

                // Setting image on image view using Bitmap
                val bitmap = MediaStore.Images.Media
                    .getBitmap(
                        contentResolver,
                        filepath
                    )
                imageView3.setImageBitmap(bitmap)
            } catch (e: IOException) {
                // Log the exception
                e.printStackTrace()
            }
        }
    }
    private fun uploadImage() {
        storage = FirebaseStorage.getInstance();
        storageReference = storage.reference;
        if (filepath != null) {
            val progressDialog = ProgressDialog(this)
            progressDialog.setTitle("Uploading...")
            progressDialog.show()
            val name = UUID.randomUUID().toString()
            val ref = storageReference.child("images/$name")
            val uploadTask = ref.putFile(filepath)
                .addOnSuccessListener { // Image uploaded successfully
                    // Dismiss dialog
                    progressDialog.dismiss()
                    Toast.makeText(this, "Image Uploaded!!", Toast.LENGTH_SHORT).show()
                    storageReference.child("images/$name").downloadUrl.addOnSuccessListener { uri ->
                        // Got the download URL for 'users/me/profile.png'
                        imageUrl = uri.toString()
                        postText(imageUrl)
                    }.addOnFailureListener {
                        // Handle any errors
                    }
                }
                .addOnFailureListener { e -> // Error, Image not uploaded
                    progressDialog.dismiss()
                    Toast.makeText(this, "Failed " + e.message, Toast.LENGTH_SHORT).show()
                }
        }
    }
}