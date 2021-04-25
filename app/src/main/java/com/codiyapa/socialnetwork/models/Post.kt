package com.codiyapa.socialnetwork.models

data class Post( val text : String = "",
                 val createdBy : User = User(),
                 val time : Long = 0L,
                 val likedBy : ArrayList<String> = ArrayList()
)