package com.codiyapa.socialnetwork

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codiyapa.socialnetwork.daos.PostDao
import com.codiyapa.socialnetwork.models.Post
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.post_item.view.*

class PostAdapter(options: FirestoreRecyclerOptions<Post>, private var mAuth: FirebaseAuth, val listener: MainActivity2) : FirestoreRecyclerAdapter<Post, PostAdapter.PostViewHolder>(
        options
) {
    class PostViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val userImage: ImageView = view.userImage
        val userName: TextView = view.userName
        val createdAt: TextView = view.createdAt
        val postText: TextView = view.postTitle
        val likeButton: ImageView = view.likeButton
        val likeCount: TextView = view.likeCount
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = PostViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.post_item, parent, false))
        return view
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int, model: Post) {
        holder.postText.text = model.text
        holder.userName.text = model.createdBy.displayName
        Glide.with(holder.userImage.context).load(model.createdBy.imageUrl).circleCrop().into(holder.userImage)
        holder.likeCount.text = model.likedBy.size.toString()
        holder.createdAt.text = Utils.getTimeAgo(model.time)
        val user = mAuth.currentUser
        val userId = user.uid
        val isLiked = model.likedBy.contains(userId)
        if (isLiked){
            holder.likeButton.setImageDrawable(ContextCompat.getDrawable(holder.likeButton.context, R.drawable.liked))
        }else{
            holder.likeButton.setImageDrawable(ContextCompat.getDrawable(holder.likeButton.context, R.drawable.unlike))
        }
        holder.likeButton.setOnClickListener {
            if (isLiked) {
                model.likedBy.remove(userId)
                holder.likeButton.setImageDrawable(ContextCompat.getDrawable(holder.likeButton.context, R.drawable.unlike))
                val postId = snapshots.getSnapshot(position).id
                val post = Post(model.text, model.createdBy, model.time, model.likedBy)
                PostDao().posts.document(postId).set(post)
            } else {
                model.likedBy.add(userId)
                holder.likeButton.setImageDrawable(ContextCompat.getDrawable(holder.likeButton.context, R.drawable.liked))
                val postId = snapshots.getSnapshot(position).id
                val post = Post(model.text, model.createdBy, model.time, model.likedBy)
                PostDao().posts.document(postId).set(post)
            }
        }

    }
}