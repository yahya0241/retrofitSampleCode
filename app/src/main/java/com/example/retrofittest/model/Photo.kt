package com.example.retrofittest.model

import com.google.gson.annotations.SerializedName

class Photo {
    @SerializedName("albumId")
    var albumId: Int = 0

    @SerializedName("id")
    var id:Int = 0

    @SerializedName("title")
    lateinit var title:String

    @SerializedName("url")
    lateinit var url:String

    @SerializedName("thumbnailUrl")
    lateinit var thumbnailUrl:String

    constructor(albumId: Int, id: Int, title: String, url: String, thumbnailUrl:String) {
        this.albumId = albumId
        this.id = id
        this.title = title
        this.url = url
        this.thumbnailUrl = thumbnailUrl
    }
}