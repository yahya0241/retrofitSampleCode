package com.example.retrofittest.model

import com.google.gson.annotations.SerializedName

class Posts {

    @SerializedName("userId")
    var userId: Int = 0

    @SerializedName("id")
    var id: Int = 0

    @SerializedName("title")
    lateinit var title: String

    @SerializedName("body")
    lateinit var body: String

    constructor(userId: Int, id: Int, title: String, body: String) {
        this.userId = userId
        this.id = id
        this.title = title
        this.body = body
    }

    override fun toString(): String {
        return "Posts(userId=$userId, id=$id, title='$title', body='$body')"
    }


}
